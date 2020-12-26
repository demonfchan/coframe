package org.gocom.coframe.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.DictEntry;
import org.gocom.coframe.model.DictEntry_;
import org.gocom.coframe.model.DictType_;
import org.springframework.data.jpa.domain.Specification;

public interface DictEntryRepository extends CoframeJpaRepository<DictEntry, String> {
	public default Specification<DictEntry> toSpecification(DictEntry.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = criteria.buildPredicates(root, builder);
			if(criteria.isListRoot()) {
				predicates.add(builder.isNull(root.get(DictEntry_.parentId)));
			}else {
				Optional.ofNullable(criteria.getParentId()).ifPresent(value -> {
					predicates.add(builder.equal(root.get(DictEntry_.parentId), value));
				});
			}
			Optional.ofNullable(criteria.getDictTypeCode()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(DictEntry_.dictType).get(DictType_.code), value));
			});
			Optional.ofNullable(criteria.getDictTypeId()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(DictEntry_.dictType).get(DictType_.id), value));
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
