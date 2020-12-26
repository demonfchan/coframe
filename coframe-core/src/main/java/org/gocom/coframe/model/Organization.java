/*******************************************************************************
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机构
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "ORG")
@ApiModel("机构")
public class Organization extends AbstractTreeModel<Organization> {
	private static final long serialVersionUID = 1195092739423010308L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dimension_id")
	@Valid
	@NotNull(groups = CoframeValidationGroups.Create.class)
	@ConvertGroup(from = CoframeValidationGroups.Create.class, to = CoframeValidationGroups.Association.class)
	@ConvertGroup(from = CoframeValidationGroups.Update.class, to = CoframeValidationGroups.Association.class)
	@ApiModelProperty("维度")
	private Dimension dimension;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "main_dimension_org_id")
	@ApiModelProperty("此机构映射到的主维度的机构")
	private Organization mainDimensionOrg;
	
	@ApiModelProperty("机构映射策略")
	@Enumerated(EnumType.ORDINAL)
	private OrgStrategy strategy;
	
	public static enum OrgStrategy {
		NONE,CURRENT,ALL;
	}

	@NotBlank(groups = CoframeValidationGroups.Create.class)
	@ApiModelProperty("机构类型")
	private String type;

	@ApiModelProperty("机构等级")
	private String degree;

	@ApiModelProperty("机构状态")
	private String status = CoframeConstants.STATUS_ENABLED;

	@ApiModelProperty("区域")
	private String area;

	@ApiModelProperty("地址")
	private String address;

	@ApiModelProperty("邮编")
	private String zipcode;

	@ApiModelProperty("联系人")
	private String linkMan;

	@ApiModelProperty("联系电话")
	private String linkTel;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("网站")
	private String website;

	@ApiModelProperty("负责人Id")
	private String managerId;
	
	/**
	 * 查询条件
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria extends AbstractTreeModelCriteria {
		private String dimension;
		private String type;
		private String status;
		private String area;
		private String dimensionId;
		private String dimensionCode;
		private OrgStrategy strategy;

		/**
		 * @return the dimension
		 */
		public String getDimension() {
			return dimension;
		}

		/**
		 * @param dimension the dimension to set
		 */
		public void setDimension(String dimension) {
			this.dimension = dimension;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
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
		 * @return the area
		 */
		public String getArea() {
			return area;
		}

		/**
		 * @param area the area to set
		 */
		public void setArea(String area) {
			this.area = area;
		}

		/**
		 * @return the dimensionId
		 */
		public String getDimensionId() {
			return dimensionId;
		}

		/**
		 * @param dimensionId the dimensionId to set
		 */
		public void setDimensionId(String dimensionId) {
			this.dimensionId = dimensionId;
		}

		/**
		 * @return the dimensionCode
		 */
		public String getDimensionCode() {
			return dimensionCode;
		}

		/**
		 * @param dimensionCode the dimensionCode to set
		 */
		public void setDimensionCode(String dimensionCode) {
			this.dimensionCode = dimensionCode;
		}

		/**
		 * @return the strategy
		 */
		public OrgStrategy getStrategy() {
			return strategy;
		}

		/**
		 * @param strategy the strategy to set
		 */
		public void setStrategy(OrgStrategy strategy) {
			this.strategy = strategy;
		}
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
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
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the linkMan
	 */
	public String getLinkMan() {
		return linkMan;
	}

	/**
	 * @param linkMan the linkMan to set
	 */
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	/**
	 * @return the linkTel
	 */
	public String getLinkTel() {
		return linkTel;
	}

	/**
	 * @param linkTel the linkTel to set
	 */
	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the managerId
	 */
	public String getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	/**
	 * @return the mainDimensionOrg
	 */
	public Organization getMainDimensionOrg() {
		return mainDimensionOrg;
	}

	/**
	 * @param mainDimensionOrg the mainDimensionOrg to set
	 */
	public void setMainDimensionOrg(Organization mainDimensionOrg) {
		this.mainDimensionOrg = mainDimensionOrg;
	}

	/**
	 * @return the strategy
	 */
	public OrgStrategy getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(OrgStrategy strategy) {
		this.strategy = strategy;
	}
}
