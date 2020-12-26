/*******************************************************************************
 *
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2018年6月29日 上午9:29:38
 *******************************************************************************/

package com.primeton.iam.client;

/**
 * IUserInfo.
 *
 * @author haoyf (mailto: haoyf@primeton.com)
 */
public interface IUserInfo {
	
	/**
	 * 获取用户账号
	 * @return
	 */
	String getUsername();
	
	/**
	 * 获取用户姓名
	 * @return
	 */
	String getNickname();
	
	/**
	 * 账号邮件
	 * @return
	 */
	String getEmail();
	

}
