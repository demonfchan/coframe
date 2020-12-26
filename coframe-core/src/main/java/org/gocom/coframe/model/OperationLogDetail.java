/**
 * 
 */
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractPersistentModel;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "OPERATION_LOG_DETAIL")
public class OperationLogDetail extends AbstractPersistentModel {
	private static final long serialVersionUID = -2912586231513318394L;

	@Lob
	private String oldDataJson;

	@Lob
	private String newDataJson;

	public String getOldDataJson() {
		return oldDataJson;
	}

	public void setOldDataJson(String oldDataJson) {
		this.oldDataJson = oldDataJson;
	}

	public String getNewDataJson() {
		return newDataJson;
	}

	public void setNewDataJson(String newDataJson) {
		this.newDataJson = newDataJson;
	}
}
