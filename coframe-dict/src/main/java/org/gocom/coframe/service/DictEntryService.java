package org.gocom.coframe.service;

import static org.gocom.coframe.core.exception.CoframeErrorCode.FOUND_DUPLICATED_MODEL_WITH_SAME_CODE;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.model.DictEntry.Criteria;
import org.gocom.coframe.repository.DictEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class DictEntryService extends AbstractTreeModelService<DictEntry, DictEntry.Criteria> {
	@Autowired
	private DictEntryRepository dictEntryRepository;
	@Autowired
	private DictEntryInternationalService entryInternalSvc;

	@Override
	protected CoframeJpaRepository<DictEntry, String> getRepository() {
		return dictEntryRepository;
	}

	public List<DictEntry> queryDictEntyFromDict(DictEntry.Criteria criteria) {
		return dictEntryRepository.findAll(dictEntryRepository.toSpecification(criteria));

	}

	public List<DictEntry> querySubDictEntrys(String dictEntryId, String subDictEntryName, String subDictEntryCode) {
		DictEntry.Criteria criteria = new DictEntry.Criteria();
		criteria.setParentId(dictEntryId);
		criteria.setCode(subDictEntryCode);
		criteria.setName(subDictEntryName);
		return dictEntryRepository.findAll(dictEntryRepository.toSpecification(criteria));
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
	public Page<DictEntry> pagingByCriteria(Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		Page<DictEntry> dictEntrys = dictEntryRepository.findAll(dictEntryRepository.toSpecification(criteria), pageRequest);
		if(criteria.isLoadChildren()) {
			if(criteria.getLoadChildrenLevel() >=0) {
				dictEntrys.stream().forEach(dictEntry -> {
					loadTreeModel(dictEntry, criteria.getLoadChildrenLevel(), criteria.isLoadOthers());
				});
			}
		}
		return dictEntrys;
	}

	public Optional<DictEntry> findOne(String entryCode, String dictTypeId) {
		DictEntry.Criteria entryCriteria = new DictEntry.Criteria();
		entryCriteria.setCode(entryCode);
		entryCriteria.setDictTypeId(dictTypeId);
		entryCriteria.setUsingLikeQuery(false);
		return dictEntryRepository.findOne(dictEntryRepository.toSpecification(entryCriteria));
	}

	public List<DictEntry> queryDictEntyByDictCode(DictEntry.Criteria criteria) {
		return dictEntryRepository.findAll(dictEntryRepository.toSpecification(criteria));

	}

	public List<DictEntry> findByDictIdForDictEntry(Criteria criteria) {
		return dictEntryRepository.findAll(dictEntryRepository.toSpecification(criteria));
	}

	public DictEntry findById(String id, String locale) {
		return loadI18NEntry(loadById(id, false, 0), locale);
	}

	public DictEntry findById(String id, String locale, boolean loadChildren, int loadLevel) {
		return loadI18NEntry(loadById(id, loadChildren, loadLevel), locale);
	}

	private DictEntry loadI18NEntry(DictEntry dictEntry, String locale) {
		if(locale != null) {
			if (!locale.equals(dictEntry.getLocale())) {
				entryInternalSvc.queryLocalDictType(locale, dictEntry.getId()).ifPresent(i18nEntry -> {
					dictEntry.setName(i18nEntry.getDictEntryName());
					dictEntry.setLocale(i18nEntry.getLocale());
				});
			}
			dictEntry.getChildren().forEach(child -> {
				loadI18NEntry(child, locale);
			});
		}
		return dictEntry;
	}

	public void deleteByIds(String... ids) {
		if (ids == null) {
			return;
		}
		Set<String> idSets = new HashSet<>(Arrays.asList(ids));
		Set<String> querySets = new HashSet<>();
		idSets.forEach(id -> {
			DictEntry dictEntry = loadById(id,true,0);
			querySets.add(id);
			dictEntry.getChildren().stream().forEach(d -> {
				querySets.add(d.getId());
			});
		});
		if(CollectionUtils.isEqualCollection(idSets,querySets)){
			idSets.forEach(id -> {
				deleteById(id);
			});
		} else {
			throw CoframeErrorCode.DICT_ENTRY_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
	}
	
	@Override
	public void preUpdate(DictEntry dictEntry) {
	}

	@Override
	public void preDelete(String id) {

	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.DICT_ENTRY_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gocom.coframe.core.service.AbstractTreeModelService#preCreate(org.gocom.
	 * coframe.core.model.AbstractTreeModel)
	 */
	@Override
	public void preCreate(DictEntry dictEntry) {
		DictEntry.Criteria entryCriteria = new DictEntry.Criteria();
		entryCriteria.setCode(dictEntry.getCode());
		Optional.ofNullable(dictEntry.getDictType()).ifPresent(dictType -> {
			entryCriteria.setDictTypeId(dictType.getId());
		});
		entryCriteria.setUsingLikeQuery(false);
		dictEntryRepository.findOne(dictEntryRepository.toSpecification(entryCriteria)).ifPresent(value -> {
			throw FOUND_DUPLICATED_MODEL_WITH_SAME_CODE.runtimeException();
		});
		dictEntry.setLeaf(true);
		if (dictEntry.getParentId() == null) {
			generateRootSeq(dictEntry);
			dictEntry.setLevel(1);
		} else {
			dictEntryRepository.findById(dictEntry.getParentId()).ifPresent(parent -> {
				parent.setLeaf(false);
				getRepository().save(parent);
				generateSeq(parent, dictEntry);
				dictEntry.setLevel(parent.getLevel() + 1);
			});
		}
	}
	
	public void move(DictEntry dictEntry) {
		super.checkAndMove(dictEntry);
	}
}
