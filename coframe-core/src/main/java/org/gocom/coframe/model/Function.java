/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on November 20, 2010
 *******************************************************************************/
package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractMainModelCriteria;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.gocom.coframe.sdk.model.CofFunctionURL;
import org.gocom.coframe.sdk.util.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 菜单
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Entity
@Table(name = TABLE_CORE_PREFIX + "FUNCTION")
@ApiModel("功能")
public class Function extends AbstractMainModel {
	private static final long serialVersionUID = 1033376223562344577L;

	@NotBlank(groups = CoframeValidationGroups.Create.class)
	@Null(groups = CoframeValidationGroups.Update.class)
	private String groupId;

	@ApiModelProperty("类型")
	private String type = CoframeConstants.RESOURCE_TYPE_FUNCTION;

	@ApiModelProperty("是否需要校验")
	@Column(name = "IS_CHECK")
	private boolean check = true;

	@Transient
	private boolean selected;

	@Column(name = "URLS")
	@Convert(converter = URLsConvertor.class)
	private Set<CofFunctionURL> urls = new HashSet<>();

	/**
	 * 查询条件
	 *
	 * @author qinsc (mailto:qinsc@primeton.com)
	 */
	public static class Criteria extends AbstractMainModelCriteria {
		private String groupId;
		private String type;

		/**
		 * @return the groupId
		 */
		public String getGroupId() {
			return groupId;
		}

		/**
		 * @param groupId the groupId to set
		 */
		public void setGroupId(String groupId) {
			this.groupId = groupId;
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
	}

	public static class URLsConvertor implements AttributeConverter<Set<CofFunctionURL>, String> {
		private ObjectMapper mapper = new ObjectMapper();

		@Override
		public String convertToDatabaseColumn(Set<CofFunctionURL> urls) {
			if(urls == null) {
				urls = new HashSet<>();
			}
			Set<String> formatUrls = urls.stream().map(x -> JsonUtil.toJson(x)).collect(Collectors.toSet());
			try {
				return mapper.writeValueAsString(formatUrls);
			} catch (JsonProcessingException e) {
			}
			return "[]";
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Set<CofFunctionURL> convertToEntityAttribute(String json) {
			if (json != null) {
				List formatUrls = null;
				try {
					formatUrls = mapper.readValue(json, List.class);
				} catch (IOException e) {
				}
				if (formatUrls != null) {
					return (Set<CofFunctionURL>) formatUrls.stream().map(x -> JsonUtil.toObject((String)x, CofFunctionURL.class)).collect(Collectors.toSet());
				}
			}
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Set<CofFunctionURL> getUrls() {
		return urls;
	}

	public void setUrls(Set<CofFunctionURL> urls) {
		this.urls = urls;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
}
