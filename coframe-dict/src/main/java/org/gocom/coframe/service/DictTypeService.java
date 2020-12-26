package org.gocom.coframe.service;

import static org.gocom.coframe.CoframeConstants.TEMPLATE_NAME;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.model.DictEntryInternational;
import org.gocom.coframe.model.DictResponse;
import org.gocom.coframe.model.DictType;
import org.gocom.coframe.model.DictTypeInternational;
import org.gocom.coframe.repository.DictEntryInternationalRepository;
import org.gocom.coframe.repository.DictTypeInternationalRepository;
import org.gocom.coframe.repository.DictTypeRepository;
import org.gocom.coframe.service.excel.ChangeUtil;
import org.gocom.coframe.service.excel.DictExcelImporter;
import org.gocom.coframe.service.excel.ExcelDictLine;
import org.gocom.coframe.service.excel.ExcelTemplate;
import org.gocom.coframe.service.excel.ImportDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

@Service
public class DictTypeService extends AbstractTreeModelService<DictType, DictType.Criteria> {
	@Autowired
	private DictTypeRepository dictTypeRepository;
	@Autowired
	private DictEntryService dictEntryService;
	@Autowired
	private DictEntryInternationalService entryInternalSvc;
	@Autowired
	private DictTypeInternationalService typeInternalSvc;
	@Autowired
	private DictTypeInternationalRepository typeInternalRepo;
	@Autowired
	private DictEntryInternationalRepository entryInternalRepo;

	private int typeCount = 0;
	private int entryCount = 0;

