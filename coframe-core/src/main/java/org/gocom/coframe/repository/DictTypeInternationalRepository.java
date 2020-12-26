package org.gocom.coframe.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.DictTypeInternational;
import org.gocom.coframe.model.DictTypeInternational.Criteria;
import org.gocom.coframe.model.DictTypeInternational_;
import org.gocom.coframe.model.DictType_;
import org.springframework.data.jpa.domain.Specification;

public interface DictTypeInternationalRepository extends CoframeJpaRepository<DictTypeInternational, String> {

	public default Specification<DictTypeInternational> toDictTypeSpecification(Criteria criteria, String dictTypeId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			Optional.ofNullable(dictTypeId).ifPresent(value ->
					predicates.add(builder.equal(root.get(DictTypeInternational_.DICT_TYPE).get(DictType_.ID), value)));
			Optional.ofNullable(criteria.getLocale()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(DictTypeInternational_.locale), value));
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
