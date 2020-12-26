/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 25, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.service;

import static org.gocom.coframe.sdk.util.HttpUtil.headers;

import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.model.CofDictEntry;
import org.gocom.coframe.sdk.model.CofDictType;
import org.gocom.coframe.sdk.model.CofPage;
import org.gocom.coframe.sdk.util.HttpUtil.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 字典服务类
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class CofDictService {
	@Autowired
	private CofSDKConfiguration configuration;

	@Autowired
	@Qualifier(CofConstants.COF_REST_TEMPLATE)
	private RestTemplate restTemplate;

	/**
	 * 根据 id 查询字典类型
	 * 
	 * @param id
	 * @param locale
	 * @param loadChildren
	 * @param loadLevel
	 * @return
	 */
	public CofDictType getDictTypeById(String id, String locale, boolean loadChildren, int loadLevel) {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix());
		builder.path("/dict-types/" + id).queryParam("locale", locale).queryParam("loadChildren", loadChildren).queryParam("loadLevel", loadLevel);
		ResponseEntity<CofDictType> dictType = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<String>(headers()), CofDictType.class);
		return dictType.getBody();
	}

	/**
	 * 根据条件分页查询字典类型
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public CofPage<CofDictType> findDictTypesByCriteria(CofDictType.Criteria criteria, Pageable pageable) {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix());
		builder.path("/dict-types/page-search").queryParam(criteria).pageParam(pageable);
		ParameterizedTypeReference<CofPage<CofDictType>> responseType = new ParameterizedTypeReference<CofPage<CofDictType>>() {};
		ResponseEntity<CofPage<CofDictType>> result = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<String>(headers()), responseType);
		return result.getBody();
	}

	/**
	 * 根据 id 查询字典项
	 * 
	 * @param id
	 * @param locale
	 * @param loadChildren
	 * @param loadLevel
	 * @return
	 */
	public CofDictEntry getDictEntryById(String id, String locale, boolean loadChildren, int loadLevel) {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix());
		builder.path("/dict-entries/" + id).queryParam("locale", locale).queryParam("loadChildren", loadChildren).queryParam("loadLevel", loadLevel);
		ResponseEntity<CofDictEntry> dictEntry = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<String>(headers()), CofDictEntry.class);
		return dictEntry.getBody();
	}

	/**
	 * 按条件，分页查询字典项
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public CofPage<CofDictEntry> findDictEntriesByCriteria(CofDictEntry.Criteria criteria, Pageable pageable) {
		UrlBuilder builder = new UrlBuilder(configuration.getCoframeUriPrefix());
		builder.path("/dict-entries/page-search").queryParam(criteria).pageParam(pageable);
		ParameterizedTypeReference<CofPage<CofDictEntry>> responseType = new ParameterizedTypeReference<CofPage<CofDictEntry>>() {};
		ResponseEntity<CofPage<CofDictEntry>> result = restTemplate.exchange(builder.build(), HttpMethod.GET, new HttpEntity<String>(headers()), responseType);
		return result.getBody();
	}
}
