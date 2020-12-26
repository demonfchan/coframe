/**
 * 
 */
package org.gocom.coframe.core.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.gocom.coframe.core.support.CoframeValidationGroups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

/**
 * 抽像主要实体模型，有编码，支持多租户
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@MappedSuperclass
@JsonIgnoreProperties(value = { "createdAt", "updatedAt", "hibernateLazyInitializer" }, allowGetters = true)
@JsonPropertyOrder({ "id", "name", "code", "description", "createdAt", "updatedAt" })
public abstract class AbstractMainModel extends AbstractSimpleModel implements Serializable {
	private static final long serialVersionUID = -4843128001144502327L;

	@NotBlank(groups = { CoframeValidationGroups.Create.class})
	@ApiModelProperty("编码")
	@Size(max = 64)
	private String code;

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
}
