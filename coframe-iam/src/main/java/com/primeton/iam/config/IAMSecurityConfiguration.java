package com.primeton.iam.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.primeton.iam.authentication.RequestHeaderAuthenticationChecker;
import com.primeton.iam.authentication.RequestHeaderAuthenticationManager;
import com.primeton.iam.authentication.RequestHeaderTokenAuthenticationFilter;
import com.primeton.iam.client.CookieTokenService;
import com.primeton.iam.client.IAMLoginUrlAuthenticationEntryPoint;
import com.primeton.iam.client.IAMUserInfoRestTemplateCustomizer;
import com.primeton.iam.client.cipher.DefaultTokenCipher;

@Configuration
@Import({ IAMSecurity.class, IAMWebSecurityIgnoreConfig.class })
@ConditionalOnProperty(prefix = "security.iam", name = "enabled", havingValue = "true")
@ComponentScan(basePackages = { "com.primeton.iam.controller", "com.primeton.iam.client" })
public class IAMSecurityConfiguration {

	private static Logger log = LoggerFactory.getLogger(IAMSecurityConfiguration.class);
	@Autowired
	private IAMSecurity iamSecurity;

	@Autowired(required = false)
	private List<IAMSecurityConfigurer> iamSecurityConfigurers = new ArrayList<>();

	@Autowired(required = false)
	private List<RequestHeaderAuthenticationChecker> requestHeaderAuthenticationCheckers = new ArrayList<>();

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${spring.application.name:${vcap.application.name:${spring.config.name:application}}}")
	private String applicationName;

	public void configure(HttpSecurity http) throws Exception {

		addAuthenticationEntryPoint(http);
		// 添加请求头处理器
		configureRequestHeaderTokenAuthenticationFilters(http);
		// 进行扩展配置
		configureIAMSecurityConfigurers(http);
	}

	/**
	 * 配置异常入口点
	 *
	 * @param http
	 * @throws Exception
	 */
	private void addAuthenticationEntryPoint(HttpSecurity http) throws Exception {
		ExceptionHandlingConfigurer<HttpSecurity> exceptions = http.exceptionHandling();
		ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
		if (contentNegotiationStrategy == null) {
			contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
		}
		MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML, MediaType.TEXT_PLAIN);
		preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));

		IAMLoginUrlAuthenticationEntryPoint entryPoint = new IAMLoginUrlAuthenticationEntryPoint(iamSecurity.getLoginUrl());
		entryPoint.setCookieTokenService(cookieTokenService());

		MediaTypeRequestMatcher jsonXmlMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy, MediaType.APPLICATION_JSON);
		jsonXmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));

		RequestHeaderRequestMatcher ajaxMatcher = new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
		// When multiple entry points are provided the default is the first one
		exceptions.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new OrRequestMatcher(ajaxMatcher, jsonXmlMatcher));
		exceptions.defaultAuthenticationEntryPointFor(entryPoint, preferredMatcher);
	}

	@Bean
	@ConfigurationProperties("security.iam.cookie")
	public CookieTokenService cookieTokenService() {
		CookieTokenService tokenService = new CookieTokenService();
		tokenService.setTokenCipher(iamTokenCipher());
		if (StringUtils.hasText(applicationName)) {
			tokenService.setCookieNameSuffix(Integer.toHexString(applicationContext.getApplicationName().hashCode()));
		}
		return tokenService;
	}

	@Bean
	@ConfigurationProperties(prefix = "security.iam.token.cipher")
	public DefaultTokenCipher iamTokenCipher() {
		return new DefaultTokenCipher();
	}

	@Bean
	public IAMUserInfoRestTemplateCustomizer iamUserInfoRestTemplateCustomizer() {
		return new IAMUserInfoRestTemplateCustomizer();
	}

	private void configureRequestHeaderTokenAuthenticationFilters(HttpSecurity http) {
		// 添加请求头认证管理器
		RequestHeaderAuthenticationManager requestHeaderAuthenticationManager = new RequestHeaderAuthenticationManager(requestHeaderAuthenticationCheckers);
		// 添加请求头处理器
		requestHeaderAuthenticationCheckers.forEach(checker -> {
			log.info(String.format("Add filter [%s] before to [%s]", checker, AbstractPreAuthenticatedProcessingFilter.class));
			RequestHeaderTokenAuthenticationFilter filter = new RequestHeaderTokenAuthenticationFilter(checker.getHeaderName());
			filter.setAuthenticationManager(requestHeaderAuthenticationManager);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(filter);

			http.addFilterBefore(filter, AbstractPreAuthenticatedProcessingFilter.class);
		});
	}

	private void configureIAMSecurityConfigurers(HttpSecurity http) throws Exception {
		// 进行扩展配置
		for (IAMSecurityConfigurer iamSecurityConfigurer : iamSecurityConfigurers) {
			log.info("Config iam security configurer : " + iamSecurityConfigurer);
			iamSecurityConfigurer.configure(http);
		}
	}

	/**
	 * 处理重定向是的 x-forward-*问题
	 *
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter(SecurityProperties securityProperties) {
		FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter = new FilterRegistrationBean<>();
		forwardedHeaderFilter.setFilter(new ForwardedHeaderFilter());
		forwardedHeaderFilter.setOrder(securityProperties.getFilter().getOrder() - 1);
		return forwardedHeaderFilter;
	}
}