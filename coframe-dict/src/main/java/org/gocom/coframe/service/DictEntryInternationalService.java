package org.gocom.coframe.service;

import java.util.Optional;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.model.DictEntryInternational;
import org.gocom.coframe.repository.DictEntryInternationalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictEntryInternationalService extends AbstractPersistentModelService<DictEntryInternational>{
	@Autowired
	private DictEntryInternationalRepository entryInternalRepo;
	
	@Override
	protected CoframeJpaRepository<DictEntryInternational, String> getRepository() {
		return entryInternalRepo;
	}

	@Transactional(rollbackFor = Throwable.class)
	public void save(DictEntry dictEntry, String dictEntryName, String locale) {
		DictEntryInternational entryInternal = new DictEntryInternational();
		entryInternal.setDictEntry(dictEntry);
		entryInternal.setDictEntryName(dictEntryName);
		entryInternal.setLocale(locale);
		entryInternalRepo.save(entryInternal);
	}

	public Optional<DictEntryInternational> queryLocalDictType(String locale, String id) {
		DictEntryInternational.Criteria criteria = new DictEntryInternational.Criteria();
		criteria.setLocale(locale);
		return entryInternalRepo.findOne(entryInternalRepo.toDictEntrySpecification(criteria, id));
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteDictEntryInternational(String locale , String dictEntryName){
		DictEntryInternational.Criteria criteria = new DictEntryInternational.Criteria();
		criteria.setLocale(locale);
		criteria.setName(dictEntryName);
		criteria.setUsingLikeQuery(false);
		entryInternalRepo.delete(entryInternalRepo.toDictEntrySpecification(criteria,null));
	}

}
