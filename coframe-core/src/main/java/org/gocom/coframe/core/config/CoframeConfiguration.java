/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 5, 2019
 *******************************************************************************/
package org.gocom.coframe.core.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.cache.MemoryCache;
import org.gocom.coframe.core.cache.NoCache;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * coframe 配置加载
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Configuration
@EnableAspectJAutoProxy
public class CoframeConfiguration {
	private Logger logger = LoggerFactory.getLogger(CoframeConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public PhysicalNamingStrategy physicalNamingStrategy() {
		return new SpringPhysicalNamingStrategy() {
			protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
				return false;
			}
		};
	}

	@Bean
	@ConditionalOnProperty(name = CoframeConstants.PROPERTY_PREFIX + "bean-enabled.hibernate5-module", havingValue = "true", matchIfMissing = true)
	public Module coframeDatatypeHibernateModule() {
		logger.info("Load coframe hibernate5Module config");
		Hibernate5Module module = new Hibernate5Module();
		module.configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
		// 为了让 @Transient 的属性也会被JSON序列化
		module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
		return module;
	}

	@Bean
	@ConditionalOnProperty(name = CoframeConstants.PROPERTY_PREFIX + "bean-enabled.multipart-config-factory", havingValue = "true", matchIfMissing = true)
	public MultipartConfigElement coframeMultipartConfigElement() {
		logger.info("Load coframe multipartConfig config");
		MultipartConfigFactory factory = new MultipartConfigFactory();
		String location = System.getProperty("user.dir") + "/data/tmp";
		File tmpFile = new File(location);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		factory.setLocation(location);
		return factory.createMultipartConfig();
	}

	@Bean
	@ConditionalOnProperty(name = CoframeConstants.PROPERTY_PREFIX + "bean-enabled.swagger-docket", havingValue = "true", matchIfMissing = true)
	public Docket coframeRestApi() {
		logger.info("Load coframe swagger config");
		return new Docket(DocumentationType.SWAGGER_2) //
				.apiInfo(apiInfo()) //
				.select()//
				.apis(RequestHandlerSelectors.basePackage("org.gocom.coframe.controller")) //
				.paths(PathSelectors.any()) //
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Coframe API").description(
				"This is a sample server coframe server. You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/). For this sample, you can use the api key `special-key` to test the authorization filters.")
				.version("1.0.0").termsOfServiceUrl("http://swagger.io/terms/").contact(new Contact("qinsc", "", "qinsc@primeton.com")).build();
	}

	@Bean
	@ConditionalOnProperty(name = CoframeConstants.PROPERTY_PREFIX + "bean-enabled.local-validator-factory-bean", havingValue = "true", matchIfMissing = true)
	public LocalValidatorFactoryBean coframeDefaultValidator() {
		logger.info("Load coframe localValidatorFactoryBean config");
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/validation/Messages");
		messageSource.setDefaultEncoding("UTF-8");
		factoryBean.setValidationMessageSource(messageSource);
		return factoryBean;
	}

	@Bean
	@ConditionalOnProperty(name = "coframe.cache.enabled", havingValue = "false", matchIfMissing = true)
    public ICacheManager noCacheBean(){
        logger.info("Cache is not enabled or type is not supported, use no cache.");
	    return new NoCache();
    }
	
	@Bean
	@ConditionalOnExpression("${coframe.cache.enabled:true} && '${coframe.cache.type}'.equals('memory')")
	public ICacheManager cofMemoryCacheManager(StringRedisTemplate redisTemplate) {
		return new MemoryCache();
	}
}
