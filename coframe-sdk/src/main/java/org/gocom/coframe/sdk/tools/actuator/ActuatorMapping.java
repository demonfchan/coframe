/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jul 3, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.tools.actuator;

import java.util.HashSet;
import java.util.Set;

import org.gocom.coframe.sdk.model.CofFunction;

/**
 * 功能码与 rest api 映射
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class ActuatorMapping {
	/**
	 * rest api http 方法 (POST, PUT, GET, DELETE等）
	 */
	private Set<String> httpMethod = new HashSet<>();

	/**
	 * rest api path (如 /api/users/{userId}
	 */
	private String path;

	/**
	 * rest api 所在实现类的类名
	 */
	private String className;

	/**
	 * rest api 所在方法的方法名
	 */
	private String methodName;

	/**
	 * 方法上绑定的功能
	 */
	private Set<CofFunction> functions = new HashSet<>();

	/**
	 * @return the httpMethod
	 */
	public Set<String> getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(Set<String> httpMethod) {
		this.httpMethod = httpMethod;
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the functions
	 */
	public Set<CofFunction> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(Set<CofFunction> functions) {
		this.functions = functions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActuatorMapping [");
		if (httpMethod != null) {
			builder.append("httpMethod=");
			builder.append(httpMethod);
			builder.append(", ");
		}
		if (path != null) {
			builder.append("path=");
			builder.append(path);
			builder.append(", ");
		}
		if (className != null) {
			builder.append("className=");
			builder.append(className);
			builder.append(", ");
		}
		if (methodName != null) {
			builder.append("methodName=");
			builder.append(methodName);
			builder.append(", ");
		}
		if (functions != null) {
			builder.append("functions=");
			builder.append(functions);
		}
		builder.append("]");
		return builder.toString();
	}
}
