///*******************************************************************************
// *
// * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
// * All rights reserved.
// *
// * Created on Feb 28, 2019
// *******************************************************************************/
//package org.gocom.coframe.model;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Set;
//
//import org.gocom.coframe.sdk.model.CofFunction;
//
///**
// * @author qinsc (mailto:qinsc@primeton.com)
// */
//public class LoginedUser implements Serializable {
//	private static final long serialVersionUID = 9083739604855816504L;
//	private String userId;
//	private String userName;
//	private String state;
//	private String salt;
//	private Set<CofFunction> functions;
//	private List<Role> roles;
//
//	/**
//	 * 默认构造方法
//	 */
//	public LoginedUser() {
//	}
//
//	/**
//	 * @param userId
//	 * @param salt
//	 * @param functionCodes
//	 */
//	public LoginedUser(String userId, String userName, String state, String salt, List<Role> roles, Set<CofFunction> functions) {
//		this.userId = userId;
//		this.userName = userName;
//		this.state = state;
//		this.salt = salt;
//		this.roles = roles;
//		this.functions = functions;
//	}
//
//	/**
//	 * @return the userId
//	 */
//	public String getUserId() {
//		return userId;
//	}
//
//	/**
//	 * @param userId the userId to set
//	 */
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	/**
//	 * @return the userName
//	 */
//	public String getUserName() {
//		return userName;
//	}
//
//	/**
//	 * @param userName the userName to set
//	 */
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	/**
//	 * @return the state
//	 */
//	public String getState() {
//		return state;
//	}
//
//	/**
//	 * @param state the state to set
//	 */
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	/**
//	 * @return the salt
//	 */
//	public String getSalt() {
//		return salt;
//	}
//
//	/**
//	 * @param salt the salt to set
//	 */
//	public void setSalt(String salt) {
//		this.salt = salt;
//	}
//
//	/**
//	 * @return the functions
//	 */
//	public Set<CofFunction> getFunctions() {
//		return functions;
//	}
//
//	/**
//	 * @param functions the functions to set
//	 */
//	public void setFunctions(Set<CofFunction> functions) {
//		this.functions = functions;
//	}
//
//	/**
//	 * @return the roles
//	 */
//	public List<Role> getRoles() {
//		return roles;
//	}
//
//	/**
//	 * @param roles the roles to set
//	 */
//	public void setRoles(List<Role> roles) {
//		this.roles = roles;
//	}
//}