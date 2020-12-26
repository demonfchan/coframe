/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.core.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;

/**
 * TODO 此处填写 class 信息
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public abstract class CoframeJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CoframeJpaRepository<T, ID> {

	protected JpaEntityInformation<T, ?> entityInformation;
	protected EntityManager em;

	public CoframeJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityInformation = entityInformation;
		this.em = em;
	}

	public Class<T> getDomainClass() {
		return super.getDomainClass();
	}

	protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort) {
		CrudMethodMetadata metadata = getRepositoryMethodMetadata();
		TypedQuery<S> typedQuery = super.getQuery(customizedSpec(spec, metadata), domainClass, customizedSort(sort, metadata));
		return typedQuery;
	}

	protected <S extends T> Specification<S> customizedSpec(Specification<S> spec, CrudMethodMetadata metadata) {
		return spec;
	}

	protected Sort customizedSort(Sort sort, CrudMethodMetadata metadata) {
		return sort;
	}

	public int delete(Specification<T> spec) {
		return getDeleteQuery(spec, getDomainClass()).executeUpdate();
	}

	private <S extends T> Query getDeleteQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<S> delete = builder.createCriteriaDelete(domainClass);
		Root<S> root = delete.from(domainClass);
		if (spec != null) {
			Predicate predicate = spec.toPredicate(root, builder.createQuery(domainClass), builder);
			if (predicate != null) {
				delete.where(predicate);
			}
		}
		return em.createQuery(delete);
	}
}
