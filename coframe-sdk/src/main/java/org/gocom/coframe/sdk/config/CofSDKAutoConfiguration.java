package org.gocom.coframe.sdk.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.gocom.coframe.sdk" })
public class CofSDKAutoConfiguration {
	// 用于配置 spring boot 的 EnableAutoConfiguration, 用于支持 starter 的方式引入sdk
}
