/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Aug 2, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import java.util.Optional;

import org.gocom.coframe.model.Function;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public interface IFunctionService {
	/**
	 * 给定功能编码, 查询功能
	 * @param code
	 * @return
	 */
	public Optional<Function> findByCode(String code);
}
