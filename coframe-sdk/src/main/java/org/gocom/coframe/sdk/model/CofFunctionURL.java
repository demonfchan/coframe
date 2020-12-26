/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 1, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.io.Serializable;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofFunctionURL implements Serializable {
	private static final long serialVersionUID = 6521672170558593946L;
	private String method;
	private String path;
	private String pathRegExp;

	public CofFunctionURL() {
	}

	/**
	 * @param method
	 * @param path
	 */
	public CofFunctionURL(String method, String path) {
		this.method = method;
		this.path = path;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the pathRegExp
	 */
	public String getPathRegExp() {
		return pathRegExp;
	}

	/**
	 * @param pathRegExp the pathRegExp to set
	 */
	public void setPathRegExp(String pathRegExp) {
		this.pathRegExp = pathRegExp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CofFunctionURL [");
		if (method != null) {
			builder.append("method=");
			builder.append(method);
			builder.append(", ");
		}
		if (path != null) {
			builder.append("path=");
			builder.append(path);
			builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
}
