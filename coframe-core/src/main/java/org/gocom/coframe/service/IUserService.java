/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.Set;

import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface IUserService {
	/**
	 * 从上下文中取得当前登陆用户
	 * 
	 * @return
	 */
	public CofUser getCurrentLoginedUser();

	/**
	 * 根据用户名找到用户
	 * 
	 * @param userName
	 * @return
	 */
	public CofUser getUserByName(String userName);

	/**
	 * 获取用户的密码（加密的）
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserPassword(String userId);

	/**
	 * 列所用户所有的角色
	 * 
	 * @param userId
	 * @return
	 */
	public Set<CofRole> getUserRoles(String userId);

	/**
	 * 根据 id 获取用户角色
	 * 
	 * @param roleId
	 * @return
	 */
	public CofRole getRole(String roleId);

	/**
	 * 更新用户
	 * 
	 * @param userId
	 * @param salt
	 * @return
	 */
	void updateSalt(String userId, String salt);
}
