/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月06日 16:17:41
 ******************************************************************************/

package com.primeton.iam.authentication;

/**
 * @author huzd@primeton.com
 */
public class RequestHeaderToken {


    /**
     * http 请求头名称
     */
    private String name;
    /**
     * HTTP请求头值
     */
    private String value;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	public RequestHeaderToken(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
    
    

}
