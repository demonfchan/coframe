/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月11日 14:34:12
 ******************************************************************************/

package com.primeton.iam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.util.StringUtils;

import com.primeton.iam.client.IAMClientLoginFailureHandler;
import com.primeton.iam.client.IAMClientLoginSuccessHandler;

/**
 * @author huzd@primeton.com
 * @see OAuth2AutoConfigurationExclusionFilter
 */
@Configuration
@ConditionalOnBean(IAMSecurityConfiguration.class)
@AutoConfigureAfter(IAMSecurityConfiguration.class)
@Conditional(IAMFormLoginConfiguration.IAMFormCondition.class)
@Order(101)
public class IAMFormLoginConfiguration extends WebSecurityConfigurerAdapter {

    private final IAMSecurityConfiguration securityConfiguration;

    
    @Autowired
    private IAMSecurity iamSecurity;
    
    public static interface CofHeaderWriter extends HeaderWriter{};

    public IAMFormLoginConfiguration(@Autowired IAMSecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(new IAMClientLoginSuccessHandler())
                .failureHandler(new IAMClientLoginFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
                .logoutUrl(iamSecurity.getLogoutUrl());
        this.securityConfiguration.configure(http);
    }

    static class IAMFormCondition extends SpringBootCondition {
        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context,
                                                AnnotatedTypeMetadata metadata) {
            String enabled = context.getEnvironment().getProperty("security.iam.sso.enabled");
            ConditionMessage.Builder message = ConditionMessage
                    .forCondition("IAM Form Login");
            if (!StringUtils.hasLength(enabled) || "false".equalsIgnoreCase(enabled)) {
                return ConditionOutcome.match(message
                        .foundExactly("IAM Form Login enabled on miss security.iam.sso.enabled."));
            }
            return ConditionOutcome.noMatch(message
                    .didNotFind("IAM Form Login disabled").atAll());
        }
    }
}
