/**
 * 
 */
package org.gocom.coframe.core.jpa;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.model.AbstractFixedPersistentModel;
import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.gocom.coframe.core.model.AbstractPersistentModel_;
import org.gocom.coframe.core.model.AbstractSimpleModel;
import org.gocom.coframe.core.model.AbstractSimpleModel_;
import org.gocom.coframe.core.support.CoframeIdUtil;
import org.gocom.coframe.sdk.CofContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;

/**
 * TODO 此处填写 class 信息
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public class CoframeJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends JpaRepositoryFactoryBean<T, S, ID> {
	public CoframeJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	/**
	 * 利用实体类的元数据, 创建对应的 Repository
	 */
	protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
		return new JpaRepositoryFactory(entityManager) {
			protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
				if (AbstractMainModel.class.isAssignableFrom(metadata.getDomainType())) {
					return MainModelJpaRepositoryImpl.class;
				}
				if (AbstractSimpleModel.class.isAssignableFrom(metadata.getDomainType())) {
					return SimpleModelJpaRepositoryImpl.class;
				}
				if (AbstractFixedPersistentModel.class.isAssignableFrom(metadata.getDomainType())) {
					return FixedPersistentModelJpaRepositoryImpl.class;
				}
				if (AbstractPersistentModel.class.isAssignableFrom(metadata.getDomainType())) {
					return PersistentModelJpaRepositoryImpl.class;
				}
				Class<?> repositoryInterface = metadata.getRepositoryInterface();
				if (CoframeJpaRepository.class.isAssignableFrom(repositoryInterface)) {
					return CoframeJpaRepositoryImpl.class;
				}
				return super.getRepositoryBaseClass(metadata);
			}
		};
	}

	public static class PersistentModelJpaRepositoryImpl<T extends AbstractPersistentModel> extends CoframeJpaRepositoryImpl<T, String> {
		public PersistentModelJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
			super(entityInformation, entityManager);
		}

		public boolean existsById(String id) {
			return count((root, query, builder) -> {
				return builder.equal(root.get(AbstractPersistentModel_.id), id);
			}) == 1L;
		}

		@Override
		public <S extends T> S forceSave(S model) {
			return save(model);
		}

		@Override
		public <S extends T> S save(S model) {
			if (entityInformation.isNew(model)) {
				model.setId(CoframeIdUtil.generateId(model.getClass()));
			}
			return super.save(model);
		}
	}

	public static class FixedPersistentModelJpaRepositoryImpl<T extends AbstractFixedPersistentModel> extends PersistentModelJpaRepositoryImpl<T> {
		public FixedPersistentModelJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
			super(entityInformation, entityManager);
		}

		@Override
		public <S extends T> S forceSave(S model) {
			return save(model, true);
		}

		public <S extends T> S save(S model, boolean ignoreFixed) {
			if (!ignoreFixed && model.isFixed()) {
				throw CoframeErrorCode.FIXED_MODEL_CAN_NOT_UPDATE.runtimeException();
			}
			return super.save(model);
		}

		public void deleteById(String id) {
			findById(id).ifPresent(model -> {
				if (model.isFixed()) {
					throw CoframeErrorCode.FIXED_MODEL_CAN_NOT_DELETE.runtimeException();
				}
			});
			super.deleteById(id);
		}
	}

	public static class SimpleModelJpaRepositoryImpl<T extends AbstractSimpleModel> extends FixedPersistentModelJpaRepositoryImpl<T> {
		public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, AbstractSimpleModel_.createTime.getName());

		public SimpleModelJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
			super(entityInformation, entityManager);
		}

		public Sort customizedSort(@Nullable Sort sort, @Nullable CrudMethodMetadata metadata) {
			if (sort == null || sort == Sort.unsorted()) {
				sort = DEFAULT_SORT;
			}
			return sort;
		}

		public <S extends T> S save(S model) {
			return save(model, false);
		}

		public <S extends T> S save(S model, boolean ignoreFixed) {
			if (entityInformation.isNew(model)) {
				Date date = new Date();
				model.setCreateTime(date);
				model.setUpdateTime(date);
				if (model.getTenantId() == null) {
					model.setTenantId((String) CofContext.getContext().get(CoframeConstants.CONTEXT_TENANT_ID));
				}
			} else {
				Date date = new Date();
				model.setUpdateTime(date);
			}
			return super.save(model, ignoreFixed);
		}
	}

	public static class MainModelJpaRepositoryImpl<T extends AbstractMainModel> extends SimpleModelJpaRepositoryImpl<T> {
		public MainModelJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
			super(entityInformation, entityManager);
		}

		protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
			spec = customizedSpec(spec, getRepositoryMethodMetadata());
			return super.getCountQuery(spec, domainClass);
		}

		public Optional<T> findById(String id) {
			return super.findById(id);
		}

		public boolean existsById(String id) {
			return count((root, query, builder) -> {
				return builder.equal(root.get(AbstractPersistentModel_.id), id);
			}) == 1L;
		}

		public T getOne(String id) {
			return findById(id).orElseThrow(() -> new EntityNotFoundException(CoframeErrorCode.NOT_FOUND_MODEL_BY_ID.getMessage(getDomainClass().getName(), id)));
		}
	}
}
