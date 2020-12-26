/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:28 PM
 */

package org.gocom.coframe.sdk.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.DispatcherType;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.sdk.cache.RedisCache;
import org.gocom.coframe.sdk.exception.CofResponseErrorHandler;
import org.gocom.coframe.sdk.filter.CofTokenFilter;
import org.gocom.coframe.sdk.service.CofSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * sdk 配置
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Configuration
public class CofSDKConfiguration {
	private Logger log = LoggerFactory.getLogger(CofSDKConfiguration.class);
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Value("${server.servlet.context-path:/}")
	private String contextPath;

	@Value("${coframe.filter.token.validate-token:false}")
	private boolean validateToken;

	@Value("${coframe.filter.token.uri-patterns:''}")
	private String urlPatterns;

	@Value("${coframe.filter.token.exclude-uri-patterns:''}")
	private String excludeUrlPatterns;

	@Value("${coframe.server.name:''}") // coframe 服务的名称
	private String serverName;

	@Value("${coframe.server.context-path:''}") // coframe 服务的 context-path
	private String serverContextPath;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	@ConditionalOnProperty(name = "coframe.filter.token.enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean cofTokenFilter(CofSDK sdk) {
		log.info("Enabled cofTokenFilter, parame is: ");
		log.info("	     coframe.filter.token.validate-token: {}", validateToken);
		log.info("	     coframe.filter.token.uri-patterns: {}", urlPatterns);
		log.info("	     coframe.filter.token.exclude-uri-patterns: {}", excludeUrlPatterns);

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CofTokenFilter(this, sdk));
		registration.addUrlPatterns(urlPatterns);// 拦截路径
		registration.setName("cofTokenFilter");// 拦截器名称
		registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST)); // 只拦截请求
		registration.setOrder(1);// 顺序
		return registration;
	}

	@LoadBalanced
	@Bean(name = CofConstants.COF_REST_TEMPLATE)
	public RestTemplate cofRestTemplate() {
		log.info("Regist cofRestTemplate");
		RestTemplate restTemplate = new RestTemplateBuilder().errorHandler(new CofResponseErrorHandler()).build();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		return restTemplate;
	}

	@Bean
	@ConditionalOnExpression("${coframe.cache.enabled:true} && '${coframe.cache.type}'.equals('redis')")
	public ICacheManager cofRedisCacheManager() {
		log.info("Regist cofRedisCacheManager");
		return new RedisCache(redisTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<String> getExcludeUriPattern() {
		if (StringUtils.isNotBlank(excludeUrlPatterns)) {
			return Arrays.asList(excludeUrlPatterns.split(",")).stream().filter(StringUtils::isNotBlank).map(x -> uri(x)).collect(Collectors.toList());
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * 为 uri 添加 contextPath
	 * 
	 * @param uri
	 * @return
	 */
	public String uri(String uri) {
		return "/".equals(contextPath) ? uri : contextPath + uri;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @return the excludeUrlPatterns
	 */
	public String getExcludeUrlPatterns() {
		return excludeUrlPatterns;
	}

	/**
	 * 判断 uri 是否需要拦截
	 * 
	 * @param uri
	 * @return
	 */
	public boolean shouldFilter(String uri) {
		List<String> excludeUriPatterns = getExcludeUriPattern();
		for (String excludeUriPattern : excludeUriPatterns) {
			if (uri.matches(excludeUriPattern)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the serverName
	 */
	public String getCoframeUriPrefix() {
		return "http://" + serverName + ("/".equals(serverContextPath) ? "/" : serverContextPath) + "/api";
	}

	/**
	 * @return the validateToken
	 */
	public boolean isValidateToken() {
		return validateToken;
	}
}
