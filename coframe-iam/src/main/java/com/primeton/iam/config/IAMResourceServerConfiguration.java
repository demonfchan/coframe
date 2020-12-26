/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 13:42:59
 ******************************************************************************/

package com.primeton.iam.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.RequestContextListener;

import com.primeton.iam.client.CookieTokenExtractor;
import com.primeton.iam.client.DisableUrlSessionFilter;
import com.primeton.iam.client.IAMAuthenticationEntryPoint;
import com.primeton.iam.client.IAMAuthenticationFailureHandler;
import com.primeton.iam.client.IAMClientLogoutHandler;
import com.primeton.iam.client.IAMClientUserAuthenticationConverter;
import com.primeton.iam.client.IAMCookieTokenWriteFilter;
import com.primeton.iam.client.IAMLoginUrlAuthenticationEntryPoint;
import com.primeton.iam.client.IAMSSOAuthenticationSuccessHandler;
import com.primeton.iam.client.SsoTokenFilter;

/**
 * @author huzd@primeton.com
 */
@Configuration
@EnableOAuth2Client
@EnableResourceServer
@AutoConfigureAfter(IAMSecurityConfiguration.class)
@ConditionalOnBean(IAMSecurityConfiguration.class)
@ConditionalOnProperty(name = "security.iam.sso.enabled")
@EnableConfigurationProperties(OAuth2SsoProperties.class)
@AutoConfigureBefore(OAuth2AutoConfiguration.class)
public class IAMResourceServerConfiguration extends ResourceServerConfigurerAdapter implements JwtAccessTokenConverterConfigurer {
	
//	@Autowired
//	OAuth2RestTemplate  restTemplate;
    @Autowired
    private IAMSecurity iamSecurity;
    @Autowired
    private OAuth2SsoProperties ssoProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IAMSecurityConfiguration securityConfiguration;
    @Autowired
    private  DisableUrlSessionFilter disableUrlSessionFilter;
    
//    @Autowired
//    public UserService userService;
//    @Autowired
//    public SSoTokenService sSoTokenService;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        securityConfiguration.configure(http);
//        CookieTokenService cookieTokenService = securityConfiguration.cookieTokenService();
        // @formatter:off
        http.cors().and().csrf()
                .disable()
                .headers().frameOptions().disable()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(new FrameOptionsAllowFromStrategy()))
                .and()
            .anonymous().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/iam/*.html")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .logout()
                    .addLogoutHandler(iamLogoutHandler())
                    .logoutUrl(iamSecurity.getLogoutUrl())
                    //TODO 此处是否需要再加一个处理 jsonp 的处理,因为单点退出是在 iFrame 内进行调用
                    // 目前是判断返回的状态码是否是204
                    .defaultLogoutSuccessHandlerFor(iframeLogoutSuccessHandler("/iam/logout.html"),
                            request-> "iframe".equals(request.getParameter("display")))
                    .defaultLogoutSuccessHandlerFor(
                            new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT),
                            new OrRequestMatcher(jsonXmlRequestMatcher(), ajaxRequestMatcher()));
        // @formatter:on
        // 配置单点登录处理器
        OAuth2ClientAuthenticationProcessingFilter ssoFilter = oauth2SsoFilter();
        http.addFilterBefore(ssoFilter, BasicAuthenticationFilter.class);
        http.addFilterAfter(ssoFilter, AbstractPreAuthenticatedProcessingFilter.class);
        ssoFilter.setFilterProcessesUrl(iamSecurity.getLoginUrl());
        ssoFilter.setAuthenticationSuccessHandler(ssoAuthenticationSuccessHandler());
        ssoFilter.setAuthenticationFailureHandler(iamAuthenticationFailureHandler());
        // 添加一个 filter, 在每次修改 token 之后,将 token 写入 cookie
        http.addFilterAfter(iamCookieTokenWriteFilter(),
                OAuth2ClientAuthenticationProcessingFilter.class);
        
        http.addFilterBefore(disableUrlSessionFilter,oauth2SsoFilter().getClass());
        http.addFilterAfter(ssoTokenFilter(),iamCookieTokenWriteFilter().getClass());
    }
    
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    /**
     * 新增
     * @param context
     * @param details
     * @return
     */
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext context, OAuth2ProtectedResourceDetails details) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(details, context);

        AuthorizationCodeAccessTokenProvider authCodeProvider = new AuthorizationCodeAccessTokenProvider();
        authCodeProvider.setStateMandatory(false);
        AccessTokenProviderChain provider = new AccessTokenProviderChain(
                Arrays.asList(authCodeProvider));
        template.setAccessTokenProvider(provider);
        return template;
    }

    /**
     * 返回一个需要在 iframe 中进行退出的回调的网页地址
     */
    private LogoutSuccessHandler iframeLogoutSuccessHandler(String url) {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setAlwaysUseDefaultTargetUrl(true);
        handler.setDefaultTargetUrl(url);
        return handler;
    }

    public IAMCookieTokenWriteFilter iamCookieTokenWriteFilter() {
        OAuth2RestOperations restTemplate = this.applicationContext
                .getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        return new IAMCookieTokenWriteFilter(securityConfiguration.cookieTokenService(), restTemplate);
    }

    @Bean
    public IAMAuthenticationFailureHandler iamAuthenticationFailureHandler() {
        return new IAMAuthenticationFailureHandler();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.stateless(true)
                .authenticationEntryPoint(iamAuthenticationEntryPoint())
                .tokenExtractor(tokenExtractor());
    }

    @Bean
    public LogoutHandler iamLogoutHandler() {
        IAMClientLogoutHandler handler = new IAMClientLogoutHandler();
        OAuth2RestOperations restTemplate = this.applicationContext
                .getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();

        IAMSecurity iamSecurity = this.applicationContext
                .getBean(IAMSecurity.class);
        handler.setCookieTokenService(securityConfiguration.cookieTokenService());
        handler.setIamSecurity(iamSecurity);
        handler.setRestTemplate(restTemplate);
        handler.setDefaultLoginUrl(iamSecurity.getLoginUrl());
        return handler;
    }

    @Bean
    public IAMAuthenticationEntryPoint iamAuthenticationEntryPoint() {
        IAMAuthenticationEntryPoint entryPoint = new IAMAuthenticationEntryPoint();
        entryPoint.setCookieTokenService(securityConfiguration.cookieTokenService());
        entryPoint.setEntryPoint(delegatingAuthenticationEntryPoint());
        return entryPoint;
    }

    @Bean
    public DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint() {
        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();

        // 根据 http accept header 判断,返回页面

        IAMLoginUrlAuthenticationEntryPoint iamEntryPoint =
                new IAMLoginUrlAuthenticationEntryPoint(iamSecurity.getLoginUrl());
        iamEntryPoint.setCookieTokenService(securityConfiguration.cookieTokenService());
        // 网页
        entryPoints.put(textRequestMatcher(), iamEntryPoint);

        // json,xml ajax 返回401
        entryPoints.put(new OrRequestMatcher(jsonXmlRequestMatcher(), ajaxRequestMatcher()), new OAuth2AuthenticationEntryPoint());
        DelegatingAuthenticationEntryPoint entryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        entryPoint.setDefaultEntryPoint(new OAuth2AuthenticationEntryPoint());
        return entryPoint;
    }

    private RequestMatcher ajaxRequestMatcher() {
        return new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest");
    }

    private RequestMatcher jsonXmlRequestMatcher() {
        MediaTypeRequestMatcher jsonXmlMatcher = new MediaTypeRequestMatcher(getContentNegotiationStrategy(),
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML);
        jsonXmlMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return jsonXmlMatcher;
    }

    private RequestMatcher textRequestMatcher() {
        MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(
                getContentNegotiationStrategy(), MediaType.APPLICATION_XHTML_XML,
                new MediaType("image", "*"), MediaType.TEXT_HTML, MediaType.TEXT_PLAIN);
        preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return preferredMatcher;
    }

    private ContentNegotiationStrategy getContentNegotiationStrategy() {
        ContentNegotiationStrategy contentNegotiationStrategy = applicationContext.getBean(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        return contentNegotiationStrategy;
    }

    @Bean
    public TokenExtractor tokenExtractor() {
        OAuth2RestOperations restTemplate = this.applicationContext
                .getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        TokenStore tokenStore = this.applicationContext.getBean(TokenStore.class);
        CookieTokenExtractor tokenExtractor = new CookieTokenExtractor();
        tokenExtractor.setRestTemplate(restTemplate);
        tokenExtractor.setTokenStore(tokenStore);
        tokenExtractor.setIamSecurity(iamSecurity);
        tokenExtractor.setCookieTokenService(securityConfiguration.cookieTokenService());
        return tokenExtractor;
    }

    @Bean
    public IAMSSOAuthenticationSuccessHandler ssoAuthenticationSuccessHandler() {
        OAuth2RestOperations restTemplate = this.applicationContext
                .getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        IAMSSOAuthenticationSuccessHandler handler = new IAMSSOAuthenticationSuccessHandler();
        handler.setRestTemplate(restTemplate);
        handler.setCookieTokenService(securityConfiguration.cookieTokenService());
        return handler;
    }

    /**
     * 用户信息处理
     *
     * @return UserAuthenticationConverter
     */
    @Bean
    public UserAuthenticationConverter userTokenConverter() {
        return new IAMClientUserAuthenticationConverter();
    }

    @Override
    public void configure(JwtAccessTokenConverter converter) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter());
        converter.setAccessTokenConverter(accessTokenConverter);
    }

    private OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter() {
        OAuth2RestOperations restTemplate = this.applicationContext 
                .getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        ResourceServerTokenServices tokenServices = this.applicationContext
                .getBean(ResourceServerTokenServices.class);
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                ssoProperties.getLoginPath());
        filter.setRestTemplate(restTemplate);
        filter.setTokenServices(tokenServices);
        filter.setApplicationEventPublisher(this.applicationContext);
        return filter;
    }
    
    @Bean
    public SsoTokenFilter ssoTokenFilter() {
    	SsoTokenFilter ssoTokenFilter = new SsoTokenFilter();
    	return ssoTokenFilter;
    }
}
