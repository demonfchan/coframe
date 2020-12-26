/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 21, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractFixedPersistentModel;

import io.swagger.annotations.ApiModel;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "AUTH_RES")
@ApiModel("可授权者与资源的关联")
public class AuthRes extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = 6448580109733896288L;

	@NotBlank
	private String type;

	@NotBlank
	private String authType;

	@Column(name = "AUTH_ID")
	@NotBlank
	private String authId;

	@NotBlank
	private String resType;

	@Column(name = "RES_ID")
	@NotBlank
	private String resId;

	/**
	 * 
	 */
	public AuthRes() {
	}

	/**
	 * @param type
	 * @param authType
	 * @param authId
	 * @param resType
	 * @param resId
	 */
	public AuthRes(String type, String authType, String authId, String resType, String resId) {
		this.type = type;
		this.authType = authType;
		this.authId = authId;
		this.resType = resType;
		this.resId = resId;
	}

	/**
	 * @param type
	 * @param authType
	 * @param authId
	 * @param resType
	 * @param resId
	 */
	public AuthRes(String authType, String authId, String resType, String resId) {
		this(CoframeConstants.AUTH_RES_TYPEMAPPING, authType, authId, resType, resId);
	}

	/**
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria {
		private String type;
		private String authType;
		private String authId;
		private String resType;
		private String resId;

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
		 * @return the authType
		 */
		public String getAuthType() {
			return authType;
		}

		/**
		 * @param authType the authType to set
		 */
		public void setAuthType(String authType) {
			this.authType = authType;
		}

		/**
		 * @return the authId
		 */
		public String getAuthId() {
			return authId;
		}

		/**
		 * @param authId the authId to set
		 */
		public void setAuthId(String authId) {
			this.authId = authId;
		}

		/**
		 * @return the resType
		 */
		public String getResType() {
			return resType;
		}

		/**
		 * @param resType the resType to set
		 */
		public void setResType(String resType) {
			this.resType = resType;
		}

		/**
		 * @return the resId
		 */
		public String getResId() {
			return resId;
		}

		/**
		 * @param resId the resId to set
		 */
		public void setResId(String resId) {
			this.resId = resId;
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
	 * @return the authType
	 */
	public String getAuthType() {
		return authType;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * @return the authId
	 */
	public String getAuthId() {
		return authId;
	}

	/**
	 * @param authId the authId to set
	 */
	public void setAuthId(String authId) {
		this.authId = authId;
	}

	/**
	 * @return the resType
	 */
	public String getResType() {
		return resType;
	}

	/**
	 * @param resType the resType to set
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

	/**
	 * @return the resId
	 */
	public String getResId() {
		return resId;
	}

	/**
	 * @param resId the resId to set
	 */
	public void setResId(String resId) {
		this.resId = resId;
	}
}
