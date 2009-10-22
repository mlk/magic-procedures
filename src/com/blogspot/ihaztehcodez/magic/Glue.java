package com.blogspot.ihaztehcodez.magic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/** The Glue is what sticks an interface to the -
 *
 */
public class Glue {
	private final InvocationHandler handler;
	
	public Glue(final InvocationHandler handler) {
		if (handler == null) {
			throw new NullPointerException("handler is null");
		}
		this.handler = handler;
	}
	
	public <T> T toDatabasePackage(Class<T> clazz) {
		validateisInterface(clazz);
		
		validateNoParentage(clazz);
		
		Method[] methods = clazz.getDeclaredMethods();
		
		validateHasMethods(clazz, methods);
		validateAllMethodsHaveAnnotation(clazz, methods);
		validateAllMethodsThrowException(clazz, methods);
		
		return clazz.cast(Proxy.newProxyInstance(getClass().getClassLoader(), 
				new Class<?>[] {clazz}, handler));
	}

	private <T> void validateAllMethodsThrowException(Class<T> clazz, Method[] methods) {
		StringBuilder methodNames = new StringBuilder();
		for (final Method method : methods) {
			Class<?>[] exceptionTypes = method.getExceptionTypes();
			if (exceptionTypes.length != 1 
					|| exceptionTypes[0].getClass().equals(SQLException.class)) {
				methodNames.append(method.getName()).append(", ");
			}
		}
		if (methodNames.length() > 0) {
			throw new IllegalArgumentException(clazz.getName() + " has the following methods"
					+ " without the required sql exception: " + methodNames.substring(0, 
							methodNames.length() - 2));
		}
	}
	
	private <T> void validateAllMethodsHaveAnnotation(Class<T> clazz, Method[] methods) {
		StringBuilder methodNames = new StringBuilder();
		for (final Method method : methods) {
			if (method.getAnnotation(DatabaseScript.class) == null) {
				methodNames.append(method.getName()).append(", ");
			}
		}
		if (methodNames.length() > 0) {
			throw new IllegalArgumentException(clazz.getName() + " has the following methods"
					+ " without the required annotation: " + methodNames.substring(0, 
							methodNames.length() - 2));
		}
	}

	private <T> void validateNoParentage(Class<T> clazz) {
		if (clazz.getInterfaces().length > 0) {
			throw new IllegalArgumentException(clazz.getName() + " extends an interface.");
		}
	}

	private <T> void validateHasMethods(Class<T> clazz, Method[] methods) {
		if (methods.length == 0) {
			throw new IllegalArgumentException(clazz.getName() + " has no methods");
		}
	}

	private <T> void validateisInterface(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException(clazz.getName() + " is not an interface");
		}
	}

}
