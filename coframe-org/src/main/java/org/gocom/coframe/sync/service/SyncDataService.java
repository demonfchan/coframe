package org.gocom.coframe.sync.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.model.Dimension;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.OrgEmp;
import org.gocom.coframe.model.Organization;
import org.gocom.coframe.model.Organization.Criteria;
import org.gocom.coframe.repository.DimensionRepository;
import org.gocom.coframe.repository.EmployeeRepository;
import org.gocom.coframe.repository.OrgEmpRepository;
import org.gocom.coframe.repository.OrganizationRepository;
import org.gocom.coframe.sync.model.SyncEmployee;
import org.gocom.coframe.sync.model.SyncOrganization;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnProperty(prefix = "security.iam.sso", name = "enabled", havingValue = "true")
public class SyncDataService extends AbstractTreeModelService<Organization, Organization.Criteria>{

	 @Value("${coframe.sync.uri:\"\"}")
	 private String syncUri;
	 
	 private SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 
	 private RestTemplate restTemplate = new RestTemplate();
	 
	 @Autowired
	 private OrganizationRepository orgRepo;
	 
	 @Autowired
	 private DimensionRepository dimenRepo;
	 
	 @Autowired
	 private EmployeeRepository empRepo;
	 
	 @Autowired
	 private OrgEmpRepository orgEmpRepo;
	
	 
	public void syncData(String startDate, String endDate , String syncMode) {
		List<SyncOrganization> orgs = getSyncDataForOrg(startDate,endDate,syncMode);
		List<SyncEmployee> employees = getSyncDataForEmployee(startDate,endDate,syncMode);
		List<Organization> orgList = new ArrayList<Organization>();
		if(orgs.size() != 0) {
			syncOrgs(orgs,orgList);
			for(Organization org : orgList) {
				checkAndMove(org);
			}
		}
		if(employees.size() != 0) {
			syncEmployees(employees);
		}
	}
	/**
	 * 同步机构
	 * @param orgs
	 */
	@Transactional(rollbackFor = Throwable.class)
	private void syncOrgs(List<SyncOrganization> orgs, List<Organization> orgList) {
		orgs.stream().forEach(org -> {
			Dimension.Criteria dCriteria = new Dimension.Criteria();
			dCriteria.setMain(true);
			dCriteria.setUsingLikeQuery(false);
			Dimension dimension = dimenRepo.findOne(dimenRepo.toSpecification(dCriteria)).get();
			Organization existOrg = findOrgByCriteria(org.getCode());
			if (StringUtils.isBlank(org.getParentCode())) {
				saveOrgNoParent( org,  existOrg,  dimension, orgList);
			} else {
				Organization existParentOrg = findOrgByCriteria(org.getParentCode());
				if(ObjectUtils.isEmpty(existParentOrg)) {
					saveOrg(org, dimension, existOrg, orgList);
				} else {
					saveOrgHaveParent( org, existOrg, existParentOrg, orgList);
				}
			}
		});
	}
	
	private void saveOrg(SyncOrganization org, Dimension dimension, Organization existOrg, List<Organization> orgList) {
		Organization pOrganization = new Organization();
		pOrganization.setCode(org.getParentCode());
		pOrganization.setName(org.getParentCode());
		pOrganization.setTenantId("default");
		pOrganization.setDegree("0");
		pOrganization.setLevel(1);
		pOrganization.setLeaf(true);
		pOrganization.setDimension(dimension);
		generateRootSeq(pOrganization);
		pOrganization = orgRepo.save(pOrganization);
		orgList.add(pOrganization);
		if(ObjectUtils.isEmpty(existOrg)) {
			Organization organization = new Organization();
			BeanUtils.copyProperties(org, organization);
			organization.setTenantId("default");
			organization.setLeaf(false);
			organization.setLevel(pOrganization.getLevel()+1);
			generateSeq(pOrganization, organization);
			organization.setParentId(pOrganization.getParentId());
			organization.setDimension(pOrganization.getDimension());
			organization = orgRepo.save(organization);
			orgList.add(organization);
		} else {
			if(StringUtils.isBlank(existOrg.getParentId())) {
				BeanUtils.copyProperties(org, existOrg);
				existOrg.setParentId(pOrganization.getParentId());
				existOrg.setLevel(pOrganization.getLevel()+1);
				existOrg.setDimension(pOrganization.getDimension());
				orgList.add(existOrg);
			} else if(!existOrg.getCode().equals(org.getCode()) || !existOrg.getName().equals(org.getName()) || !pOrganization.getCode().equals(orgRepo.findById(existOrg.getParentId()).get().getCode())) {
				BeanUtils.copyProperties(org, existOrg);
				existOrg.setParentId(pOrganization.getParentId());
				existOrg.setDimension(pOrganization.getDimension());
				existOrg.setLevel(pOrganization.getLevel()+1);
				orgList.add(existOrg);
			}
		}
	}
	
