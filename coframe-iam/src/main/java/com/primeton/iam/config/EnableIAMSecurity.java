/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月11日 10:21:31
 ******************************************************************************/

package com.primeton.iam.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author huzd@primeton.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({IAMSecurityConfiguration.class,
        IAMResourceServerConfiguration.class,
        IAMFormLoginConfiguration.class})
public @interface EnableIAMSecurity {
}
