/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:27 PM
 */
package org.gocom.coframe.sdk.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.annotation.ClassBoundFunctions;
import org.gocom.coframe.sdk.annotation.BoundFunction;
import org.gocom.coframe.sdk.annotation.BoundFunctions;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * 抽像的功能权限监测类
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public abstract class AbstractCofFunctionCheckAspect {
	private static Logger log = LoggerFactory.getLogger(AbstractCofFunctionCheckAspect.class);

	/**
	 * 权限检测
	 * 
	 * @param joinPoint
	 */
	@SuppressWarnings("unchecked")
	public void checkPermissions(JoinPoint joinPoint) {
		// 获取当前登陆用户拥有的权限编码
		Set<String> userAllowedfunctions = (Set<String>) CofContext.getContext().get(CofConstants.CONTEXT_LOGINED_USER_FUNCTION_CODES);
		// 取不到，则用户未登陆
		if (userAllowedfunctions == null) {
			throw CofErrorCode.USER_NOT_LOGIN.runtimeException();
		}

		// 取方法上定义的权限编码
		String className = joinPoint.getTarget().getClass().getName();
//		String realCallClassName = joinPoint.getSignature().getDeclaringTypeName();
		String method = joinPoint.getSignature().getName();
		List<String> methedAllowedfunctions = null;
		try {
			methedAllowedfunctions = getMethedAllowedFunctionCodes(className, method);
		} catch (Exception e) {
			log.error("Get function codes on method for " + className + "." + method + " failed, msg is " + e.getLocalizedMessage());
			throw CofErrorCode.NO_PERMISSION_ON_MRTHOD.runtimeException(method);
		}

		// 检查当前用户是否有执行此方法的权限
		if (methedAllowedfunctions != null && methedAllowedfunctions.size() > 0) {
			boolean hasPermission = false;
			for (String configuredFunctionCode : methedAllowedfunctions) {
				if (userAllowedfunctions.contains(configuredFunctionCode)) {
					hasPermission = true;
					break;
				}
			}
			if (!hasPermission) {
				log.warn("Logined user has no invoke permissions on " + className + "." + method);
				throw CofErrorCode.NO_PERMISSION_ON_MRTHOD.runtimeException(method);
			}
		}
	}

	/**
	 * 从注解中获取配置在方法上的权限码
	 * 
	 * @param className  目标类名
	 * @param methodName 方法名
	 * @return
	 * @throws Exception
	 */
	private List<String> getMethedAllowedFunctionCodes(String className, String methodName) throws Exception {
		Class<?> clazz = Class.forName(className);
		String clazz_name = clazz.getName();
		ClassPool pool = ClassPool.getDefault();
		ClassClassPath classPath = new ClassClassPath(clazz);
		pool.insertClassPath(classPath);

		List<String> codes = new ArrayList<String>();
		CtClass ctClass = pool.get(clazz_name);
		CtMethod ctMethod = null;

		// 先尝试从目标类中去取找这个方法
		try {
			ctMethod = ctClass.getDeclaredMethod(methodName);
		} catch (Exception e) {
		}
		// 先从方法上去找权限定义注解
		if (ctMethod != null) {
			// 取 MethodAllowedFunctions 定义
			if (ctMethod.hasAnnotation(BoundFunctions.class)) {
				BoundFunction[] functions = ((BoundFunctions) ctMethod.getAnnotation(BoundFunctions.class)).functions();
				if (functions != null) {
					Arrays.asList(functions).stream().forEach(function -> {
						codes.add(function.code());
					});
				}
			}
			// 取 MethodAllowedFunction 定义
			if (ctMethod.hasAnnotation(BoundFunction.class)) {
				codes.add(((BoundFunction) ctMethod.getAnnotation(BoundFunction.class)).value());
			}
			if (codes.size() > 0) {
				return codes;
			}
		}
		// 找不到目标方法，则方法可能在父类中, 此时通过取类上的权限定义去进行控制
		if (ctClass.hasAnnotation(ClassBoundFunctions.class)) {
			ClassBoundFunctions classBoundFunctions = (ClassBoundFunctions) ctClass.getAnnotation(ClassBoundFunctions.class);
			if (classBoundFunctions != null) {
				BoundFunctions[] methodAllowedFunctions = classBoundFunctions.value();
				if (methodAllowedFunctions != null) {
					Arrays.asList(methodAllowedFunctions).stream().filter(x -> methodName.equals(x.method())).forEach(methodAllowedFunction -> {
						Arrays.asList(methodAllowedFunction.functions()).stream().forEach(function -> {
							codes.add(function.value());
						});
					});
				}
			}
		}
		return codes;
	}
}