	private void saveOrgNoParent(SyncOrganization org, Organization existOrg, Dimension dimension, List<Organization> orgList) {
		if(ObjectUtils.isEmpty(existOrg)) {
			Organization organization = new Organization();
			BeanUtils.copyProperties(org, organization);
			organization.setTenantId("default");
			organization.setLevel(1);
			organization.setLeaf(true);
			organization.setDegree("0");
			organization.setDimension(dimension);
			generateRootSeq(organization);
			Organization dBOrg = orgRepo.save(organization);
			orgList.add(dBOrg);
		} else {
			if(!existOrg.getCode().equals(org.getCode()) || !existOrg.getName().equals(org.getName())) {
				BeanUtils.copyProperties(org, existOrg);
				existOrg.setParentId(null);
				orgList.add(existOrg);
			}
		}
	}
	
	private void saveOrgHaveParent(SyncOrganization org,Organization existOrg,Organization existParentOrg, List<Organization> orgList) {
		if(ObjectUtils.isEmpty(existOrg)) {
			Organization organization = new Organization();
			organization.setTenantId("default");
			BeanUtils.copyProperties(org, organization);
			organization.setParentId(existParentOrg.getId());
			organization.setLevel(existParentOrg.getLevel()+1);
			organization.setLeaf(false);
			organization.setDegree("0");
			generateRootSeq(organization);
			organization.setDimension(existParentOrg.getDimension());
			organization = orgRepo.save(organization);
			orgList.add(organization);
		} else {
			if(StringUtils.isBlank(existOrg.getParentId())) {
				BeanUtils.copyProperties(org, existOrg);
				existOrg.setParentId(existParentOrg.getId());
				existOrg.setDimension(existParentOrg.getDimension());
				orgList.add(existOrg);
			} else if(!existOrg.getCode().equals(org.getCode()) || !existOrg.getName().equals(org.getName()) || !existParentOrg.getCode().equals(orgRepo.findById(existOrg.getParentId()).get().getCode())) {
				BeanUtils.copyProperties(org, existOrg);
				existOrg.setParentId(existParentOrg.getId());
				existOrg.setDimension(existParentOrg.getDimension());
				orgList.add(existOrg);
			}
		}
	}
	
	private Organization findOrgByCriteria(String code) {
		Organization.Criteria criteria = new Organization.Criteria();
		criteria.setCode(code);
		criteria.setUsingLikeQuery(false);
		Organization organization = orgRepo.findOne(orgRepo.toSpecification(criteria)).orElse(null);
		return organization;
	}
	
	/**
	 * 同步人员
	 * @param employees
	 */
	@Transactional(rollbackFor = Throwable.class)
	private void syncEmployees(List<SyncEmployee> employees) {
		employees.stream().forEach(employee -> {
			if(StringUtils.isBlank(employee.getOrganizationCode())) {
				saveEmployee(employee);
			} else {
				Organization existOrg = findOrgByCriteria(employee.getOrganizationCode());
				if(ObjectUtils.isEmpty(existOrg)) {
					saveEmployee(employee);
				} else {
					saveExistEmp(employee,existOrg);
				}
			}
		});
	}
	
	private void saveEmployee(SyncEmployee employee) {
		Employee.Criteria criteria = new Employee.Criteria();
		criteria.setCode(employee.getCode());
		criteria.setUsingLikeQuery(false);
		Employee existEmp = empRepo.findOne(empRepo.toSpecification(criteria)).orElse(null);
		if(ObjectUtils.isEmpty(existEmp)) {
			Employee emp = new Employee();
			BeanUtils.copyProperties(employee, emp);
			emp.setTenantId("default");
			empRepo.save(emp);
		} else {
			if(!existEmp.getCode().equals(employee.getCode()) || !existEmp.getName().equals(employee.getName())) {
				BeanUtils.copyProperties(employee, existEmp);
				empRepo.save(existEmp);
			}
		}
	}
	
