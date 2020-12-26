/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jul 4, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.tools.function;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.tools.actuator.ActuatorMapping;
import org.gocom.coframe.sdk.util.CofBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * 功能码扫描类（扫描获取 basePackage，再扫描这些 basePackage， 获取 ActuatorMapping 的信息（主要是
 * className, methodName, functionCodes
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class FunctionAnnotationScanner implements ResourceLoaderAware, ApplicationContextAware {
	// Spring容器注入
	private ApplicationContext applicationContext;
	// Spring容器注入
	private ResourceLoader resourceLoader;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * 扫描代码，从启动类或 AutoConguration 类型中的类中，找出所有配置的 basePackage。再扫描这些 basePackage,
	 * 从中找出配置有 ClassBoundFunctions 注解的类，或配置有 MethodAllowedFunctions 的方法，从而收集
	 * ActuatorMapping 的信息（主要是 className, methodName, functionCodes）
	 * 
	 * @return
	 * @param basePackages 需要额外扫描的包
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Set<ActuatorMapping> scanFunctionAnnotations(String... basePackages) throws IOException {
		Set<ActuatorMapping> mappings = new HashSet<>();
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);

		Set<String> packages = new HashSet<>();
		if (basePackages != null && basePackages.length > 0) {
			packages.addAll(Arrays.asList(basePackages).stream().filter(x -> x != null).collect(Collectors.toList()));
		}
		packages.addAll(getBasePackages());
		for (String pkg : packages) {
			mappings.addAll(scanPackage(resolver, metaReader, pkg));
		}
		return mappings;
	}

	/**
	 * 扫描提供的 basePackage, 从中找出配置有 ClassBoundFunctions 注解的类，或配置有
	 * MethodAllowedFunctions 的方法，从而收集 ActuatorMapping
	 * 
	 * @param resolver
	 * @param metaReader
	 * @param basePackage
	 * @return
	 * @throws IOException
	 */
	private Set<ActuatorMapping> scanPackage(ResourcePatternResolver resolver, MetadataReaderFactory metaReader, String basePackage) throws IOException {
		Map<String, ActuatorMapping> scanedMappings = new HashMap<>();
		basePackage = basePackage.replaceAll("\\.", "/");
		Resource[] resources = (Resource[]) resolver.getResources("classpath*:" + basePackage + "/**/*.class");
		for (Resource r : resources) {
			MetadataReader reader = metaReader.getMetadataReader(r);
			ClassMetadata classMetaData = reader.getClassMetadata();
			AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

			// 扫描 ClassBoundFunctions 注解, 获取功能信息
			scanAnnotationClassBoundFunctions(scanedMappings, annotationMetadata, classMetaData.getClassName());
			// 扫描 BoundFunctions 注解
			scanAnnotationBoundFunctions(scanedMappings, annotationMetadata, classMetaData.getClassName());
			// 扫描 BoundFunction 注解
			scanAnnotationBoundFunction(scanedMappings, annotationMetadata, classMetaData.getClassName());
//			// 扫描 RequestMapping 注解, 获取方法上的 httpMethod 与 url 信息
//			scanAnnotationRequestMapping(scanedMappings, annotationMetadata, classMetaData.getClassName());
		}
		return scanedMappings.values().stream().collect(Collectors.toSet());
	}

	/**
	 * 扫描类上的 ClassBoundFunctions 注解, 添加方法与功能的绑定
	 * 
	 * @param scanedMappings
	 * @param annotationMetadata
	 * @param className
	 */
	private void scanAnnotationClassBoundFunctions(Map<String, ActuatorMapping> scanedMappings, AnnotationMetadata annotationMetadata, String className) {
		MultiValueMap<String, Object> classAnnotationMap = annotationMetadata.getAllAnnotationAttributes(ClassBoundFunctions.class.getName());
		if (classAnnotationMap != null) {
			List<Object> classAnnotations = classAnnotationMap.get("value");
			if (classAnnotations != null && classAnnotations.size() > 0) {
				for (Object classAnnotation : classAnnotations) {
					AnnotationAttributes[] annotationAttributes = (AnnotationAttributes[]) classAnnotation;
					if (annotationAttributes != null && annotationAttributes.length > 0) {
						for (AnnotationAttributes annotationAttribute : annotationAttributes) {
							String methodName = annotationAttribute.getString("method");
							AnnotationAttributes[] boundFunctions = annotationAttribute.getAnnotationArray("functions");
							if (boundFunctions != null && boundFunctions.length > 0) {
								addMapping(scanedMappings, buildActuatorMapping(className, methodName, boundFunctions));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 扫描类上的 BoundFunctions 注解, 添加方法与功能的绑定
	 * 
	 * @param scanedMappings
	 * @param annotationMetadata
	 * @param className
	 */
	private void scanAnnotationBoundFunctions(Map<String, ActuatorMapping> scanedMappings, AnnotationMetadata annotationMetadata, String className) {
		Set<MethodMetadata> boundFunctionsMetadata = annotationMetadata.getAnnotatedMethods(BoundFunctions.class.getName());
		if (boundFunctionsMetadata != null && boundFunctionsMetadata.size() > 0) {
			for (MethodMetadata methodMetadata : boundFunctionsMetadata) {
				String methodName = methodMetadata.getMethodName();
				MultiValueMap<String, Object> methodAnnotationMap = methodMetadata.getAllAnnotationAttributes(BoundFunctions.class.getName());
				List<Object> boundFunctions = methodAnnotationMap.get("functions");
				if (boundFunctions != null && boundFunctions.size() > 0) {
					for (Object item : boundFunctions) {
						addMapping(scanedMappings, buildActuatorMapping(className, methodName, ((AnnotationAttributes) item)));
					}
				}
			}
		}
	}

	/**
	 * 扫描类上的 BoundFunction 注解, 添加方法与功能的绑定
	 * 
	 * @param scanedMappings
	 * @param annotationMetadata
	 * @param className
	 */
	private void scanAnnotationBoundFunction(Map<String, ActuatorMapping> scanedMappings, AnnotationMetadata annotationMetadata, String className) {
		Set<MethodMetadata> boundFunctionMetadata = annotationMetadata.getAnnotatedMethods(BoundFunction.class.getName());
		if (boundFunctionMetadata != null) {
			for (MethodMetadata methodMetadata : boundFunctionMetadata) {
				String methodName = methodMetadata.getMethodName();
				MultiValueMap<String, Object> methodAnnotationMap = methodMetadata.getAllAnnotationAttributes(BoundFunction.class.getName());

				ActuatorMapping mapping = new ActuatorMapping();
				mapping.setClassName(className);
				mapping.setMethodName(methodName);
				CofFunction function = new CofFunction();
				function.setName((String) methodAnnotationMap.getFirst("name"));
				function.setCode((String) methodAnnotationMap.getFirst("code"));
				mapping.getFunctions().add(function);
				addMapping(scanedMappings, mapping);
			}
		}
	}

//	/**
//	 * 扫描类上的 RequestMapping 注解, 添加方法与rest api的绑定
//	 * @param scanedMappings
//	 * @param annotationMetadata
//	 * @param className
//	 */
//	private void scanAnnotationRequestMapping(Map<String, ActuatorMapping> scanedMappings, AnnotationMetadata annotationMetadata, String className) {
//		Set<MethodMetadata> restApiMetadata = annotationMetadata.getAnnotatedMethods(RequestMapping.class.getName());
//		if (restApiMetadata != null) {
//			for (MethodMetadata methodMetadata : restApiMetadata) {
//				String methodName = methodMetadata.getMethodName();
//				MultiValueMap<String, Object> methodAnnotationMap = methodMetadata.getAllAnnotationAttributes(RequestMapping.class.getName());
//
//				ActuatorMapping mapping = new ActuatorMapping();
//				mapping.setClassName(className);
//				mapping.setMethodName(methodName);
//				RequestMethod[] methods = (RequestMethod[]) methodAnnotationMap.getFirst("method");
//				if (methods.length > 0) {
//					mapping.getHttpMethod().add(methods[0].name());
//				} else { // method如果不写, 同时支持 get 与 post
//					mapping.getHttpMethod().add(RequestMethod.GET.name());
//					mapping.getHttpMethod().add(RequestMethod.POST.name());
//				}
//				String[] paths = (String[]) methodAnnotationMap.getFirst("value");
//				if (paths.length > 0) {
//					mapping.setPath(paths[0]);
//				}
//				addMapping(scanedMappings, mapping);
//			}
//		}
//	}

	/**
	 * 添加映射
	 * 
	 * @param scanedMappings
	 * @param scanedMapping
	 */
	private void addMapping(Map<String, ActuatorMapping> scanedMappings, ActuatorMapping scanedMapping) {
		String key = key(scanedMapping);
		ActuatorMapping mapping = scanedMappings.get(key);
		if (mapping == null) {
			mapping = scanedMapping;
			scanedMappings.put(key, mapping);
		} else {
			scanedMapping.getFunctions().addAll(mapping.getFunctions()); // 先把目标中的function全拷过来. 以防止拷贝的时候被冲掉了
			CofBeanUtils.copyNotNullProperties(scanedMapping, mapping);
		}
	}

	/**
	 * 映射的key
	 * 
	 * @param mapping
	 * @return
	 */
	private String key(ActuatorMapping mapping) {
		return mapping.getClassName() + "." + mapping.getMethodName();
	}

	/**
	 * 构建映射
	 * 
	 * @param className
	 * @param methodName
	 * @param attributesArray
	 * @return
	 */
	private ActuatorMapping buildActuatorMapping(String className, String methodName, AnnotationAttributes... attributesArray) {
		ActuatorMapping mapping = new ActuatorMapping();
		mapping.setClassName(className);
		mapping.setMethodName(methodName);
		for (AnnotationAttributes attributes : attributesArray) {
			CofFunction function = new CofFunction();
			function.setName(attributes.getString("name"));
			function.setCode(attributes.getString("code"));
			mapping.getFunctions().add(function);
		}
		return mapping;
	}

	/**
	 * 找出所有的配置有 ComponentScan 注解，或 ComponentScans 注解的 bean 上配置的 basePacakges 信息
	 * 
	 * @return
	 */
	public Set<String> getBasePackages() {
		Set<String> basePackages = new HashSet<>();
		// 查找所有配置有ComponentScan注解的 bean
		applicationContext.getBeansWithAnnotation(ComponentScan.class).forEach((name, instance) -> {
			// 找出类上的 ComponentScan 注解
			Set<ComponentScan> scanSet = AnnotatedElementUtils.findAllMergedAnnotations(instance.getClass(), ComponentScan.class);
			// 找出其中的 basePackages
			for (ComponentScan scan : scanSet) {
				String[] packages = scan.basePackages();
				if (packages != null && packages.length > 0) {
					basePackages.addAll(Arrays.asList(packages));
				}
			}
		});
		// 查找所有配置有ComponentScans注解的 bean
		applicationContext.getBeansWithAnnotation(ComponentScans.class).forEach((name, instance) -> {
			// 找出类上的 ComponentScans 注解
			Set<ComponentScans> scansSet = AnnotatedElementUtils.findAllMergedAnnotations(instance.getClass(), ComponentScans.class);
			// 找出其中的 basePackages
			for (ComponentScans scans : scansSet) {
				ComponentScan[] scanArray = scans.value();
				if (scanArray != null && scanArray.length > 0) {
					for (ComponentScan scan : scanArray) {
						String[] packages = scan.basePackages();
						if (packages != null && packages.length > 0) {
							basePackages.addAll(Arrays.asList(packages));
						}
					}
				}
			}
		});
		return basePackages;
	}
}
