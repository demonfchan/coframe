/**
 * 
 */
package org.gocom.coframe.sdk.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * TODO 此处填写 class 信息
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */

public class CofBeanUtils {
	/**
	 * 往目标对象复制同名的非空属性
	 * 
	 * @param src
	 * @param target
	 */
	public static <T> T copyNotNullProperties(Object src, T target) {
		BeanUtils.copyProperties(src, target, getNullProperties(src));
		return target;
	}

	/**
	 * 查出值为 null 的属性的名称数组
	 * 
	 * @param src
	 * @return
	 */
	private static String[] getNullProperties(Object src) {
		BeanWrapper srcBean = new BeanWrapperImpl(src);
		PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
		List<String> nullProperties = new ArrayList<String>();
		for (PropertyDescriptor p : pds) {
			Object srcValue = srcBean.getPropertyValue(p.getName());
			if (srcValue == null) {
				nullProperties.add(p.getName());
			}
		}
		String[] result = new String[nullProperties.size()];
		return nullProperties.toArray(result);
	}
}
