package org.gocom.coframe.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Dimension_;
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.model.Organization_;
import org.springframework.data.jpa.domain.Specification;

public interface OrganizationRepository extends CoframeJpaRepository<Organization, String> {
	/**
	 * 机构查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Organization> toSpecification(Organization.Criteria criteria) {
		return (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			predicates.addAll(criteria.buildPredicates(root, builder));
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.type), value)));
			Optional.ofNullable(criteria.getDimension()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.dimension), value)));
			Optional.ofNullable(criteria.getStatus()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.status), value)));
			Optional.ofNullable(criteria.getDimensionId()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.dimension).get(Dimension_.id), value)));
			if (criteria.isUsingLikeQuery()) {
				Optional.ofNullable(criteria.getArea()).ifPresent((value) -> predicates.add(builder.like(root.get(Organization_.area), "%" + value + "%")));
				Optional.ofNullable(criteria.getDimensionCode()).ifPresent((value) -> predicates.add(builder.like(root.get(Organization_.dimension).get(Dimension_.code), "%" + value + "%")));
			} else {
				Optional.ofNullable(criteria.getArea()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.area), value)));
				Optional.ofNullable(criteria.getDimensionCode()).ifPresent((value) -> predicates.add(builder.equal(root.get(Organization_.dimension).get(Dimension_.code), value)));
			}
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
