package org.gocom.coframe.service;

import java.util.Optional;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.service.AbstractMainModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Employee.Criteria;
import org.gocom.coframe.model.Employee.EmployeeInfo;
import org.gocom.coframe.model.OrgEmp;
import org.gocom.coframe.model.PositionEmp;
import org.gocom.coframe.model.User;
import org.gocom.coframe.model.WorkgroupEmp;
import org.gocom.coframe.repository.EmployeeRepository;
import org.gocom.coframe.repository.OrgEmpRepository;
import org.gocom.coframe.repository.PositionEmpRepository;
import org.gocom.coframe.repository.WorkgroupEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工操作
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Service
public class EmployeeService extends AbstractMainModelService<Employee> {
	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private OrgEmpRepository orgEmpRepository;

	@Autowired
	private PositionEmpRepository positionEmpRepository;

	@Autowired
	private WorkgroupEmpRepository workgroupEmpRepository;

	@Autowired
	private UserService userService;

	@Override
	protected EmployeeRepository getRepository() {
		return repository;
	}

	@Value("${coframe.flag.employee-user-cascade-delete:false}")
	private boolean employeeUserCascadeDelete = false; // 员工与用户是否级连删除

	/**
	 * 创建员工
	 * 
	 * @param model
	 * @return
	 */
	@Transactional(rollbackFor = Throwable.class)
	public Employee create(EmployeeInfo model) {
		Employee employee = super.create(model.getEmployee());
		model.setEmployee(employee);
		handleEmployeeUser(model);

		// 如果带了机构，同步创建关联
		Optional.ofNullable(model.getOrganizationId()).ifPresent(orgId -> orgEmpRepository.save(new OrgEmp(orgId, employee.getId())));
		// 如果带了岗位，同步创建关联
		Optional.ofNullable(model.getPositionId()).ifPresent(positionId -> positionEmpRepository.save(new PositionEmp(positionId, employee.getId())));
		// 如果带了工作组，同步创建关联
		Optional.ofNullable(model.getWorkgroupId()).ifPresent(workgroupId -> workgroupEmpRepository.save(new WorkgroupEmp(workgroupId, employee.getId())));
		return employee;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Employee update(EmployeeInfo model) {
		// 如果带了用户，绑定用户
		handleEmployeeUser(model);
		// 同步修改用户状态
		if (model.getUser() != null) {
			String status = model.getUser().getStatus();
			if (CoframeConstants.STATUS_ENABLED.equals(status)) {
				userService.enableUser(model.getUser().getId());
			} else if (CoframeConstants.STATUS_DISABLED.equals(status)) {
				userService.disableUser(model.getUser().getId());
			}
		}
		return update(model.getEmployee(), false);
	}
	
	/**
	 * 覆写父类方法，默认带上用户
	 */
	@Override
	public Employee findById(@Nullable String id) {
		Employee emp = super.findById(id, true);
		if (emp != null) {
			emp.setUser(userService.findByEmployeeId(emp.getId()));
		}
		return emp;
	}

	/**
	 * 将员工与用户绑定
	 * 
	 * @param employee
	 */
	private void handleEmployeeUser(EmployeeInfo employeeInfo) {
		// 解除人员与用户的关联
		User boundUser = userService.findByEmployeeId(employeeInfo.getEmployee().getId());
		if (employeeInfo.isUnbindUser()) {
			if (boundUser != null) {
				boundUser.setEmployeeId(null);
				userService.update(boundUser);
			}
			return;
		}

		// 处理用户的绑定
		User user = employeeInfo.getUser();
		if (user != null) {
			// 创建新的用户，并绑定
			if (user.getId() == null) {
				user.setEmployeeId(employeeInfo.getEmployee().getId());
				userService.create(user);
			} else { // 绑至已有用户
				// 绑定至用户
				User toBindUser = userService.findById(employeeInfo.getUser().getId());
				// 判断带的是不是同一个，如果是同一个，不往下处理
				if (boundUser != null && boundUser.getId().equals(toBindUser.getId())) {
					return;
				}
				// 判断一下待绑定的用户，是否已经绑过人员了
				if (toBindUser.getEmployee() != null) {
					throw CoframeErrorCode.USER_ALREADY_BIND_EMPLOYEE.runtimeException();
				}
				// 如果不是同一个，替换
				toBindUser.setEmployeeId(employeeInfo.getEmployee().getId());
				userService.update(toBindUser);
				// 解除原有绑定
				if (boundUser != null) {
					boundUser.setEmployeeId(null);
					userService.update(boundUser);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteById(String id) {
		super.deleteById(id);
		// 解除与用户的绑定
		User user = userService.findByEmployeeId(id);
		if (user != null) {
			if (employeeUserCascadeDelete) { // 员工与用户
				userService.deleteById(user.getId());
			} else {
				user.setEmployeeId(null);
				userService.update(user);
			}
		}
		// 解除与机构的关联
		orgEmpRepository.delete(orgEmpRepository.toSpecification(null, id));
		// 解除与岗位的关联
		positionEmpRepository.delete(positionEmpRepository.toSpecification(null, id));
		// 解除与工作组的关联
		workgroupEmpRepository.delete(workgroupEmpRepository.toSpecification(null, id));
	}

	/**
	 * 按条件查询员工
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public Page<Employee> pagingByCriteria(Criteria criteria, Pageable pageable) {
		Page<Employee> employees = repository.findAll(repository.toSpecification(criteria), pageable);
		if (criteria.isIncludeUser() && employees != null) {
			employees.forEach(emp -> {
				emp.setUser(userService.findByEmployeeId(emp.getId()));
			});
		}
		return employees;
	}

	/**
	 * 按条件计数员工
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public long countByCriteria(Criteria criteria) {
		return repository.count(repository.toSpecification(criteria));
	}
}
