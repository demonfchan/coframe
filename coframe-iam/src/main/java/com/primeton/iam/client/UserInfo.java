/*******************************************************************************
 *
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2018年6月28日 下午6:57:12
 *******************************************************************************/

package com.primeton.iam.client;

import java.util.HashMap;
import java.util.Map;

/**
 * UserInfo.
 *
 * @author haoyf (mailto: haoyf@primeton.com)
 */
public class UserInfo implements IUserInfo {
	
	private String username;
	
	private String nickname;
	
	private String email;
	
	private Map<String,Object> attributes = new HashMap<>();

	/**
	 * @return Returns the attributes.
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	
	public void setAttribute(String key , Object value) {
		this.attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * @return Returns the nickname.
	 */
	public String getNickname() {
		return nickname;
	}


	/**
	 * @param nickname The nickname to set.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}


	
}
