/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 27, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.DATE_FORMAT;
import static org.gocom.coframe.CoframeConstants.DATE_TIME_FORMAT;
import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;
import static org.gocom.coframe.CoframeConstants.TIME_ZONE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractSimpleModel;
import org.gocom.coframe.core.model.AbstractSimpleModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "USER")
@ApiModel("用户")
public class User extends AbstractSimpleModel {
	private static final long serialVersionUID = 3229360007042379972L;

	@Transient
	private Employee employee;
	
	@Column(name = "`emp_id`")
	@JsonIgnore
	private String employeeId;

	@ApiModelProperty("密码")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 128)
	private String password;

	// 生成TOKEN时使用的salt
	@Null(groups = { CoframeValidationGroups.Create.class, CoframeValidationGroups.Update.class })
	@JsonIgnore
	private String salt; // 加盐，辅助密码加解密用

	//@Null(groups = { CoframeValidationGroups.Update.class, CoframeValidationGroups.Create.class })
	@ApiModelProperty("用户状态")
	private String status = CoframeConstants.STATUS_ENABLED;

	@ApiModelProperty("认证模式")
	private String authMode;

	@DateTimeFormat(pattern = DATE_TIME_FORMAT)
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	@ApiModelProperty("解锁时间")
	private Date unlockTime;

	@DateTimeFormat(pattern = DATE_TIME_FORMAT)
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	@ApiModelProperty("上次登陆时间")
	private Date lastLogin;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = TIME_ZONE)
	@ApiModelProperty("有效开始时间")
	private Date startDate;

	@DateTimeFormat(pattern = DATE_FORMAT)
	@JsonFormat(pattern = DATE_FORMAT, timezone = TIME_ZONE)
	@ApiModelProperty("有效结束时间")
	private Date endDate;

	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractSimpleModelCriteria {
		private String employeeId;
		private String status;
		@ApiModelProperty("绑定状态过滤，binded 或者 unBinded，或者不给为空不过滤")
		private String bindState;
		
		@ApiModelProperty("成员相关，成员所属的领域类型")
		private String ownerType;
		@ApiModelProperty("成员相关，成员所属的领域Id")
		private String ownerId;
		@ApiModelProperty("成员相关，成员状态，取值为 isMember 或 notMember。此值需 ownerType 与 ownerId 也同时赋值才意义。isMember 查询为此领域成员的用户，notMember 则查询不为此领域成员的用户，用于往某领域内添加成员时的用户过滤")
		private String memberState;

		/**
		 * @return the employeeId
		 */
		public String getEmployeeId() {
			return employeeId;
		}

		/**
		 * @param employeeId the employeeId to set
		 */
		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
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
		 * @return the ownerType
		 */
		public String getOwnerType() {
			return ownerType;
		}

		/**
		 * @param ownerType the ownerType to set
		 */
		public void setOwnerType(String ownerType) {
			this.ownerType = ownerType;
		}

		/**
		 * @return the ownerId
		 */
		public String getOwnerId() {
			return ownerId;
		}

		/**
		 * @param ownerId the ownerId to set
		 */
		public void setOwnerId(String ownerId) {
			this.ownerId = ownerId;
		}

		/**
		 * @return the memberState
		 */
		public String getMemberState() {
			return memberState;
		}

		/**
		 * @param memberState the memberState to set
		 */
		public void setMemberState(String memberState) {
			this.memberState = memberState;
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
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
	public void setEmployee(Employee employee) {
		this.employee = employee;
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
	 * @return the authMode
	 */
	public String getAuthMode() {
		return authMode;
	}

	/**
	 * @param authMode the authMode to set
	 */
	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	/**
	 * @return the unlockTime
	 */
	public Date getUnlockTime() {
		return unlockTime;
	}

	/**
	 * @param unlockTime the unlockTime to set
	 */
	public void setUnlockTime(Date unlockTime) {
		this.unlockTime = unlockTime;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}
