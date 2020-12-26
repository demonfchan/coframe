/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 09:47:59
 ******************************************************************************/

package com.primeton.iam.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 如果是 启动 IAMFormLoginConfiguration 模式,则过滤掉 OAuth2AutoConfiguration
 *
 * @author huzd@primeton.com
 */
public class OAuth2AutoConfigurationExclusionFilter implements AutoConfigurationImportFilter, EnvironmentAware {
    private final String oauth2AutoConfigurationClass = OAuth2ClientAutoConfiguration.class.getCanonicalName();
    private static Logger log = LoggerFactory.getLogger(OAuth2AutoConfigurationExclusionFilter.class);
    private final String securityAutoConfigClasses =
            "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
                    "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration," +
                    "org.springframework.boot.autoconfigure.security.servlet.FallbackWebSecurityAutoConfiguration"; /*TODO: springboot2.0+ 以后没有该类 wangwb */

    private String ignoreAutoConfigurations = "";

    @Override
    public void setEnvironment(Environment environment) {
    	
        if (!isEnabled(environment.getProperty("security.iam.enabled"))) {
            ignoreAutoConfigurations = securityAutoConfigClasses + "," + oauth2AutoConfigurationClass;
        }
        if (!isEnabled(environment.getProperty("security.iam.sso.enabled"))) {
            ignoreAutoConfigurations = ignoreAutoConfigurations + "," + oauth2AutoConfigurationClass;
        }

        log.info("AutoConfigurationExclusion: " + ignoreAutoConfigurations);
    }

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] match = new boolean[autoConfigurationClasses.length];
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
        	if(autoConfigurationClasses[i] != null) {
        		 match[i] = !ignoreAutoConfigurations.contains(autoConfigurationClasses[i]);
        	}
        }
        return match;
    }

    private boolean isEnabled(String enabled) {
        return StringUtils.hasLength(enabled) && "true".equalsIgnoreCase(enabled);
    }
}
