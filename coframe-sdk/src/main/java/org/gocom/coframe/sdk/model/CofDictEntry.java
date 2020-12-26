/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 25, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典项
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofDictEntry extends CofMainModel {
	private static final long serialVersionUID = -8146084084258349518L;

	private CofDictEntry parentEntry;
	private String locale;
	private String status;
	private List<CofDictEntry> children = new ArrayList<>();

	public static class Criteria extends CofMainModel {
		private static final long serialVersionUID = -6294279635462377165L;

		private String dictTypeId; // 字典类型 Id
		private String dictTypeName; // 字典类型名称
		private String dictTypeCode;// 字典类型编码
		private String status;
		private String locale;
		private String parentId;
		private boolean listRoot;
		private boolean loadChildren;
		private int loadChildrenLevel = 0;
		private boolean loadOthers;

		/**
		 * @return the dictTypeName
		 */
		public String getDictTypeName() {
			return dictTypeName;
		}

		/**
		 * @param dictTypeName the dictTypeName to set
		 */
		public void setDictTypeName(String dictTypeName) {
			this.dictTypeName = dictTypeName;
		}

		/**
		 * @return the dictTypeCode
		 */
		public String getDictTypeCode() {
			return dictTypeCode;
		}

		/**
		 * @param dictTypeCode the dictTypeCode to set
		 */
		public void setDictTypeCode(String dictTypeCode) {
			this.dictTypeCode = dictTypeCode;
		}

		/**
		 * @return the dictTypeId
		 */
		public String getDictTypeId() {
			return dictTypeId;
		}

		/**
		 * @param dictTypeId the dictTypeId to set
		 */
		public void setDictTypeId(String dictTypeId) {
			this.dictTypeId = dictTypeId;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the locale
		 */
		public String getLocale() {
			return locale;
		}

		/**
		 * @param locale the locale to set
		 */
		public void setLocale(String locale) {
			this.locale = locale;
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
		 * @return the loadChildrenLevel
		 */
		public int getLoadChildrenLevel() {
			return loadChildrenLevel;
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
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the parentEntry
	 */
	public CofDictEntry getParentEntry() {
		return parentEntry;
	}

	/**
	 * @param parentEntry the parentEntry to set
	 */
	public void setParentEntry(CofDictEntry parentEntry) {
		this.parentEntry = parentEntry;
	}

	/**
	 * @return the children
	 */
	public List<CofDictEntry> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<CofDictEntry> children) {
		this.children = children;
	}
}
