package org.gocom.coframe.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.DictType;
import org.gocom.coframe.model.DictType.Criteria;
import org.gocom.coframe.model.DictType_;
import org.springframework.data.jpa.domain.Specification;

public interface DictTypeRepository extends CoframeJpaRepository<DictType, String> {
	public default Specification<DictType> toSpecification(Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getLocale()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(DictType_.locale), value));
			});
			if (criteria.isListRoot()) {
				predicates.add(builder.isNull(root.get(DictType_.parentId)));
			} else {
				Optional.ofNullable(criteria.getParentId()).ifPresent(value -> {
					predicates.add(builder.equal(root.get(DictType_.parentId), value));
				});
			}
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