	@Override
	protected CoframeJpaRepository<DictType, String> getRepository() {
		return dictTypeRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gocom.coframe.core.service.AbstractTreeModelService#pagingByCriteria(org.
	 * gocom.coframe.core.model.AbstractTreeModelCriteria,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<DictType> pagingByCriteria(DictType.Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC,"sortNo").and(new Sort(Direction.DESC, "createTime")));
		Page<DictType> dictTypes = dictTypeRepository.findAll(dictTypeRepository.toSpecification(criteria), pageRequest);
		if(criteria.isLoadChildren()) {
			if(criteria.getLoadChildrenLevel() >=0) {
				dictTypes.stream().forEach(dictType -> {
					loadTreeModel(dictType, criteria.getLoadChildrenLevel(), criteria.isLoadOthers());
				});
			}
		}
		return dictTypes;
	}

	public List<DictEntry> findByCriteriaForDictEntry(String dictTypeId, String subDictEntryName, String subDictEntryCode) {
		DictEntry.Criteria criteria = new DictEntry.Criteria();
		criteria.setDictTypeId(dictTypeId);
		criteria.setCode(subDictEntryCode);
		criteria.setName(subDictEntryName);
		return dictEntryService.queryDictEntyFromDict(criteria);
	}

	public List<DictEntry> findByDictIdForDictEntry(String dictTypeId) {
		DictEntry.Criteria criteria = new DictEntry.Criteria();
		criteria.setDictTypeId(dictTypeId);
		return dictEntryService.findByDictIdForDictEntry(criteria);
	}

	public List<DictType> findByCriteria(String dictTypeId, String subDictName, String subDictCode) {
		DictType.Criteria criteria = new DictType.Criteria();
		criteria.setParentId(dictTypeId);
		criteria.setCode(subDictCode);
		criteria.setName(subDictName);
		return dictTypeRepository.findAll(dictTypeRepository.toSpecification(criteria));
	}

	public Optional<DictType> findOne(String code) {
		DictType.Criteria criteria = new DictType.Criteria();
		criteria.setUsingLikeQuery(false);
		criteria.setCode(code);
		return dictTypeRepository.findOne(dictTypeRepository.toSpecification(criteria));
	}

	@Transactional(rollbackFor = Throwable.class)
	public DictResponse importExcel(InputStream excelSource, boolean overideExists) {
		int tempTypeCount = 0;
		int tempEntryCount = 0;
		ImportDict importDict = null;
		try {
			importDict = DictExcelImporter.dictImport(new ExcelTemplate(new ClassPathResource(TEMPLATE_NAME)), excelSource);
			Optional.ofNullable(importDict).orElseThrow(() -> CoframeErrorCode.DICT_TYPE_IMPORT_DATA_IS_NOT_NULL.runtimeException());
			if(importDict.getLines().size() == 0) {
				throw CoframeErrorCode.DICT_TYPE_IMPORT_DATA_IS_NOT_NULL.runtimeException();
			}
		} catch (IOException e) {
			throw CoframeErrorCode.DICT_TYPE_IMPORT_FAILTURE.runtimeException();
		}
		importDict.getLines().stream().map(line -> {
			return Tuple.of(line);
		}).forEach(t -> {
			DictType dictType = t.getDict();
			Optional.ofNullable(dictType.getName()).orElseThrow(() -> CoframeErrorCode.DICTTYPE_NAME_IS_NOT_NULL.runtimeException());
			Optional<DictType> dict = findOne(dictType.getCode());
			DictType savedType = importDictType(dictType, dictType.getLocale(), overideExists, dict);

			DictEntry dictEntry = t.getDictEntry();
			if (dictEntry.getCode() != null) {
				Optional.ofNullable(dictEntry.getName()).orElseThrow(() -> CoframeErrorCode.DICTENTRY_NAME_IS_NOT_NULL.runtimeException());
				Optional<DictEntry> entry = dictEntryService.findOne(dictEntry.getCode(),savedType.getId());
				importDictEntry(entry, dict, savedType, dictEntry, overideExists, dictEntry.getLocale());
			}
		});
		tempTypeCount = typeCount;
		tempEntryCount = entryCount;
		typeCount = 0;
		entryCount = 0;
		return new DictResponse(tempTypeCount, tempEntryCount);
	}

	private void importDictEntry(Optional<DictEntry> entry, Optional<DictType> dict, DictType savedType,
			DictEntry dictEntry, boolean overideExists, String locale) {
		if (entry.isPresent()) {
			if (overideExists) {
				overideDictEntry(dictEntry, entry, savedType);
			} else {
				throw CoframeErrorCode.FOUND_DUPLICATED_MODEL_WITH_SAME_CODE.runtimeException();
			}
			// 如果父类是后面导入的字典
			if (entry.get().getLocale() == null) {
				overideDictEntry(dictEntry, entry, savedType);
			}
			if (locale != null) {
				DictEntryInternational.Criteria criteria = new DictEntryInternational.Criteria();
				criteria.setLocale(locale);
				if (!locale.equals(entry.get().getLocale()) && entryInternalRepo
						.count(entryInternalRepo.toDictEntrySpecification(criteria, entry.get().getId())) == 0) {
					entryInternalSvc.save(entry.get(), dictEntry.getName(), locale);
				} else if (!locale.equals(entry.get().getLocale()) && entryInternalRepo
						.count(entryInternalRepo.toDictEntrySpecification(criteria, entry.get().getId())) != 0) {
					DictEntryInternational entryInternal = entryInternalRepo
							.findAll(entryInternalRepo.toDictEntrySpecification(criteria, entry.get().getId())).get(0);
					if (!dictEntry.getName().equals(entryInternal.getDictEntryName())) {
						entryInternal.setDictEntryName(dictEntry.getName());
						entryInternalSvc.update(entryInternal);
					}
				}
			}
		} else {
			if (dict.isPresent() && !overideExists) {
				saveParentEntry(dictEntry, dict.get());
				dictEntry.setDictType(dict.get());
			} else {
				saveParentEntry(dictEntry, savedType);
				dictEntry.setDictType(savedType);
			}
			dictEntryService.create(dictEntry);
			entryCount = entryCount + 1;
		}
	}

	private void overideDictEntry(DictEntry dictEntry, Optional<DictEntry> entry, DictType savedType) {
		DictEntry parentEntry = saveParentEntry(dictEntry, savedType);
		dictEntry.setDictType(savedType);
		dictEntry.setId(entry.get().getId());
		Optional.ofNullable(parentEntry).ifPresent(value -> {
			dictEntry.setParentId(value.getId());
		});
		dictEntryService.move(dictEntry);
	}

	private DictType importDictType(DictType dictType, String locale, boolean overideExists, Optional<DictType> dict) {
		DictType savedType = null;
		if (dict.isPresent()) {
			if (overideExists) {
				savedType = overrideDictType(dictType, dict);
			} else {
				throw CoframeErrorCode.FOUND_DUPLICATED_MODEL_WITH_SAME_CODE.runtimeException();
			}
			// 父字典类型为后面导入的数据
			if (dict.get().getLocale() == null) {
				savedType = overrideDictType(dictType, dict);
			}
			if (locale != null) {
				DictTypeInternational.Criteria criteria = new DictTypeInternational.Criteria();
				criteria.setLocale(locale);
				if (!locale.equals(dict.get().getLocale()) && typeInternalRepo
						.count(typeInternalRepo.toDictTypeSpecification(criteria, dict.get().getId())) == 0) {
					typeInternalSvc.save(dict.get(), dictType.getName(), locale);
				} else if (!locale.equals(dict.get().getLocale()) && typeInternalRepo
						.count(typeInternalRepo.toDictTypeSpecification(criteria, dict.get().getId())) != 0) {
					DictTypeInternational typeInternal = typeInternalRepo
							.findAll(typeInternalRepo.toDictTypeSpecification(criteria, dict.get().getId())).get(0);
					if (!dictType.getName().equals(typeInternal.getDictTypeName())) {
						typeInternal.setDictTypeName(dictType.getName());
						typeInternalSvc.update(typeInternal);
					}
				}
			}
		} else {
			saveParentType(dictType);
			savedType = create(dictType);
			typeCount = typeCount + 1;
		}
		return savedType;
	}

	private DictType overrideDictType(DictType dictType, Optional<DictType> dict) {
		DictType parentType = saveParentType(dictType);
		dictType.setId(dict.get().getId());
		Optional.ofNullable(parentType).ifPresent(value -> {
			dictType.setParentId(value.getId());
		});
		super.checkAndMove(dictType);
		return findOne(dictType.getCode()).get();
	}

	@Override
	public void preUpdate(DictType dictType) {
	}

	private DictEntry saveParentEntry(DictEntry dictEntry, DictType dictType) {
		DictEntry parentEntry = null;
		if (dictEntry.getParent() != null) {
			if (StringUtils.isBlank(dictEntry.getParent().getCode())) {
				dictEntry.setParentId(null);
				return null;
			}
			if (StringUtils.isNotBlank(dictEntry.getParent().getCode())) {
				if (StringUtils.isEmpty(dictEntry.getParent().getName())) {
					throw CoframeErrorCode.DICTENTRY_NAME_IS_NOT_NULL.runtimeException();
				} else {
					parentEntry = dictEntry.getParent();
					DictType parentType = null;
					if(StringUtils.isNotBlank(dictType.getParentId())) {
						 parentType = dictTypeRepository.findById(dictType.getParentId()).get();
						parentEntry.setDictType(parentType);
					} else {
						throw CoframeErrorCode.DONT_HAVE_PARENT_DICTTYPE.runtimeException() ;
					}
					Optional<DictEntry> nameEntry = null;
						nameEntry = dictEntryService.findOne(parentEntry.getCode(),dictType.getParentId());
					if (!nameEntry.isPresent()) {
						parentEntry = dictEntryService.create(parentEntry);
						entryCount = entryCount + 1;
						return parentEntry;
					} else {
						nameEntry.get().setDictType(parentType);
						return dictEntryService.update(nameEntry.get());
					}
				}
			}
		}
		return null;
	}

	private DictType saveParentType(DictType dictType) {
		DictType parentType = null;
		if (dictType.getParent() != null) {
			if (StringUtils.isBlank(dictType.getParent().getCode())) {
				dictType.setParentId(null);
				return null;
			}
			if (StringUtils.isNotBlank(dictType.getParent().getCode())) {
				if (!StringUtils.isEmpty(dictType.getParent().getName())) {
					parentType = dictType.getParent();
					parentType.setCode(parentType.getCode());
					Optional<DictType> nameDict = findOne(parentType.getCode());
					if (!nameDict.isPresent()) {
						parentType = create(parentType);
						typeCount = typeCount + 1;
						dictType.setParentId(parentType.getId());
						return parentType;
					} else {
						dictType.setParentId(nameDict.get().getId());
						return nameDict.get();
					}
				} else {
					throw CoframeErrorCode.PARENT_DICTYTYPE_NAME_IS_NOT_NULL.runtimeException();
				}
			}
		}
		return null;
	}

	public void exportExcelByCodes(OutputStream out, String locale, String... codes) {
		List<ExcelDictLine> list = new ArrayList<>();
		Arrays.asList(codes).stream().forEach(code -> {
			DictType.Criteria dictCriteria = new DictType.Criteria();
			dictCriteria.setCode(code);
			dictCriteria.setUsingLikeQuery(false);
			List<DictType> dicts = dictTypeRepository.findAll(dictTypeRepository.toSpecification(dictCriteria));
			generateExcelLine(dicts, list, locale);
		});
		exportExcel(list, out);
	}

	private void exportExcel(List<ExcelDictLine> list, OutputStream out) {
			try {
				ClassPathResource resource = new ClassPathResource(TEMPLATE_NAME);
				InputStream inputStream;
				inputStream = resource.getInputStream();
				File targetFile = new File(UUID.randomUUID().toString() + ".xls");
				inputstreamtofile(inputStream, targetFile);

				FileSystemResource fileSystemResource = new FileSystemResource(targetFile);
				ExcelTemplate excelTemplate = new ExcelTemplate(fileSystemResource);
				DictExcelImporter.dictExport(list, null, excelTemplate, out);
				out.flush();
				out.close();
				FileSystemUtils.deleteRecursively(targetFile);
			} catch (IOException e) {
				throw CoframeErrorCode.DICT_TYPE_EXPORT_FAILTURE.runtimeException();
			}
	}

	public void exportExcelByIds(OutputStream out, String locale, String... ids) {
		List<ExcelDictLine> list = new ArrayList<>();
		Arrays.asList(ids).stream().forEach(id -> {
			DictType.Criteria dictCriteria = new DictType.Criteria();
			dictCriteria.setId(id);
			dictCriteria.setUsingLikeQuery(false);
			List<DictType> dicts = dictTypeRepository.findAll(dictTypeRepository.toSpecification(dictCriteria));
			generateExcelLine(dicts, list, locale);
		});
		exportExcel(list, out);
	}

	private void generateExcelLine(List<DictType> dicts, List<ExcelDictLine> list, String locale) {
		DictEntry.Criteria entryCriteria = new DictEntry.Criteria();
		dicts.stream().forEach(dict -> {
			if (locale != null) {
				if (!locale.equals(dict.getLocale())) {
					dict = findById(dict.getId(), locale);
				}
			}
			if (dict.getParentId() != null) {
				dict.setParent(findById(dict.getParentId()));
			}
		});

		dicts.stream().forEach(dictType -> {
			entryCriteria.setDictTypeCode(dictType.getCode());
			List<DictEntry> entrys = dictEntryService.queryDictEntyByDictCode(entryCriteria);
			if (entrys.size() == 0) {
				list.add(new Tuple(dictType, null).mapToExcelDictLine());
			} else {
				entrys.forEach(dictEntry -> {
					if (locale != null) {
						if (!locale.equals(dictEntry.getLocale())) {
							dictEntry = dictEntryService.findById(dictEntry.getId(), locale);
						}
					}
					if (dictEntry.getParentId() != null) {
						dictEntry.setParent(dictEntryService.findById(dictEntry.getParentId()));
					}
					list.add(new Tuple(dictType, dictEntry).mapToExcelDictLine());
				});
			}
		});
	}

	private void inputstreamtofile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			toFileOperation(ins, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void toFileOperation(InputStream ins, OutputStream os) {
		try {
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class Tuple {
		private DictType dictType;
		private DictEntry dictEntry;

		public Tuple() {

		}

		public Tuple(DictType dictType, DictEntry dictEntry) {
			this.dictType = dictType;
			this.dictEntry = dictEntry;
		}

		public DictType getDict() {
			return dictType;
		}

		public void setDict(DictType dictType) {
			this.dictType = dictType;
		}

		public DictEntry getDictEntry() {
			return dictEntry;
		}

		public void setDictEntry(DictEntry dictEntry) {
			this.dictEntry = dictEntry;
		}

		public ExcelDictLine mapToExcelDictLine() {
			ExcelDictLine line = new ExcelDictLine();
			line.setDictTypeCode(dictType.getCode());
			line.setDictTypeName(dictType.getName());
			line.setDictTypeSortNo(!Optional.ofNullable(dictType.getSortNo()).isPresent() ? 
					String.valueOf(0) : String.valueOf(dictType.getSortNo()));
			line.setDictTypeLocale(dictType.getLocale());
			line.setParentTypeCode(!Optional.ofNullable(dictType.getParent()).isPresent() ? 
					null : dictType.getParent().getCode());
			line.setParentTypeName(!Optional.ofNullable(dictType.getParent()).isPresent() ? 
					null : dictType.getParent().getName());
			Optional.ofNullable(dictEntry).ifPresent(value -> {
				line.setDictEntryCode(value.getCode());
				line.setDictEntryName(value.getName());
				line.setStatus(value.getStatus());
				line.setDictEntryLocale(value.getLocale());
				line.setDictEntrySortNo(!Optional.ofNullable(value.getSortNo()).isPresent() ? 
						String.valueOf(0) : String.valueOf(value.getSortNo()));
				line.setParentEntryCode(!Optional.ofNullable(value.getParent()).isPresent() ? 
						null : value.getParent().getCode());
				line.setParentEntryName(!Optional.ofNullable(value.getParent()).isPresent() ? 
						null : value.getParent().getName());
			});
			return line;
		}

		static Tuple of(ExcelDictLine line) {
			DictType dictType = new DictType();
			DictEntry dictEntry = new DictEntry();

			dictType.setCode(line.getDictTypeCode());
			dictType.setName(line.getDictTypeName());
			dictType.setSortNo(StringUtils.isBlank(line.getDictTypeSortNo()) ? 
					0 : ChangeUtil.toInteger(line.getDictTypeSortNo()));
			dictType.setLocale(StringUtils.isBlank(line.getDictTypeLocale()) ? 
					"zh" : line.getDictTypeLocale());
			dictType.setTenantId("default");
			DictType parentDict = new DictType();
			parentDict.setCode(line.getParentTypeCode());
			parentDict.setName(line.getParentTypeName());
			parentDict.setTenantId("default");
			dictType.setParent(parentDict);

			dictEntry.setCode(line.getDictEntryCode());
			dictEntry.setName(line.getDictEntryName());
			dictEntry.setSortNo(StringUtils.isBlank(line.getDictEntrySortNo()) ? 
					0 : ChangeUtil.toInteger(line.getDictEntrySortNo()));
			dictEntry.setStatus(line.getStatus());
			dictEntry.setLocale(StringUtils.isBlank(line.getDictEntryLocale()) ? 
					"zh" : line.getDictEntryLocale());
			dictEntry.setTenantId("default");
			DictEntry parentEntry = new DictEntry();
			parentEntry.setCode(line.getParentEntryCode());
			parentEntry.setName(line.getParentEntryName());
			parentEntry.setTenantId("default");
			dictEntry.setParent(parentEntry);
			return new Tuple(dictType, dictEntry);
		}
	}

	public DictType findById(String id, String locale) {
		return loadI18NEtnry(loadById(id, false, 0), locale);
	}

	public DictType findById(String id, String locale, boolean loadChildren, int loadLevel) {
		DictType type = loadI18NEtnry(loadById(id, loadChildren, loadLevel), locale);
		return type;
	}

	private DictType loadI18NEtnry(DictType dictType, String locale) {
		if (locale != null) {
			if (!locale.equals(dictType.getLocale())) {
				typeInternalSvc.queryLocalDictType(locale, dictType.getId()).ifPresent(i18nType -> {
					dictType.setName(i18nType.getDictTypeName());
					dictType.setLocale(i18nType.getLocale());
				});
			}
			dictType.getChildren().forEach(child -> {
				loadI18NEtnry(child, locale);
			});
		}
		return dictType;
	}

	public void downloadDictTemplate(ByteArrayOutputStream out) {
		ClassPathResource resource = new ClassPathResource(TEMPLATE_NAME);
		InputStream ins = null;
		try {
			ins = resource.getInputStream();
			toFileOperation(ins, out);
		} catch (IOException e) {
			throw CoframeErrorCode.DICT_TYPE_DOWNLOAD_FAILTURE.runtimeException();
		}
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void deleteByIds(String... ids) {
		if (ids == null) {
			return;
		}
		Set<String> oldIdSets = new HashSet<String>(Arrays.asList(ids));
		Set<String> newIdSets = new HashSet<String>();
		oldIdSets.stream().forEach(id -> {
			newIdSets.add(id);
			DictType type = loadById(id, true, 0);
			type.getChildren().forEach(t -> {
				newIdSets.add(t.getId());
			});
		});
		if (CollectionUtils.isEqualCollection(oldIdSets, newIdSets)) {
			oldIdSets.stream().forEach(id -> {
				DictType dictType = findById(id);
				DictEntry.Criteria criteria = new DictEntry.Criteria();
				criteria.setDictTypeId(dictType.getId());
				List<DictEntry> dictEntry = dictEntryService.findByDictIdForDictEntry(criteria);
				if (dictEntry.isEmpty()) {
					deleteById(id);
				} else {
					throw CoframeErrorCode.DICT_TYPE_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
				}
			});
		} else {
			throw CoframeErrorCode.DICT_TYPE_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}
	
	@Override
	public void preDelete(String id) {
	}

	public void exportExcelById(ByteArrayOutputStream out, String locale, String id, boolean loadChildren) {
		List<ExcelDictLine> list = new ArrayList<>();
		DictType dictType = loadById(id, loadChildren, 0);
		if (loadChildren) {
			List<DictType> dicts = dictType.getChildren();
			dicts.add(0, dictType);
			generateExcelLine(dicts, list, locale);
		} else {
			generateExcelLine(dictType, list, locale);
		}
		exportExcel(list, out);
	}

	private void generateExcelLine(DictType dictType, List<ExcelDictLine> list, String locale) {
		DictEntry.Criteria entryCriteria = new DictEntry.Criteria();
		if (locale != null) {
			if (!locale.equals(dictType.getLocale())) {
				dictType = findById(dictType.getId(), locale);
			}
		}
		if (dictType.getParentId() != null) {
			dictType.setParent(findById(dictType.getParentId()));
		}
		entryCriteria.setDictTypeCode(dictType.getCode());
		List<DictEntry> entrys = dictEntryService.queryDictEntyByDictCode(entryCriteria);
		if (entrys.size() == 0) {
			list.add(new Tuple(dictType, null).mapToExcelDictLine());
		} else {
			for (DictEntry dictEntry : entrys) {
				if (locale != null) {
					if (!locale.equals(dictEntry.getLocale())) {
						dictEntry = dictEntryService.findById(dictEntry.getId(), locale);
					}
				}
				if (dictEntry.getParentId() != null) {
					dictEntry.setParent(dictEntryService.findById(dictEntry.getParentId()));
				}
				list.add(new Tuple(dictType, dictEntry).mapToExcelDictLine());
			}
		}
	}

	public void exportExcelAll(ByteArrayOutputStream out, String locale) {
		List<ExcelDictLine> list = new ArrayList<>();
		DictType.Criteria criteria = new DictType.Criteria();
		criteria.setLocale(locale);
		List<DictType> dicts = dictTypeRepository.findAll(dictTypeRepository.toSpecification(criteria));
		generateExcelLine(dicts, list, locale);
		exportExcel(list, out);
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.DICT_TYPE_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
}
