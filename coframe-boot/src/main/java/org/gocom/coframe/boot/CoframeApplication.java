/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 1, 2019
 *******************************************************************************/
package org.gocom.coframe.boot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableDiscoveryClient
public class CoframeApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoframeApplication.class);
	}

	 @Override
	    public void onStartup(ServletContext servletContext)
	            throws ServletException {
	        servletContext.getSessionCookieConfig().setName("SESSIONID");
	    }
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(CoframeApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter());
		springApplication.run(args);
	}
}
