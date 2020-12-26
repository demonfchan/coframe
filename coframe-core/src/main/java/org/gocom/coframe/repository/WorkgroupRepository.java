package org.gocom.coframe.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Organization_;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.model.Workgroup_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 工作组存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface WorkgroupRepository extends CoframeJpaRepository<Workgroup, String> {
	/**
	 * 构建查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Workgroup> toSpecification(Workgroup.Criteria criteria) {
		return (root, query, builder) -> {
			final List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(Workgroup_.type), value)));
			Optional.ofNullable(criteria.getStatus()).ifPresent((value) -> predicates.add(builder.equal(root.get(Workgroup_.status), value)));
			Optional.ofNullable(criteria.getOrganizationId()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(Workgroup_.organization).get(Organization_.id), value));
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
