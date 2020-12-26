package com.primeton.iam.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;


public class FrameOptionsAllowFromStrategy implements AllowFromStrategy{

	@Override
	public String getAllowFromValue(HttpServletRequest request) {
		String uri = request.getRequestURI();
        
        if (uri.endsWith("/")) {
        	return XFrameOptionsHeaderWriter.XFrameOptionsMode.ALLOW_FROM.name();
        }
		return XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY.name();
	}

}
