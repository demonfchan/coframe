/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:28 PM
 */

package org.gocom.coframe.sdk.model;

import java.util.HashSet;
import java.util.Set;

public class CofFunction extends CofMainModel implements Comparable<CofFunction>{
	private static final long serialVersionUID = 394468850993520387L;
	private String groupId;
	private String type;
	private Set<CofFunctionURL> urls = new HashSet<>();
	private boolean imported = false;

	/**
	 * 
	 */
	public CofFunction() {
	}

	/**
	 * @param id
	 * @param name
	 * @param code
	 * @param urls
	 */
	public CofFunction(String id, String name, String code, Set<CofFunctionURL> urls) {
		this.urls = urls;
		super.setId(id);
		super.setName(name);
		super.setCode(code);
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
	 * @return the urls
	 */
	public Set<CofFunctionURL> getUrls() {
		return urls;
	}

	/**
	 * @param urls the urls to set
	 */
	public void setUrls(Set<CofFunctionURL> urls) {
		this.urls = urls;
	}

	/**
	 * @return the imported
	 */
	public boolean isImported() {
		return imported;
	}

	/**
	 * @param imported the imported to set
	 */
	public void setImported(boolean imported) {
		this.imported = imported;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CofFunction [");
		if (groupId != null) {
			builder.append("groupId=");
			builder.append(groupId);
			builder.append(", ");
		}
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (getName() != null) {
			builder.append("name=");
			builder.append(getName());
			builder.append(", ");
		}
		if (getCode() != null) {
			builder.append("code=");
			builder.append(getCode());
			builder.append(", ");
		}
		if (urls.size() > 0) {
			builder.append("urls=");
			builder.append(urls);
			builder.append(", ");
		}

		builder.append("imported=");
		builder.append(imported);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(CofFunction o) {
		return getCode().compareTo(o.getCode());
	}
}
