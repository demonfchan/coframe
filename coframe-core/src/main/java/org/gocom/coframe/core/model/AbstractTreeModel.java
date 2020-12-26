/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 20, 2019
 *******************************************************************************/
package org.gocom.coframe.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@MappedSuperclass
public abstract class AbstractTreeModel<T extends AbstractTreeModel<T>> extends AbstractMainModel implements Comparator<T>{
	private static final long serialVersionUID = 1192947361817628106L;

	@Transient
	private T parent;
	
	@ApiModelProperty("父Id")
	private String parentId;
	
	@ApiModelProperty("节点路径标识")
	private String seq;

	@ApiModelProperty("同级序号")
	private Integer sortNo;

	@ApiModelProperty("层级")
	@Column(name="tree_level")
	private Integer level;

	@ApiModelProperty("是否叶节点")
	private boolean isLeaf;

	@Transient
	@ApiModelProperty("子集")
	private List<T> children = new ArrayList<>();

	/**
	 * @return the parent
	 */
	public T getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(T parent) {
		this.parent = parent;
	}

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
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the sortNo
	 */
	public Integer getSortNo() {
		return sortNo;
	}

	/**
	 * @param sortNo the sortNo to set
	 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the children
	 */
	public List<T> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<T> children) {
		this.children = children;
	}

	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	
/*	if (c.compare(a[runHi++], a[lo]) < 0) { // Descending
        while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) < 0)
            runHi++;
        reverseRange(a, lo, runHi);
    } else {                              // Ascending
        while (runHi < hi && c.compare(a[runHi], a[runHi - 1]) >= 0)
            runHi++;
    }*/
	
	@Override
	public int compare(T modle1, T modle2) {
		String[] seq1 = modle1.getSeq().split("\\.");
		List<Integer> m1 = new ArrayList<>();
		String[] seq2 = modle2.getSeq().split("\\.");
		List<Integer> m2 = new ArrayList<>();
		Arrays.asList(seq1).stream().forEach(seq -> {
			if(StringUtils.isNotBlank(seq)) {
				m1.add(Integer.valueOf(seq));
			}
		}
				);
		Arrays.asList(seq2).stream().forEach(seq -> {
			if(StringUtils.isNotBlank(seq)) {
				m2.add(Integer.valueOf(seq));
			}
		}
				);
		for( int i = 0; i < m1.size(); i++ ) {
			for(int j = i ; j < m2.size(); ) {
				if(m1.size() == m2.size()) {
					return -m1.get(i).compareTo(m2.get(i));
				}else if(m1.size() < m2.size()) {
					if(m1.get(i).compareTo(m2.get(j)) == 0) {
						continue;
					}else if(m1.size() == i) {
						return -1;
					} else {
						return -m1.get(i).compareTo(m2.get(j));
					}
				} else if(m1.size() > m2.size()) {
					if(m1.get(i).compareTo(m2.get(j)) == 0) {
						continue;
					}else if(m2.size() == j) {
						return 1;
					} else {
						return -m1.get(i).compareTo(m2.get(j));
					}
				}
			}
		}
		return 0;
	}
}
