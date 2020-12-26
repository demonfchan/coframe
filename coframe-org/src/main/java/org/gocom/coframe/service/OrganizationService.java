package org.gocom.coframe.service;

import java.util.Arrays;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.OrgEmp;
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.model.Organization.Criteria;
import org.gocom.coframe.model.Position;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.repository.OrgEmpRepository;
import org.gocom.coframe.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class OrganizationService extends AbstractTreeModelService<Organization, Organization.Criteria> {
	@Autowired
	private OrganizationRepository repository;

	@Autowired
	private OrgEmpRepository orgEmpRepository;

	@Autowired
	private PositionService positionService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private WorkgroupService workgroupService;

	@Override
	protected CoframeJpaRepository<Organization, String> getRepository() {
		return repository;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteById(String id) {
		// 如果机构下面还有子岗位，不允许删除
		Position.Criteria positionCriteria = new Position.Criteria();
		positionCriteria.setOrgId(id);
		if (positionService.countByCriteria(positionCriteria) > 0) {
			throw CoframeErrorCode.ORG_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
		//如果机构下面还有工作组，不允许删除
		Workgroup.Criteria criteria = new Workgroup.Criteria();
		criteria.setOrganizationId(id);
		if (workgroupService.countByCriteria(criteria) > 0) {
			throw CoframeErrorCode.ORG_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
		// 如果机构下面还有子员工，不允许删除
		Employee.Criteria empCriteria = new Employee.Criteria();
		empCriteria.setOrgId(id);
		if (employeeService.countByCriteria(empCriteria) > 0) {
			throw CoframeErrorCode.ORG_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
		// 删除机构
		super.deleteById(id);
	}

	/**
	 * 按条件查询机构
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public Page<Organization> pagingByCriteria(Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		return repository.findAll(repository.toSpecification(criteria), pageRequest);
	}

	/**
	 * 按条件计数机构
	 * 
	 * @param criteria
	 * @return
	 */
	public long countByCriteria(Criteria criteria) {
		return repository.count(repository.toSpecification(criteria));
	}

	/**
	 * 条件查询机构下的员工
	 * 
	 * @param organizationId
	 * @param employeeName
	 * @param pageRequest
	 * @return
	 */
	public Page<Employee> querySubEmployees(String organizationId, String employeeName, Pageable pageable) {
		Employee.Criteria criteria = new Employee.Criteria();
		criteria.setName(employeeName);
		criteria.setOrgId(organizationId);
		return employeeService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 条件查询机构下的工作组
	 * 
	 * @param organizationId
	 * @param groupName
	 * @param pageRequest
	 * @return
	 */
	public Page<Workgroup> querySubWorkgroups(String organizationId, String groupName, Pageable pageable) {
		Workgroup.Criteria criteria = new Workgroup.Criteria();
		criteria.setName(groupName);
		criteria.setOrganizationId(organizationId);
		return workgroupService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 条件查询机构下的岗位
	 * 
	 * @param organizationId
	 * @param subPositionName
	 * @param subPositionCode
	 * @return
	 */
	public Page<Position> querySubPositions(String organizationId, String subPositionName, String subPositionCode, Pageable pageable) {
		Position.Criteria criteria = new Position.Criteria();
		criteria.setOrgId(organizationId);
		criteria.setCode(subPositionCode);
		criteria.setName(subPositionName);
		return positionService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 往机构中批量添加员工
	 * 
	 * @param organizationId
	 * @param employeeIds
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addOrgEmployees(String organizationId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			if (orgEmpRepository.count(orgEmpRepository.toSpecification(organizationId, empId)) == 0) {
				orgEmpRepository.save(new OrgEmp(organizationId, empId));
			} else {
				CoframeErrorCode.ORG_ALREADY_HAD_THE_EMPLOYEE.runtimeException(empId);
			}
		});
	}

	/**
	 * 从机构中批量移除员工
	 * 
	 * @param organizationId
	 * @param employeeIds
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeOrgEmployees(String organizationId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			orgEmpRepository.delete(orgEmpRepository.toSpecification(organizationId, empId));
		});
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.ORG_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
}
