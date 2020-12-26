/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 26, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import org.gocom.coframe.core.support.CoframeValidationGroups;

/**
 * 抽像持久实体模型，有 id 属性
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@MappedSuperclass
public abstract class AbstractPersistentModel implements Serializable, Cloneable {
	private static final long serialVersionUID = -7734748027883432351L;

	@Id
	/*
	 * 因为现在校验是在Controller层做的, 所以可以使用@Null不让提交时传入id 在Service层还是可以手工setId
	 */
	@Null(groups = CoframeValidationGroups.Create.class)
	@NotBlank(groups = { CoframeValidationGroups.Update.class, CoframeValidationGroups.Association.class })
	private String id;
	
	public AbstractPersistentModel clone() {
		try {
			return (AbstractPersistentModel) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
