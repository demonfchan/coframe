/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 5, 2019
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractFixedPersistentModel;

import io.swagger.annotations.ApiModel;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "PARTY_AUTH")
@ApiModel("参与者与可授权者的关联")
public class PartyAuth extends AbstractFixedPersistentModel {
	private static final long serialVersionUID = 8984984262801135337L;

	@Column(name = "PARTY_TYPE")
	private String partyType;

	@Column(name = "PARTY_ID")
	private String partyId;

	@Column(name = "AUTH_TYPE")
	private String authType;

	@Column(name = "AUTH_ID")
	private String authId;

	@Column(name = "AUTH_OWNER_TYPE")
	private String authOwnerType;

	@Column(name = "AUTH_OWNER_ID")
	private String authOwnerId;

	/**
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class Criteria {
		private String partyType;
		private String partyId;
		private String authType;
		private String authId;
		private String authOwnerType;
		private String authOwnerId;

		/**
		 * 
		 */
		public Criteria() {
		}

		/**
		 * @param partyType
		 * @param partyId
		 * @param authType
		 * @param authId
		 */
		public Criteria(String partyType, String partyId, String authType, String authId) {
			this(partyType, partyId, authType, authId, null, null);
		}

		/**
		 * @param partyType
		 * @param partyId
		 * @param authType
		 * @param authId
		 * @param authOwnerType
		 * @param authOwnerId
		 */
		public Criteria(String partyType, String partyId, String authType, String authId, String authOwnerType, String authOwnerId) {
			this.partyType = partyType;
			this.partyId = partyId;
			this.authType = authType;
			this.authId = authId;
			this.authOwnerType = authOwnerType;
			this.authOwnerId = authOwnerId;
		}

		/**
		 * @return the partyType
		 */
		public String getPartyType() {
			return partyType;
		}

		/**
		 * @param partyType the partyType to set
		 */
		public void setPartyType(String partyType) {
			this.partyType = partyType;
		}

		/**
		 * @return the partyId
		 */
		public String getPartyId() {
			return partyId;
		}

		/**
		 * @param partyId the partyId to set
		 */
		public void setPartyId(String partyId) {
			this.partyId = partyId;
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
		 * @return the authOwnerType
		 */
		public String getAuthOwnerType() {
			return authOwnerType;
		}

		/**
		 * @param authOwnerType the authOwnerType to set
		 */
		public void setAuthOwnerType(String authOwnerType) {
			this.authOwnerType = authOwnerType;
		}

		/**
		 * @return the authOwnerId
		 */
		public String getAuthOwnerId() {
			return authOwnerId;
		}

		/**
		 * @param authOwnerId the authOwnerId to set
		 */
		public void setAuthOwnerId(String authOwnerId) {
			this.authOwnerId = authOwnerId;
		}
	}

	/**
	 * 
	 */
	public PartyAuth() {
	}

	/**
	 * @param partyType
	 * @param partyId
	 * @param authType
	 * @param authId
	 */
	public PartyAuth(String partyType, String partyId, String authType, String authId) {
		this(partyType, partyId, authType, authId, null, null);
	}

	/**
	 * @param partyType
	 * @param partyId
	 * @param authType
	 * @param authId
	 * @param authOwnerType
	 * @param authOwnerId
	 */
	public PartyAuth(String partyType, String partyId, String authType, String authId, String authOwnerType, String authOwnerId) {
		this.partyType = partyType;
		this.partyId = partyId;
		this.authType = authType;
		this.authId = authId;
		this.authOwnerType = authOwnerType;
		this.authOwnerId = authOwnerId;
	}

	/**
	 * @return the partyType
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * @param partyType the partyType to set
	 */
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
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
	 * @return the authOwnerType
	 */
	public String getAuthOwnerType() {
		return authOwnerType;
	}

	/**
	 * @param authOwnerType the authOwnerType to set
	 */
	public void setAuthOwnerType(String authOwnerType) {
		this.authOwnerType = authOwnerType;
	}

	/**
	 * @return the authOwnerId
	 */
	public String getAuthOwnerId() {
		return authOwnerId;
	}

	/**
	 * @param authOwnerId the authOwnerId to set
	 */
	public void setAuthOwnerId(String authOwnerId) {
		this.authOwnerId = authOwnerId;
	}
}
