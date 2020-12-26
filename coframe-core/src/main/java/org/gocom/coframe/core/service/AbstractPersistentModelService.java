/**
 * 
 */
package org.gocom.coframe.core.service;

import static org.gocom.coframe.core.exception.CoframeErrorCode.NOT_FOUND_MODEL_BY_ID;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_CREATE;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_DELETE;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_UPDATE;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.PRE_DELETE;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.gocom.coframe.core.support.CoframePersistentModelEvent;
import org.gocom.coframe.sdk.util.CofBeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */

public abstract class AbstractPersistentModelService<T extends AbstractPersistentModel> implements ApplicationEventPublisherAware {
	/**
	 * 获取 Repository
	 * 
	 * @return
	 */
	protected abstract CoframeJpaRepository<T, String> getRepository();

	public ApplicationEventPublisher eventPublisher;

	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.eventPublisher = publisher;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Throwable.class)
	public T create(@Nullable T model) {
		preCreate(model);
		T savedModel = getRepository().forceSave(model);
		publishEvent(new CoframePersistentModelEvent(POST_CREATE, savedModel));
		afterCreate(model, savedModel);
		return savedModel;
	}

	@Transactional(rollbackFor = Throwable.class)
	public T update(@Nullable T model) {
		return update(model, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Throwable.class)
	public T update(@Nullable T model, boolean dynamicUpdate) {
		preUpdate(model);
		// 不clone的话, 经过save, existedModel的值会和model一样
		T existedModel = (T) findById(model.getId()).clone();
		T savedModel = null;
		if (dynamicUpdate) {
			T modelToSave = findById(model.getId());/* 会直接使用Hibernate Session缓存的model, 不会再查数据库 */
			CofBeanUtils.copyNotNullProperties(model, modelToSave);
			savedModel = getRepository().save(modelToSave);
		} else {
			savedModel = getRepository().save(model);
		}
		publishEvent(new CoframePersistentModelEvent(POST_UPDATE, existedModel, savedModel));
		afterUpdate(model, savedModel);
		return savedModel;
	}

	@Transactional(rollbackFor = Throwable.class)
	public T directUpdate(@Nullable T model) {
		return directUpdate(model, true);
	}

	@Transactional(rollbackFor = Throwable.class)
	public T directUpdate(@Nullable T model, boolean dynamicUpdate) {
		if (dynamicUpdate) {
			T modelToSave = findById(model.getId());
			CofBeanUtils.copyNotNullProperties(model, modelToSave);
			model = getRepository().save(modelToSave);
		} else {
			model = getRepository().save(model);
		}
		return model;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(rollbackFor = Throwable.class)
	public void deleteById(@Nullable String id) {
		T existedModel = (T) findById(id, false);
		if (existedModel == null) {
			return;
		}

		preDelete(id);
		publishEvent(new CoframePersistentModelEvent(PRE_DELETE, existedModel));
		getRepository().deleteById(id);
		publishEvent(new CoframePersistentModelEvent(POST_DELETE, existedModel));
		afterDelete(existedModel);
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteByIds(String... ids) {
		if (ids == null) {
			return;
		}
		Arrays.asList(ids).stream().forEach(id -> deleteById(id));
	}

	public T findById(@Nullable String id) {
		return findById(id, true);
	}

	public T findById(@Nullable String id, boolean errorIfNotFound) {
		Optional<T> optional = getRepository().findById(id);
		return !errorIfNotFound ? optional.orElse(null) : optional.orElseThrow(() -> NOT_FOUND_MODEL_BY_ID.runtimeException(id, getRepository().getDomainClass().getName()));
	}

	public void errorIfNotFoundById(String id) {
		if (!getRepository().existsById(id)) {
			throw NOT_FOUND_MODEL_BY_ID.runtimeException(id, getRepository().getDomainClass().getName());
		}
	}

	/**
	 * 按条件记数
	 * 
	 * @param specification
	 * @return
	 */
	public long count(Specification<T> specification) {
		return getRepository().count(specification);
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<T> findAll() {
		return getRepository().findAll(null);
	}

	@SuppressWarnings({ "rawtypes" })
	protected void publishEvent(CoframePersistentModelEvent event) {
		eventPublisher.publishEvent(event);
	}

	/**
	 * 创建之前
	 * 
	 * @param model
	 */
	public void preCreate(T model) {
	}

	/**
	 * 创建之后
	 * 
	 * @param model
	 * @param savedModel
	 */
	public void afterCreate(T model, T savedModel) {
	}

	/**
	 * 删除之前
	 * 
	 * @param id
	 */
	public void preDelete(String id) {
	}

	/**
	 * 删除之后
	 * 
	 * @param model
	 */
	public void afterDelete(T model) {
	}

	/**
	 * 更新之前
	 * 
	 * @param model
	 */
	public void preUpdate(T model) {
	}

	/**
	 * 更新之后
	 * 
	 * @param model
	 * @param savedModel
	 */
	public void afterUpdate(T model, T savedModel) {
	}
}
