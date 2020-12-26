/*******************************************************************************
 * 
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.DATE_FORMAT;
import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;
import static org.gocom.coframe.CoframeConstants.TIME_ZONE;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractMainModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 员工
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "EMP")
@ApiModel("员工")
public class Employee extends AbstractMainModel {
	private static final long serialVersionUID = -1062638953784471817L;

	@Transient
	private User user;

	@Null(groups = { CoframeValidationGroups.Create.class, CoframeValidationGroups.Update.class })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_CORE_PREFIX + "POSITION_EMP_MAPPING", //
			joinColumns = @JoinColumn(name = "emp_id", referencedColumnName = "id"), //
			inverseJoinColumns = @JoinColumn(name = "position_id", referencedColumnName = "id"))
	@JsonIgnore
	private List<Position> positions;
	@Null(groups = { CoframeValidationGroups.Create.class, CoframeValidationGroups.Update.class })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_CORE_PREFIX + "ORG_EMP_MAPPING", //
			joinColumns = @JoinColumn(name = "emp_id", referencedColumnName = "id"), //
			inverseJoinColumns = @JoinColumn(name = "org_id", referencedColumnName = "id"))
	@JsonIgnore
	private List<Organization> organizations;

	@Null(groups = { CoframeValidationGroups.Create.class, CoframeValidationGroups.Update.class })
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_CORE_PREFIX + "WORKGROUP_EMP_MAPPING", //
			joinColumns = @JoinColumn(name = "emp_id", referencedColumnName = "id"), //
			inverseJoinColumns = @JoinColumn(name = "workgroup_id", referencedColumnName = "id"))
	@JsonIgnore
	private List<Workgroup> workgroups;

	@NotNull(groups = { CoframeValidationGroups.Create.class })
	@ApiModelProperty("性别，值为 MAN 或 FEMALE")
	private String gender;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT)
	@ApiModelProperty("入职日期，格式为yyyy-MM-dd")
	private Date inDate;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT)
	@ApiModelProperty("离职日期，格式为yyyy-MM-dd")
	private Date outDate;

	@NotNull(groups = { CoframeValidationGroups.Create.class })
	@ApiModelProperty("状态，值为 ENABLED 或 DISABLED")
	private String status;

	@ApiModelProperty("英文名称")
	private String realname;

	@ApiModelProperty("所在部门")
	private String party;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = TIME_ZONE)
	@ApiModelProperty("生日")
	private String birthday;

	@ApiModelProperty("身份证件类型，如身份证，护照等")
	private String cardType;

	@ApiModelProperty("证件编码，如身份证号等")
	private String cardNo;

	@ApiModelProperty("办公室电话")
	private String oTel;

	@ApiModelProperty("办公室地址")
	private String oAddress;

	@ApiModelProperty("办公室邮箱")
	private String oEmail;

	@ApiModelProperty("家庭电话")
	private String hTel;

	@ApiModelProperty("家庭地址")
	private String hAddress;

	@ApiModelProperty("家庭邮篇")
	private String hZipcode;

	@ApiModelProperty("员工职称")
	private String degree;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("个人邮箱")
	private String pEmail;

	@ApiModelProperty("手机号")
	private String mobileNo;

	@ApiModelProperty("传真号")
	private String fanNo;

	@ApiModelProperty("微博号")
	private String weibo;

	@ApiModelProperty("QQ号")
	private String qq;

	@ApiModelProperty("微信号")
	private String wechat;

	/**
	 * 员工的包装类，用于创建员工时提供足够信息
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class EmployeeInfo {
		@Valid
		@NotNull(groups = { CoframeValidationGroups.Create.class, CoframeValidationGroups.Update.class })
		private Employee employee;

		@ApiModelProperty("员工创建与更新时用，用于带上用户信息")
		private User user;

		@ApiModelProperty("更新时，如果此值设置为true, 则忽略带的 user，并解除人员与用户的绑定")
		private boolean unbindUser = false;

		@ApiModelProperty("用于员工创建时，指定父机构")
		private String organizationId;

		@ApiModelProperty("用于员工创建时，指定岗位")
		private String positionId;

		@ApiModelProperty("用于员工创建时，指定所属工作组")
		private String workgroupId;

		/**
		 * @return the user
		 */
		public User getUser() {
			return user;
		}

		/**
		 * @param user the user to set
		 */
		public void setUser(User user) {
			this.user = user;
		}

		/**
		 * @return the organizationId
		 */
		public String getOrganizationId() {
			return organizationId;
		}

		/**
		 * @param organizationId the organizationId to set
		 */
		public void setOrganizationId(String organizationId) {
			this.organizationId = organizationId;
		}

		/**
		 * @return the positionId
		 */
		public String getPositionId() {
			return positionId;
		}

		/**
		 * @param positionId the positionId to set
		 */
		public void setPositionId(String positionId) {
			this.positionId = positionId;
		}

		/**
		 * @return the workgroupId
		 */
		public String getWorkgroupId() {
			return workgroupId;
		}

		/**
		 * @param workgroupId the workgroupId to set
		 */
		public void setWorkgroupId(String workgroupId) {
			this.workgroupId = workgroupId;
		}

		/**
		 * @return the employee
		 */
		public Employee getEmployee() {
			return employee;
		}

		/**
		 * @param employee the employee to set
		 */
		@Validated
		public void setEmployee(Employee employee) {
			this.employee = employee;
		}

		/**
		 * @return the unbindUser
		 */
		public boolean isUnbindUser() {
			return unbindUser;
		}

		/**
		 * @param unbindUser the unbindUser to set
		 */
		public void setUnbindUser(boolean unbindUser) {
			this.unbindUser = unbindUser;
		}
	}

	/**
	 * 支持的查询字段
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractMainModelCriteria {
		@ApiModelProperty("性别，MAN 或 FEMALE")
		private String gender;
		@ApiModelProperty("状态，ENABLED 或 DISABLED")
		private String status;

		@ApiModelProperty("所属机构 Id")
		private String orgId;
		@ApiModelProperty("所属机构名称")
		private String orgName;
		@ApiModelProperty("所属机构编码")
		private String orgCode;

		@ApiModelProperty("所属岗位 Id")
		private String positionId;
		@ApiModelProperty("所属岗位名称")
		private String positionName;
		@ApiModelProperty("所属岗位编码")
		private String positionCode;

		@ApiModelProperty("所属工作组 Id")
		private String workGroupId;
		@ApiModelProperty("所属工作组名称")
		private String workGroupName;
		@ApiModelProperty("所属工作组编码")
		private String workGroupCode;

		@ApiModelProperty("绑定状态过滤，binded 或者 unBinded，或者不给为空不过滤")
		private String bindState;

		@ApiModelProperty("需要排除掉的所属机构Id，这样查出来的就是不在此机构下的员工. 注意此条件与 orgId, orgCode, orgName 冲突，有这三者任一，此条件将失效")
		private String excludedOrgId;

		@ApiModelProperty("需要排除掉的所属工作组Id，这样查出来的就是不在此工作组下的员工，注意此条件与 workGroupId, workGroupCode，workGroupName, 冲突，有这三者任一，此条件将失效")
		private String excludedWorkGroupId;

		@ApiModelProperty("需要排除掉的所属岗位Id，这样查出来的就是不在此岗位下的员工，注意此条件与 positionId, positionCode, positionName 冲突，有这三者任一，此条件将失效")
		private String excludedPositionId;

		@ApiModelProperty("是否同时查询关联的用户")
		private boolean includeUser = true;

		/**
		 * @return the gender
		 */
		public String getGender() {
			return gender;
		}

		/**
		 * @param gender the gender to set
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the orgId
		 */
		public String getOrgId() {
			return orgId;
		}

		/**
		 * @param orgId the orgId to set
		 */
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}

		/**
		 * @return the orgName
		 */
		public String getOrgName() {
			return orgName;
		}

		/**
		 * @param orgName the orgName to set
		 */
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		/**
		 * @return the orgCode
		 */
		public String getOrgCode() {
			return orgCode;
		}

		/**
		 * @param orgCode the orgCode to set
		 */
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}

		/**
		 * @return the positionId
		 */
		public String getPositionId() {
			return positionId;
		}

		/**
		 * @param positionId the positionId to set
		 */
		public void setPositionId(String positionId) {
			this.positionId = positionId;
		}

		/**
		 * @return the positionName
		 */
		public String getPositionName() {
			return positionName;
		}

		/**
		 * @param positionName the positionName to set
		 */
		public void setPositionName(String positionName) {
			this.positionName = positionName;
		}

		/**
		 * @return the positionCode
		 */
		public String getPositionCode() {
			return positionCode;
		}

		/**
		 * @param positionCode the positionCode to set
		 */
		public void setPositionCode(String positionCode) {
			this.positionCode = positionCode;
		}

		/**
		 * @return the workGroupId
		 */
		public String getWorkGroupId() {
			return workGroupId;
		}

		/**
		 * @param workGroupId the workGroupId to set
		 */
		public void setWorkGroupId(String workGroupId) {
			this.workGroupId = workGroupId;
		}

		/**
		 * @return the workGroupName
		 */
		public String getWorkGroupName() {
			return workGroupName;
		}

		/**
		 * @param workGroupName the workGroupName to set
		 */
		public void setWorkGroupName(String workGroupName) {
			this.workGroupName = workGroupName;
		}

		/**
		 * @return the workGroupCode
		 */
		public String getWorkGroupCode() {
			return workGroupCode;
		}

		/**
		 * @param workGroupCode the workGroupCode to set
		 */
		public void setWorkGroupCode(String workGroupCode) {
			this.workGroupCode = workGroupCode;
		}

		/**
		 * @return the bindState
		 */
		public String getBindState() {
			return bindState;
		}

		/**
		 * @param bindState the bindState to set
		 */
		public void setBindState(String bindState) {
			this.bindState = bindState;
		}

		/**
		 * @return the excludedOrgId
		 */
		public String getExcludedOrgId() {
			return excludedOrgId;
		}

		/**
		 * @param excludedOrgId the excludedOrgId to set
		 */
		public void setExcludedOrgId(String excludedOrgId) {
			this.excludedOrgId = excludedOrgId;
		}

		/**
		 * @return the excludedWorkGroupId
		 */
		public String getExcludedWorkGroupId() {
			return excludedWorkGroupId;
		}

		/**
		 * @param excludedWorkGroupId the excludedWorkGroupId to set
		 */
		public void setExcludedWorkGroupId(String excludedWorkGroupId) {
			this.excludedWorkGroupId = excludedWorkGroupId;
		}

		/**
		 * @return the excludedPositionId
		 */
		public String getExcludedPositionId() {
			return excludedPositionId;
		}

		/**
		 * @param excludedPositionId the excludedPositionId to set
		 */
		public void setExcludedPositionId(String excludedPositionId) {
			this.excludedPositionId = excludedPositionId;
		}

		/**
		 * @return the includeUser
		 */
		public boolean isIncludeUser() {
			return includeUser;
		}

		/**
		 * @param includeUser the includeUser to set
		 */
		public void setIncludeUser(boolean includeUser) {
			this.includeUser = includeUser;
		}
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the positions
	 */
	public List<Position> getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	/**
	 * @return the organizations
	 */
	public List<Organization> getOrganizations() {
		return organizations;
	}

	/**
	 * @param organizations the organizations to set
	 */
	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	/**
	 * @return the workgroups
	 */
	public List<Workgroup> getWorkgroups() {
		return workgroups;
	}

	/**
	 * @param workgroups the workgroups to set
	 */
	public void setWorkgroups(List<Workgroup> workgroups) {
		this.workgroups = workgroups;
	}

	/**
	 * @return the inDate
	 */
	public Date getInDate() {
		return inDate;
	}

	/**
	 * @param inDate the inDate to set
	 */
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	/**
	 * @return the outDate
	 */
	public Date getOutDate() {
		return outDate;
	}

	/**
	 * @param outDate the outDate to set
	 */
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return the party
	 */
	public String getParty() {
		return party;
	}

	/**
	 * @param party the party to set
	 */
	public void setParty(String party) {
		this.party = party;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * @return the oTel
	 */
	public String getoTel() {
		return oTel;
	}

	/**
	 * @param oTel the oTel to set
	 */
	public void setoTel(String oTel) {
		this.oTel = oTel;
	}

	/**
	 * @return the oAddress
	 */
	public String getoAddress() {
		return oAddress;
	}

	/**
	 * @param oAddress the oAddress to set
	 */
	public void setoAddress(String oAddress) {
		this.oAddress = oAddress;
	}

	/**
	 * @return the oEmail
	 */
	public String getoEmail() {
		return oEmail;
	}

	/**
	 * @param oEmail the oEmail to set
	 */
	public void setoEmail(String oEmail) {
		this.oEmail = oEmail;
	}

	/**
	 * @return the hTel
	 */
	public String gethTel() {
		return hTel;
	}

	/**
	 * @param hTel the hTel to set
	 */
	public void sethTel(String hTel) {
		this.hTel = hTel;
	}

	/**
	 * @return the hAddress
	 */
	public String gethAddress() {
		return hAddress;
	}

	/**
	 * @param hAddress the hAddress to set
	 */
	public void sethAddress(String hAddress) {
		this.hAddress = hAddress;
	}

	/**
	 * @return the hZipcode
	 */
	public String gethZipcode() {
		return hZipcode;
	}

	/**
	 * @param hZipcode the hZipcode to set
	 */
	public void sethZipcode(String hZipcode) {
		this.hZipcode = hZipcode;
	}

	/**
	 * @return the pEmail
	 */
	public String getpEmail() {
		return pEmail;
	}

	/**
	 * @param pEmail the pEmail to set
	 */
	public void setpEmail(String pEmail) {
		this.pEmail = pEmail;
	}

	/**
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the fanNo
	 */
	public String getFanNo() {
		return fanNo;
	}

	/**
	 * @param fanNo the fanNo to set
	 */
	public void setFanNo(String fanNo) {
		this.fanNo = fanNo;
	}

	/**
	 * @return the weibo
	 */
	public String getWeibo() {
		return weibo;
	}

	/**
	 * @param weibo the weibo to set
	 */
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/**
	 * @return the wechat
	 */
	public String getWechat() {
		return wechat;
	}

	/**
	 * @param wechat the wechat to set
	 */
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	/**
	 * @return the degree
	 */
	public String getDegree() {
		return degree;
	}

	/**
	 * @param degree the degree to set
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
