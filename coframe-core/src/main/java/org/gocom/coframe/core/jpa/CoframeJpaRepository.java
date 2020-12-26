/**
 * 
 */
package org.gocom.coframe.core.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.lang.Nullable;

/**
 * 实体操作接口
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@NoRepositoryBean
public interface CoframeJpaRepository<T, ID> extends Repository<T, ID> {
	/**
	 * 获取实体的 class
	 * 
	 * @return
	 */
	public Class<T> getDomainClass();

	/**
	 * 根据条件，查一个结果（结果可能为空）
	 * 
	 * @param spec
	 * @return
	 */
	public Optional<T> findOne(@Nullable Specification<T> spec);

	/**
	 * 根据条件，查询所有
	 * 
	 * @param spec
	 * @return
	 */
	public List<T> findAll(@Nullable Specification<T> spec);

	/**
	 * 根据条件，分页查询
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);

	/**
	 * 根据条件查询所有，并按指定的方法排序
	 * 
	 * @param spec
	 * @param sort
	 * @return
	 */
	public List<T> findAll(@Nullable Specification<T> spec, Sort sort);

	/**
	 * 查询满足条件的实体个数
	 * 
	 * @param spec
	 * @return
	 */
	public long count(@Nullable Specification<T> spec);

	/**
	 * 给定 id，查询实体是否存在
	 * @param id
	 * @return
	 */
	public boolean existsById(ID id);

	/**
	 * 给定 id 查询实体（可能为空）
	 * @param id
	 * @return
	 */
	public Optional<T> findById(ID id);
	
	/**
	 * 强制保存实体
	 * @param model
	 * @return
	 */
	public <S extends T> S forceSave(S model);

	/**
	 * 保存实体
	 * @param entity
	 * @return
	 */
	public <S extends T> S save(S entity);

	/**
	 * 根据 id 删除实体
	 * @param id
	 */
	public void deleteById(ID id);

	/**
	 * 批量保存实体
	 * @param entities
	 * @return
	 */
	public <S extends T> List<S> saveAll(Iterable<S> entities);

	/**
	 * 批量删除实体
	 * @param entities
	 */
	public void deleteAll(Iterable<? extends T> entities);

	/**
	 * 根据给定条件删除实体
	 * @param spec
	 * @return
	 */
	public int delete(@Nullable Specification<T> spec);
}
