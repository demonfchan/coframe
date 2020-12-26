package org.gocom.coframe.service;

import java.util.Arrays;

import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Position;
import org.gocom.coframe.model.PositionEmp;
import org.gocom.coframe.repository.PositionEmpRepository;
import org.gocom.coframe.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PositionService extends AbstractTreeModelService<Position, Position.Criteria> {
	@Autowired
	PositionRepository repository;

	@Autowired
	PositionEmpRepository positionEmpRepository;

	@Autowired
	EmployeeService employeeService;

	@Override
	protected CoframeJpaRepository<Position, String> getRepository() {
		return repository;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteById(String id) {
		// 如果还有子员工，不允许删除
		Employee.Criteria empCriteria = new Employee.Criteria();
		empCriteria.setPositionId(id);
		if (employeeService.countByCriteria(empCriteria) > 0) {
			throw CoframeErrorCode.POSITION_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN.runtimeException();
		}
		super.deleteById(id);
	}

	/**
	 * 按条件计数
	 * 
	 * @param criteria
	 * @return
	 */
	public long countByCriteria(Position.Criteria criteria) {
		return repository.count(repository.toSpecification(criteria));
	}

	/**
	 * 按条件查询
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<Position> pagingByCriteria(Position.Criteria criteria, Pageable pageable) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Direction.ASC, "sortNo"));
		return repository.findAll(repository.toSpecification(criteria), pageRequest);
	}

	/**
	 * 按条件查询此岗位的员工
	 * 
	 * @param positionId
	 * @param employeeName
	 * @param pageable
	 * @return
	 */
	public Page<Employee> querySubEmployees(String positionId, String employeeName, Pageable pageable) {
		Employee.Criteria criteria = new Employee.Criteria();
		criteria.setPositionId(positionId);
		criteria.setName(employeeName);
		return employeeService.pagingByCriteria(criteria, pageable);
	}

	/**
	 * 往岗位添加员工
	 * 
	 * @param positionId
	 * @param employeeIds
	 */
	public void addPositionEmployees(String positionId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			if (positionEmpRepository.count(positionEmpRepository.toSpecification(positionId, empId)) == 0) {
				positionEmpRepository.save(new PositionEmp(positionId, empId));
			} else {
				CoframeErrorCode.POSITION_ALREADY_HAD_THE_EMPLOYEE.runtimeException(empId);
			}
		});
	}

	/**
	 * 从岗位移除员工
	 * 
	 * @param positionId
	 * @param employeeIds
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void removePositionEmployees(String positionId, String... employeeIds) {
		Arrays.asList(employeeIds).forEach(empId -> {
			positionEmpRepository.delete(positionEmpRepository.toSpecification(positionId, empId));
		});
	}

	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		return CoframeErrorCode.POSITION_DELETE_NOT_ALLOWED_BY_HAS_CHILDREN;
	}
}
