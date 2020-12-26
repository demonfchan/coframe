package org.gocom.coframe.service;

import java.util.Optional;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.DictType;
import org.gocom.coframe.model.DictTypeInternational;
import org.gocom.coframe.repository.DictTypeInternationalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DictTypeInternationalService extends AbstractPersistentModelService<DictTypeInternational>{
	@Autowired
	private DictTypeInternationalRepository typeInternalRepo;
	
	@Override
	protected CoframeJpaRepository<DictTypeInternational, String> getRepository() {
		return typeInternalRepo;
	}

	@Transactional(rollbackFor = Throwable.class)
	public void save(DictType dictType, String dictTypename, String locale) {
		DictTypeInternational typeInternal = new DictTypeInternational();
		typeInternal.setDictType(dictType);
		typeInternal.setDictTypeName(dictTypename);
		typeInternal.setLocale(locale);
		typeInternalRepo.save(typeInternal);
	}

	public Optional<DictTypeInternational> queryLocalDictType(String locale, String id) {
		DictTypeInternational.Criteria criteria = new DictTypeInternational.Criteria();
		criteria.setLocale(locale);
		return typeInternalRepo.findOne(typeInternalRepo.toDictTypeSpecification(criteria,id));
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteDictTypeInternational(String locale , String dictTypeName){
		DictTypeInternational.Criteria criteria = new DictTypeInternational.Criteria();
		criteria.setLocale(locale);
		criteria.setName(dictTypeName);
		criteria.setUsingLikeQuery(false);
		typeInternalRepo.delete(typeInternalRepo.toDictTypeSpecification(criteria,null));
	}
}
