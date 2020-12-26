package org.gocom.coframe.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Employee_;
import org.gocom.coframe.model.OrgEmp;
import org.gocom.coframe.model.OrgEmp_;
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.model.Organization_;
import org.gocom.coframe.model.Position;
import org.gocom.coframe.model.PositionEmp;
import org.gocom.coframe.model.PositionEmp_;
import org.gocom.coframe.model.Position_;
import org.gocom.coframe.model.User;
import org.gocom.coframe.model.User_;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.model.WorkgroupEmp;
import org.gocom.coframe.model.WorkgroupEmp_;
import org.gocom.coframe.model.Workgroup_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 
 * 员工存储实体操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface EmployeeRepository extends CoframeJpaRepository<Employee, String> {
	/**
	 * 构建员工查询条件
	 * 
	 * @param criteria
	 * @return
	 */
	public default Specification<Employee> toSpecification(Employee.Criteria criteria) {
		return (root, query, builder) -> {
			// 员工基本属性
			final List<Predicate> predicates = criteria.buildPredicates(root, builder);
			Optional.ofNullable(criteria.getGender()).ifPresent((value) -> predicates.add(builder.equal(root.get(Employee_.gender), value)));

			// 根据与用户的绑定状态过滤
			Optional.ofNullable(criteria.getBindState()).ifPresent((value) -> {
				Subquery<String> subQuery = query.subquery(String.class);
				Root<User> userRoot = subQuery.from(User.class);
				subQuery.select(userRoot.get(User_.employeeId)).where(builder.isNotNull(userRoot.get(User_.employeeId)));
				// 只查询已绑定用户的人员
				if (CoframeConstants.BINDED_ONLY.equals(value)) {
					predicates.add(builder.in(root.get(Employee_.id)).value(subQuery));
				} else
				// 只查询未绑定用户的人员
				if (CoframeConstants.UNBINDED_ONLY.equals(value)) {
					predicates.add(builder.not(builder.in(root.get(Employee_.id)).value(subQuery)));
				}
			});

			// 关联机构查询
			if (isNotBlank(criteria.getOrgId()) || isNotBlank(criteria.getOrgCode()) || isNotBlank(criteria.getOrgName())) {
				ListJoin<Employee, Organization> orgJoin = root.join(root.getModel().getList("organizations", Organization.class), JoinType.LEFT);
				Optional.ofNullable(criteria.getOrgId()).ifPresent((value) -> predicates.add(builder.equal(orgJoin.get(Organization_.id), value)));
				if (criteria.isUsingLikeQuery()) {
					Optional.ofNullable(criteria.getOrgName()).ifPresent((value) -> predicates.add(builder.like(orgJoin.get(Organization_.name), "%" + value + "%")));
					Optional.ofNullable(criteria.getOrgCode()).ifPresent((value) -> predicates.add(builder.like(orgJoin.get(Organization_.code), "%" + value + "%")));
				} else {
					Optional.ofNullable(criteria.getOrgName()).ifPresent((value) -> predicates.add(builder.equal(orgJoin.get(Organization_.name), value)));
					Optional.ofNullable(criteria.getOrgCode()).ifPresent((value) -> predicates.add(builder.equal(orgJoin.get(Organization_.code), value)));
				}
			} else if (isNotBlank(criteria.getExcludedOrgId())) { // orgId,orgCode,orgName 与 excludeOrgId 只能有一边生效
				// 用子查询来做排除
				Subquery<String> subQuery = query.subquery(String.class);
				Root<OrgEmp> orgEmp = subQuery.from(OrgEmp.class);
				subQuery.select(orgEmp.get(OrgEmp_.empId)).where(builder.equal(orgEmp.get(OrgEmp_.orgId), criteria.getExcludedOrgId()));
				predicates.add(builder.not(builder.in(root.get(Employee_.id)).value(subQuery)));
			}

			// 关联岗位查询
			if (isNotBlank(criteria.getPositionId()) || isNotBlank(criteria.getPositionCode()) || isNotBlank(criteria.getPositionName())) {
				ListJoin<Employee, Position> positionJoin = root.join(root.getModel().getList("positions", Position.class), JoinType.LEFT);
				Optional.ofNullable(criteria.getPositionId()).ifPresent((value) -> predicates.add(builder.equal(positionJoin.get(Position_.id), value)));
				Optional.ofNullable(criteria.getExcludedPositionId()).ifPresent((value) -> predicates.add(builder.not(builder.equal(positionJoin.get(Position_.id), value))));
				if (criteria.isUsingLikeQuery()) {
					Optional.ofNullable(criteria.getPositionName()).ifPresent((value) -> predicates.add(builder.like(positionJoin.get(Position_.name), "%" + value + "%")));
					Optional.ofNullable(criteria.getPositionCode()).ifPresent((value) -> predicates.add(builder.like(positionJoin.get(Position_.code), "%" + value + "%")));
				} else {
					Optional.ofNullable(criteria.getPositionName()).ifPresent((value) -> predicates.add(builder.equal(positionJoin.get(Position_.name), value)));
					Optional.ofNullable(criteria.getPositionCode()).ifPresent((value) -> predicates.add(builder.equal(positionJoin.get(Position_.code), value)));
				}
			} else if (isNotBlank(criteria.getExcludedPositionId())) {
				// 用子查询来做排除
				Subquery<String> subQuery = query.subquery(String.class);
				Root<PositionEmp> positionEmp = subQuery.from(PositionEmp.class);
				subQuery.select(positionEmp.get(PositionEmp_.empId)).where(builder.equal(positionEmp.get(PositionEmp_.positionId), criteria.getExcludedPositionId()));
				predicates.add(builder.not(builder.in(root.get(Employee_.id)).value(subQuery)));
			}

			// 关联工作组查询
			if (isNotBlank(criteria.getWorkGroupId()) || isNotBlank(criteria.getWorkGroupCode()) || isNotBlank(criteria.getWorkGroupName())) {
				ListJoin<Employee, Workgroup> workGroupJoin = root.join(root.getModel().getList("workgroups", Workgroup.class), JoinType.LEFT);
				Optional.ofNullable(criteria.getWorkGroupId()).ifPresent((value) -> predicates.add(builder.equal(workGroupJoin.get(Workgroup_.id), value)));
				Optional.ofNullable(criteria.getExcludedWorkGroupId()).ifPresent((value) -> predicates.add(builder.not(builder.equal(workGroupJoin.get(Workgroup_.id), value))));
				// 模糊查询
				if (criteria.isUsingLikeQuery()) {
					Optional.ofNullable(criteria.getWorkGroupName()).ifPresent((value) -> predicates.add(builder.like(workGroupJoin.get(Workgroup_.name), "%" + value + "%")));
					Optional.ofNullable(criteria.getWorkGroupCode()).ifPresent((value) -> predicates.add(builder.like(workGroupJoin.get(Workgroup_.code), "%" + value + "%")));
				} else {
					Optional.ofNullable(criteria.getWorkGroupName()).ifPresent((value) -> predicates.add(builder.equal(workGroupJoin.get(Workgroup_.name), value)));
					Optional.ofNullable(criteria.getWorkGroupCode()).ifPresent((value) -> predicates.add(builder.equal(workGroupJoin.get(Workgroup_.code), value)));
				}
			} else if (isNotBlank(criteria.getExcludedWorkGroupId())) {
				// 用子查询来做排除
				Subquery<String> subQuery = query.subquery(String.class);
				Root<WorkgroupEmp> workgroupEmp = subQuery.from(WorkgroupEmp.class);
				subQuery.select(workgroupEmp.get(WorkgroupEmp_.empId)).where(builder.equal(workgroupEmp.get(WorkgroupEmp_.workgroupId), criteria.getExcludedWorkGroupId()));
				predicates.add(builder.not(builder.in(root.get(Employee_.id)).value(subQuery)));
			}

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
