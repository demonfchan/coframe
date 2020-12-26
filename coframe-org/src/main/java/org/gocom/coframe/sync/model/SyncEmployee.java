package org.gocom.coframe.sync.model;

import static org.gocom.coframe.CoframeConstants.DATE_TIME_FORMAT;
import static org.gocom.coframe.CoframeConstants.TIME_ZONE;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SyncEmployee {

	    private String name;
	    
	    private String code; // 机构编码
	    
	    private String tenantCode;
	    
	    private String organizationCode;
	    
	    private boolean deleted = false;
	    
	    @JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	    private Date lastModifiedDate = new Date();

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
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the tenantCode
		 */
		public String getTenantCode() {
			return tenantCode;
		}

		/**
		 * @param tenantCode the tenantCode to set
		 */
		public void setTenantCode(String tenantCode) {
			this.tenantCode = tenantCode;
		}

		/**
		 * @return the deleted
		 */
		public boolean isDeleted() {
			return deleted;
		}

		/**
		 * @param deleted the deleted to set
		 */
		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}

		/**
		 * @return the lastModifiedDate
		 */
		public Date getLastModifiedDate() {
			return lastModifiedDate;
		}

		/**
		 * @param lastModifiedDate the lastModifiedDate to set
		 */
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		/**
		 * @return the organizationCode
		 */
		public String getOrganizationCode() {
			return organizationCode;
		}

		/**
		 * @param organizationCode the organizationCode to set
		 */
		public void setOrganizationCode(String organizationCode) {
			this.organizationCode = organizationCode;
		}
}
