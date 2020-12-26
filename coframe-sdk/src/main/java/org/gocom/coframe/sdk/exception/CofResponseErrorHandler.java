/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 27, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.exception;

import java.io.IOException;
import java.io.InputStream;

import org.gocom.coframe.sdk.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 统一处理 sdk 通过 rest template 向 coframe 发送请求时，捕获到的异常，并进行异常传递（请求转发时，比如登陆登出）
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CofResponseErrorHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		String json = getBody(httpResponse);
		if (json != null) {
			CofErrorObject error = JsonUtil.toObject(json, CofErrorObject.class);
			throw new CofRuntimeException(error.getError(), HttpStatus.UNAUTHORIZED.value(), error.getMessage(), null);
		}
		throw new HttpClientErrorException(httpResponse.getStatusCode());
	}

	/**
	 * 读取返回的数据
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String getBody(ClientHttpResponse response) throws IOException {
		StringBuffer buf = new StringBuffer();
		byte[] b = new byte[512];
		InputStream in = response.getBody();
		int read = 0;
		while ((read = in.read(b)) > 0) {
			buf.append(new String(b, 0, read));
		}
		return buf.toString();
	}
}