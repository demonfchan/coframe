/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 26, 2019
 *******************************************************************************/
package org.gocom.coframe.core.support;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
public class CoframeValidationGroups {
	public interface Create extends javax.validation.groups.Default {
	}

	public interface Update extends javax.validation.groups.Default {
	}

	public interface Delete extends javax.validation.groups.Default {
	}

	public interface Association {
	}
}
