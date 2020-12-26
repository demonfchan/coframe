/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 26, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import static org.gocom.coframe.CoframeConstants.DATE_TIME_FORMAT;
import static org.gocom.coframe.CoframeConstants.TIME_ZONE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 抽像的简单实体模型，增加了名称，描述，创建时间，更新时间, 租户编码五个属性
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@MappedSuperclass
@JsonIgnoreProperties(value = { "createdAt", "updatedAt", "hibernateLazyInitializer" }, allowGetters = true)
@JsonPropertyOrder({ "id", "name", "description", "createdAt", "updatedAt" })
public abstract class AbstractSimpleModel extends AbstractFixedPersistentModel implements Serializable {
	private static final long serialVersionUID = 6728624000682076786L;

	@NotBlank(groups = { CoframeValidationGroups.Create.class })
	@Size(max = 128)
	private String name;

	@Column(name = "DESCRIPTION")
	@Size(max = 256)
	private String description;

	@Column(name = "CREATE_TIME", updatable = false)
	@DateTimeFormat(pattern = CoframeConstants.DATE_TIME_FORMAT)
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	private Date createTime;

	@Column(name = "UPDATE_TIME")
	@DateTimeFormat(pattern = DATE_TIME_FORMAT)
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	private Date updateTime;

	@Column(name = "TENANT_ID", updatable = false)
	@NotBlank(groups = { CoframeValidationGroups.Create.class })
	@Size(max = 64)
	private String tenantId;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
