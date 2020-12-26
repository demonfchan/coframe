/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 12, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.util;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.CofContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class HttpUtil {
	/**
	 * 从 HttpServletRequest 取出 token
	 * 
	 * @param request
	 * @return
	 */
	public static String getToken(String bearerToken) {
		if (bearerToken == null)
			return null;
		return StringUtils.removeStart(bearerToken, "Bearer ");
	}

	public static HttpHeaders headers() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CofConstants.HHN_AUTHORIZATION, (String) CofContext.getContext().get(CofConstants.CONTEXT_TOKEN));
		headers.add(CofConstants.HHN_CONTNT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		return headers;
	}

	/**
	 * url构建器
	 * 
	 * @author qinsc (mailto:qinsc@primeton.com)
	 *
	 */
	public static class UrlBuilder {
		private StringBuffer buf = new StringBuffer();

		public UrlBuilder(String prefix) {
			buf.append(prefix);
		}

		public UrlBuilder path(String path) {
			buf.append(path).append("?");
			return this;
		}

		public UrlBuilder queryParam(String paramName, Object paramValue) {
			Optional.ofNullable(paramValue).ifPresent(x -> buf.append(paramName + "=" + x + "&"));
			return this;
		}

		@SuppressWarnings("unchecked")
		public UrlBuilder queryParam(Object paramContainer) {
			Map<String, String> paramMap = JsonUtil.toObject(JsonUtil.toJson(paramContainer), Map.class);
			if (paramMap != null) {
				paramMap.keySet().forEach(key -> {
					queryParam(key, paramMap.get(key));
				});
			}
			return this;
		}

		public UrlBuilder pageParam(Pageable pageable) {
			int pageNum = pageable.getPageNumber();
			int pageSize = pageable.getPageSize();
			queryParam("pageNum", pageNum < 0 ? CofConstants.DEFAULT_BEGIN_PAGE : pageNum);
			queryParam("pageSize", pageSize <= 0 ? 10 : CofConstants.DEFAULT_PAGE_SIZE);
			return this;
		}

		public String build() {
			if (buf.charAt(buf.length() - 1) == '&') {
				buf.setLength(buf.length() - 1);
			}
			if (buf.charAt(buf.length() - 1) == '?') {
				buf.setLength(buf.length() - 1);
			}
			return buf.toString();
		}
	}

//	public static void main(String[] args) {
//		CofDictType.Criteria criteria = new CofDictType.Criteria();
//		criteria.setId("id11");
//		criteria.setName("name11");
//		criteria.setListRoot(true);
//		criteria.setLoadChildrenLevel(5);
//		CofPageRequest pageRequest = new CofPageRequest();
//		pageRequest.setPageNum(13);
//		pageRequest.setPageSize(11);
//		
//		System.out.println("url = " + new UrlBuilder("http://app-demo").path("/api/dict-types/aaaa").queryParam(criteria).queryParam(pageRequest).build());
//		System.out.println("url = " + new UrlBuilder("http://app-demo").path("/api/dict-types/aaaa").queryParam("listBoot","false").build());
//		System.out.println("url = " + new UrlBuilder("http://app-demo").path("/api/dict-types/aaaa").build());
//	}
}
