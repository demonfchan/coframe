package org.gocom.coframe.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.DictEntryInternational;
import org.gocom.coframe.model.DictEntryInternational.Criteria;
import org.gocom.coframe.model.DictEntryInternational_;
import org.gocom.coframe.model.DictEntry_;
import org.springframework.data.jpa.domain.Specification;

public interface DictEntryInternationalRepository extends CoframeJpaRepository<DictEntryInternational, String> {

	public default Specification<DictEntryInternational> toDictEntrySpecification(Criteria criteria,
			String dictEntryId) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			Optional.ofNullable(dictEntryId).ifPresent(value ->
					predicates.add(builder.equal(root.get(DictEntryInternational_.DICT_ENTRY).get(DictEntry_.ID), value)));
			Optional.ofNullable(criteria.getLocale()).ifPresent(value -> {
				predicates.add(builder.equal(root.get(DictEntryInternational_.locale), value));
			});
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
