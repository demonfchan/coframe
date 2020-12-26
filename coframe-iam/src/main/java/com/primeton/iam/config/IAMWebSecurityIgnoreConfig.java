package com.primeton.iam.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

//import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;

/**
 * @author huzd@primeton.com
 */
@Configuration
@Order(SecurityProperties.IGNORED_ORDER)
public class IAMWebSecurityIgnoreConfig extends WebSecurityConfigurerAdapter {

	private final EOSSecurityProperties eosSecurityProperties;

	@Autowired
	public IAMWebSecurityIgnoreConfig(EOSSecurityProperties eosSecurityProperties) {
		super(true);
		this.eosSecurityProperties = eosSecurityProperties;
	}

	@Override
	public void configure(WebSecurity web) {
		WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
		if (eosSecurityProperties.isResources()) {
			ignoring.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		}
		eosSecurityProperties.getPaths().forEach(ignoring::antMatchers);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// non filters, ignore all request
		http.requestMatcher(request -> false);
	}

	@Component
	@ConfigurationProperties(prefix = "eos.security.ignored")
	@SuppressWarnings("unused")
	private static class EOSSecurityProperties {

		private List<String> paths = new ArrayList<>();
		private boolean endpoints = false;
		private boolean resources = true;

		/**
		 * @return the paths
		 */
		public List<String> getPaths() {
			return paths;
		}

		/**
		 * @param paths the paths to set
		 */
		public void setPaths(List<String> paths) {
			this.paths = paths;
		}

		/**
		 * @return the endpoints
		 */
		public boolean isEndpoints() {
			return endpoints;
		}

		/**
		 * @param endpoints the endpoints to set
		 */
		public void setEndpoints(boolean endpoints) {
			this.endpoints = endpoints;
		}

		/**
		 * @return the resources
		 */
		public boolean isResources() {
			return resources;
		}

		/**
		 * @param resources the resources to set
		 */

		public void setResources(boolean resources) {
			this.resources = resources;
		}

	}
}
