/*******************************************************************************
  * Copyright (c) 2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.common.util;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * 
 * @author V. Kabanovich
 *
 */
public class BeanUtil {
	public static final String GET = "get"; //$NON-NLS-1$
	public static final String SET = "set"; //$NON-NLS-1$
	public static final String IS = "is"; //$NON-NLS-1$

	public static boolean isGetter(String methodName, int numberOfParameters) {
		return (((methodName.startsWith(GET) && !methodName.equals(GET))
					|| (methodName.startsWith(IS) && !methodName.equals(IS)))
				&& numberOfParameters == 0);
	}

	public static boolean isSetter(String methodName, int numberOfParameters) {
		return (((methodName.startsWith(SET) && !methodName.equals(SET)))
				&& numberOfParameters == 1);
	}

	public static boolean isGetter(IMethod method) {
		return method != null && isGetter(method.getElementName(), method.getNumberOfParameters())
			&& checkPropertyReturnType(method);
	}

	
	public static boolean checkPropertyReturnType(String typeName, String methodName) {
		if(typeName == null || typeName.equals("void")) { //$NON-NLS-1$
			return false;
		}
		if(methodName.startsWith(BeanUtil.IS)) {
			if(!"boolean".equals(typeName) && !"java.lang.Boolean".equals(typeName)) { //$NON-NLS-1$ //$NON-NLS-2$
				return false;
			}			
		}
		return true;
	}

	private static boolean checkPropertyReturnType(IMethod method) {
		return method != null && checkPropertyReturnType(EclipseJavaUtil.getMemberTypeAsString(method), method.getElementName());
	}

	public static boolean isSetter(IMethod method) {
		return method != null && isSetter(method.getElementName(), method.getNumberOfParameters());
	}

	public static String getPropertyName(String methodName) {
		if(isGetter(methodName, 0) || isSetter(methodName, 1)) {
			StringBuffer name = new StringBuffer(methodName);
			if(methodName.startsWith(IS)) {
				name.delete(0, 2);
			} else {
				name.delete(0, 3);
			}
			if(name.length() < 2 || !Character.isUpperCase(name.charAt(1))) {
				name.setCharAt(0, Character.toLowerCase(name.charAt(0)));
			}
			return name.toString();
		}
		return null;
	}
	
	/**
	 * Converts Java Class Name to name of Bean
	 * 
	 * @param className is short name or fully qualified name
	 * @return Bean Name
	 */
	public static String getDefaultBeanName(String className){
		int lastDotPosition = className.lastIndexOf("."); //$NON-NLS-1$
		if(lastDotPosition >= 0 && className.length() > lastDotPosition){
			className = className.substring(lastDotPosition+1);
		}
		if(className.length() > 0) {
			className = className.substring(0, 1).toLowerCase() + className.substring(1);
		}
		return className;
	}
	
	/**
	 * Returns name of Bean for the given IType
	 * @param type
	 * @return Bean Name
	 */
	public static String getDefaultBeanName(IType type){
		return getDefaultBeanName(type.getElementName());
	}

}
