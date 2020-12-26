/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 25, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.io.Serializable;

import org.gocom.coframe.sdk.CofConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofPageRequest implements Serializable {
	private static final long serialVersionUID = -5077217744261092151L;
	private int pageNum = CofConstants.DEFAULT_BEGIN_PAGE;
	private int pageSize = CofConstants.DEFAULT_PAGE_SIZE;

	public CofPageRequest() {
	}

	public Pageable createPageable() {
		return PageRequest.of(pageNum, pageSize);
	}

	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
