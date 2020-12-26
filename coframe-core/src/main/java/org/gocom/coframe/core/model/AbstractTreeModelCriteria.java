/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 22, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class AbstractTreeModelCriteria extends AbstractMainModelCriteria {
	@ApiModelProperty("父对象的id")
	private String parentId;

	@ApiModelProperty("是否只查询顶层数据")
	private boolean listRoot;

	@ApiModelProperty("是否加载子节点")
	private boolean loadChildren;

	@ApiModelProperty("从当前层开始，需要加载子节点的层级")
	private int loadChildrenLevel = 0;
	
	@ApiModelProperty("是否加载节点的其它资源")
	private boolean loadOthers;

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the listRoot
	 */
	public boolean isListRoot() {
		return listRoot;
	}

	/**
	 * @param listRoot the listRoot to set
	 */
	public void setListRoot(boolean listRoot) {
		this.listRoot = listRoot;
	}

	/**
	 * @return the loadChildrenLevel
	 */
	public int getLoadChildrenLevel() {
		return loadChildrenLevel;
	}
	

	/**
	 * @return the loadChildren
	 */
	public boolean isLoadChildren() {
		return loadChildren;
	}

	/**
	 * @param loadChildren the loadChildren to set
	 */
	public void setLoadChildren(boolean loadChildren) {
		this.loadChildren = loadChildren;
	}

	/**
	 * @param loadChildrenLevel the loadChildrenLevel to set
	 */
	public void setLoadChildrenLevel(int loadChildrenLevel) {
		this.loadChildrenLevel = loadChildrenLevel;
	}
	
	/**
	 * @return the loadOthers
	 */
	public boolean isLoadOthers() {
		return loadOthers;
	}

	/**
	 * @param loadOthers the loadOthers to set
	 */
	public void setLoadOthers(boolean loadOthers) {
		this.loadOthers = loadOthers;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Predicate> buildPredicates(Root root, CriteriaBuilder builder) {
		List<Predicate> predicates = super.buildPredicates(root, builder);
		if (listRoot) {
			predicates.add(builder.isNull(root.get(AbstractTreeModel_.parentId)));
		} else {
			Optional.ofNullable(parentId).ifPresent(value -> {
				predicates.add(builder.equal(root.get(AbstractTreeModel_.parentId), value));
			});
		}
		return predicates;
	}
}
