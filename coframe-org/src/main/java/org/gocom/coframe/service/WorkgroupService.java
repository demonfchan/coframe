package org.gocom.coframe.service;

import java.util.Arrays;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Workgroup;
import org.gocom.coframe.model.Workgroup.Criteria;
import org.gocom.coframe.model.WorkgroupEmp;
import org.gocom.coframe.repository.WorkgroupEmpRepository;
import org.gocom.coframe.repository.WorkgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作组操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class WorkgroupService extends AbstractTreeModelService<Workgroup, Workgroup.Criteria> {
	@Autowired
	private WorkgroupRepository repository;

	@Autowired
	private WorkgroupEmpRepository workgroupEmpRepository;

	@Autowired
	private EmployeeService employeeService;

	@Override
	protected CoframeJpaRepository<Workgroup, String> getRepository() {
		return repository;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteById(String id) {
		// 如果还有子员工，不允许删除
		Employee.Criteria empCriteria = new Employee.Criteria();
		empCriteria.setWorkGroupId(id);
		if (employeeService.countByCriteria(empCriteria) > 0) {
			throw CoframeErrorCode.WORKGROUP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
		super.deleteById(id);
	}

	/**
	 * 分页条件查询工作组
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Workgroup> pagingByCriteria(Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		return repository.findAll(repository.toSpecification(criteria), pageRequest);
	}

	/**
	 * 分页条件查询工作组下的员工
	 * 
	 * @param workGroupId
	 * @param employeeName
	 * @return
	 */
	public Page<Employee> querySubEmployees(String workGroupId, String employeeName, Pageable pageable) {
		Employee.Criteria criteria = new Employee.Criteria();
		criteria.setWorkGroupId(workGroupId);
		criteria.setName(employeeName);
		return employeeService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 往工作组添加员工
	 * 
	 * @param workgroupId
	 * @param employeeIds
	 */
	public void addWorkgroupEmployees(String workgroupId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			if (workgroupEmpRepository.count(workgroupEmpRepository.toSpecification(workgroupId, empId)) == 0) {
				workgroupEmpRepository.save(new WorkgroupEmp(workgroupId, empId));
			} else {
				CoframeErrorCode.WORKGROUP_ALREADY_HAD_THE_EMPLOYEE.runtimeException(empId);
			}
		});
	}

	/**
	 * 从工作组移除员工
	 * 
	 * @param workgroupId
	 * @param employeeIds
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removeWorkgroupEmployees(String workgroupId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			workgroupEmpRepository.delete(workgroupEmpRepository.toSpecification(workgroupId, empId));
		});
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.WORKGROUP_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
	
	public long countByCriteria(Workgroup.Criteria criteria) {
		return repository.count(repository.toSpecification(criteria));
	}
}
