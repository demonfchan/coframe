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
 * 字典类型
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofDictType extends CofMainModel {
	private static final long serialVersionUID = -6797906725535427308L;

	private String locale;

	private List<CofDictType> children = new ArrayList<>();

	public static class Criteria extends CofMainModel {
		private static final long serialVersionUID = 5256620749814368292L;
		private String locale;
		private String parentId;
		private boolean listRoot;
		private boolean loadChildren;
		private int loadChildrenLevel = 0;
		private boolean loadOthers;

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
	 * @return the children
	 */
	public List<CofDictType> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<CofDictType> children) {
		this.children = children;
	}
}
