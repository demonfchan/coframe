/**
 * 
 */
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;
import static org.gocom.coframe.CoframeConstants.DATE_TIME_FORMAT;
import static org.gocom.coframe.CoframeConstants.TIME_ZONE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "OPERATION_LOG")
public class OperationLog extends AbstractPersistentModel {

	private static final long serialVersionUID = 319985077617207413L;

	public static enum Type {
		CREATE, UPDATE, DELETE, OTHER
	}

	@Enumerated(EnumType.ORDINAL)
	private Type operateType;

	private String targetModelName;

	private String targetModelId;

	private String message;

	private String operatorId;

	private String operatorName;

	private String tenantId;

	@DateTimeFormat(pattern = DATE_TIME_FORMAT)
	@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
	private Date operateDate;

	@ApiModel("OperationLog.Criteria")
	public static class Criteria implements Serializable {
		private static final long serialVersionUID = -2387748010110686497L;

		private Type operateType;

		private String operatorId;

		@DateTimeFormat(pattern = DATE_TIME_FORMAT)
		@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
		private Date dateBegin;

		@DateTimeFormat(pattern = DATE_TIME_FORMAT)
		@JsonFormat(pattern = DATE_TIME_FORMAT, timezone = TIME_ZONE)
		private Date dateEnd;

		public Type getOperateType() {
			return operateType;
		}

		public void setOperateType(Type operateType) {
			this.operateType = operateType;
		}

		public String getOperatorId() {
			return operatorId;
		}

		public void setOperatorId(String operatorId) {
			this.operatorId = operatorId;
		}

		public Date getDateBegin() {
			return dateBegin;
		}

		public void setDateBegin(Date dateBegin) {
			this.dateBegin = dateBegin;
		}

		public Date getDateEnd() {
			return dateEnd;
		}

		public void setDateEnd(Date dateEnd) {
			this.dateEnd = dateEnd;
		}
	}

	public Type getOperateType() {
		return operateType;
	}

	public void setOperateType(Type operateType) {
		this.operateType = operateType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getTargetModelName() {
		return targetModelName;
	}

	public void setTargetModelName(String targetModelName) {
		this.targetModelName = targetModelName;
	}

	public String getTargetModelId() {
		return targetModelId;
	}

	public void setTargetModelId(String targetModelId) {
		this.targetModelId = targetModelId;
	}

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
