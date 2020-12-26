/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月12日 23:06:54
 ******************************************************************************/

package com.primeton.iam.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author huzd@primeton.com
 */
@Component
@ConfigurationProperties("security.iam")
public class IAMSecurity {

    private boolean enabled;
    /**
     * 单点登录配置
     */
    private SSO sso = new SSO();

    /**
     * 本地登录地址配置
     */
    @NotNull
    private String loginUrl = "/login";

    /**
     * 本地退出地址配置
     */
    @NotNull
    private String logoutUrl = "/logout";
    
    

    /**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}



	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	/**
	 * @return the sso
	 */
	public SSO getSso() {
		return sso;
	}



	/**
	 * @param sso the sso to set
	 */
	public void setSso(SSO sso) {
		this.sso = sso;
	}



	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}



	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}



	/**
	 * @return the logoutUrl
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}



	/**
	 * @param logoutUrl the logoutUrl to set
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}



	public static class SSO {
        private boolean enabled = false;
        private String host = "";

        /**
         * 单点退出地址
         */
        @NotNull
        private String logoutUrl = "/sso/logout";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getLogoutUrl() {
            return getHost() + logoutUrl;
        }

        public void setLogoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
        }
    }


}
