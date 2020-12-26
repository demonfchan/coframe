package org.gocom.coframe.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Organization_;
import org.gocom.coframe.model.Position;
import org.gocom.coframe.model.Position_;
import org.springframework.data.jpa.domain.Specification;

public interface PositionRepository extends CoframeJpaRepository<Position, String> {
	/**
	 * 岗位查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Position> toSpecification(Position.Criteria criteria) {
		return (root, query, builder) -> {
			final List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getType()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.type), value)));
			Optional.ofNullable(criteria.getOrgId()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.organization).get(Organization_.id), value)));
			if (criteria.isUsingLikeQuery()) {
				Optional.ofNullable(criteria.getOrgName()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.organization).get(Organization_.name), "%" + value + "%")));
				Optional.ofNullable(criteria.getOrgCode()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.organization).get(Organization_.code), "%" + value + "%")));
			} else {
				Optional.ofNullable(criteria.getOrgName()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.organization).get(Organization_.name), value)));
				Optional.ofNullable(criteria.getOrgCode()).ifPresent((value) -> predicates.add(builder.equal(root.get(Position_.organization).get(Organization_.code), value)));
			}
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