	private void saveExistEmp(SyncEmployee employee,Organization existOrg) {
		Employee.Criteria criteria = new Employee.Criteria();
		criteria.setCode(employee.getCode());
		criteria.setUsingLikeQuery(false);
		Employee existEmp = empRepo.findOne(empRepo.toSpecification(criteria)).orElse(null);
		OrgEmp orgEmp = new OrgEmp();
		if(ObjectUtils.isEmpty(existEmp)) {
			Employee emp = new Employee();
			BeanUtils.copyProperties(employee, emp);
			emp.setTenantId("default");
			emp = empRepo.save(emp);
			orgEmp.setEmpId(emp.getId());
			orgEmp.setOrgId(existOrg.getId());
			orgEmpRepo.save(orgEmp);
		} else {
			if(!existEmp.getCode().equals(employee.getCode()) || !existEmp.getName().equals(employee.getName())) {
				BeanUtils.copyProperties(employee, existEmp);
				existEmp = empRepo.save(existEmp);
			}
			if(orgEmpRepo.count(orgEmpRepo.toSpecification(existOrg.getId(), existEmp.getId())) == 0) {
				orgEmp.setEmpId(existEmp.getId());
				orgEmp.setOrgId(existOrg.getId());
				orgEmpRepo.save(orgEmp);
			}
		}
	}
	
	public List<SyncOrganization> getSyncDataForOrg(String startDate, String endDate, String syncMode) {
		String eDate = StringUtils.isBlank(endDate) ? "" : endDate;
		String sDate = StringUtils.isBlank(startDate) ? "" : startDate;
		ParameterizedTypeReference<List<SyncOrganization>> responseType = new ParameterizedTypeReference<List<SyncOrganization>>() {
		};
		ResponseEntity<List<SyncOrganization>> result = restTemplate.exchange(syncUri + "/orgs", HttpMethod.GET, null,
				responseType);
		List<SyncOrganization> orgs = result.getBody();
		orgs.sort(Comparator.comparing(SyncOrganization::getParentCode, Comparator.nullsFirst(String::compareTo)));
		if (syncMode.equals("ALL")) {
			return orgs;
		} else if (syncMode.equals("CURRENT")) {
			orgs = orgs.stream().filter(org -> {
				String date = dateFormate.format(org.getLastModifiedDate());
				if (date.compareTo(eDate) < 0 && date.compareTo(sDate) > 0) {
					return true;
				} else if (date.compareTo(eDate) == 0 && date.compareTo(sDate) == 0) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
		}
		return orgs;
	}

	
	public List<SyncEmployee> getSyncDataForEmployee(String startDate, String endDate, String syncMode) {
		String eDate = StringUtils.isBlank(endDate) ? "" : endDate;
		String sDate = StringUtils.isBlank(startDate) ? "" : startDate;
		ParameterizedTypeReference<List<SyncEmployee>> responseType = new ParameterizedTypeReference<List<SyncEmployee>>() {
		};
		ResponseEntity<List<SyncEmployee>> result = restTemplate.exchange(syncUri + "/employees", HttpMethod.GET, null,
				responseType);
		List<SyncEmployee> employees = result.getBody();
		employees.sort(Comparator.comparing(SyncEmployee::getOrganizationCode, Comparator.nullsFirst(String::compareTo)));
		if (syncMode.equals("ALL")) {
			return employees;
		} else if (syncMode.equals("CURRENT")) {
			employees = employees.stream().filter(employee -> {
				String date = dateFormate.format(employee.getLastModifiedDate());
				if (date.compareTo(eDate) < 0 && date.compareTo(sDate) > 0) {
					return true;
				} else if (date.compareTo(eDate) == 0 && date.compareTo(sDate) == 0) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
		}
		return employees;
	}
	@Override
	public Page<Organization> pagingByCriteria(Criteria criteria, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected CoframeJpaRepository<Organization, String> getRepository() {
		// TODO Auto-generated method stub
		return orgRepo;
	}
}
