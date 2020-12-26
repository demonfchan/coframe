/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Apr 4, 2019
 *******************************************************************************/
package org.gocom.coframe.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gocom.coframe.sdk.aspect.AbstractCofFunctionCheckAspect;
import org.springframework.stereotype.Component;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Aspect
@Component
public class CoframeFunctionCheckAspect extends AbstractCofFunctionCheckAspect {
	@Pointcut("!execution(public * org.gocom.coframe.controller..*.find*(..))" // 不拦截以 find 开头的方法
			+ " && !execution(public * org.gocom.coframe.controller..*.get*(..))" // 不拦截以 get 开头的方法
			+ " && !execution(public * org.gocom.coframe.controller..*.query*(..))" // 不拦截以 query 开头的方法
			+ " && !execution(public * org.gocom.coframe.controller..*.paging*(..))" // 不拦截以 paging 开头的方法
			+ " && !execution(public * org.gocom.coframe.controller..*.list*(..))" // 不拦截以 list 开头的方法
			+ " && !execution(public * org.gocom.coframe.controller.DictTypeController.exportExcelAll(..))" // 不拦截字典导出
			+ " && !execution(public * org.gocom.coframe.controller.DictTypeController.downloadDictTemplate(..))" // 不拦截下载字典模板
			+ " && execution(public * org.gocom.coframe.controller..*.*(..))" // 拦截其它public的方法
	)
	public void controllerPointCut() {
	}

	@Pointcut("!execution(public * org.gocom.coframe.core.controller..*.find*(..))" // 不拦截以 find 开头的方法
			+ " && !execution(public * org.gocom.coframe.core.controller..*.get*(..))" // 不拦截以 get 开头的方法
			+ " && !execution(public * org.gocom.coframe.core.controller..*.query*(..))" // 不拦截以 query 开头的方法
			+ " && !execution(public * org.gocom.coframe.core.controller..*.paging*(..))" // 不拦截以 paging 开头的方法
			+ " && !execution(public * org.gocom.coframe.core.controller..*.list*(..))" // 不拦截以 list 开头的方法
			+ " && execution(public * org.gocom.coframe.core.controller..*.*(..))" // 拦截其它public的方法
	)
	public void coreControllerPointCut() {
	}

	@Pointcut("execution(public * org.gocom.coframe.controller.OperationLogController.*(..))") // 拦截操作日志的所有方法
	public void operationLogPointCut() {
	}

	// controller后比一般的多加了一个点, 使它可以拦截子孙包中的方法
	@Before("coreControllerPointCut() or controllerPointCut() or operationLogPointCut()")
	private void check(JoinPoint joinPoint) {
		super.checkPermissions(joinPoint);
	}
}
