package com.blogspot.ihaztehcodez.magic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

/** The Glue is what sticks an interface to the database.
 * 
 * Note: The end goal is to have this hidden behind DI framework specific binders.
 */
public class Glue {
	/** The handler that turns a method into a database procedure. */
	private final InvocationHandler handler;
	
	/** @param handler The handler that turns a method into a database procedure.  */
	public Glue(final InvocationHandler handler) {
		if (handler == null) {
			throw new NullPointerException("handler is null");
		}
		this.handler = handler;
	}
	
	/** Glues an interface to the database package it should be attached too.
	 * This interface to be glued has the follow restrictions placed on it:
	 * It must have some methods to bind.
	 * All methods must have the {@link DatabaseScript} annotation.
	 * All methods must throw {@link SQLException}.
	 * The interface can not extend anything.
	 *
     * @param clazz The interface to bind.
	 * @return Glued interface.
	 */
	public <T> T toDatabasePackage(final Class<T> clazz) {
		validateIsInterface(clazz);
		
		validateNoParentage(clazz);
		
		Method[] methods = clazz.getDeclaredMethods();
		
		validateHasMethods(clazz, methods);
		validateAllMethodsHaveAnnotation(clazz, methods);
		validateAllMethodsThrowException(clazz, methods);
		
		return clazz.cast(Proxy.newProxyInstance(getClass().getClassLoader(), 
				new Class<?>[] {clazz}, handler));
	}

	/** This validates that all methods throw {@link SQLException}.
	 * 
	 * @param clazz The interface to validate.
	 * @param methods The methods to validate.
	 */
	private <T> void validateAllMethodsThrowException(final Class<T> clazz, 
			final Method[] methods) {
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
	
	/** This validates that all the methods have the {@link DatabaseScript} annotation.
	 * 
	 * @param clazz The interface to validate.
	 * @param methods The methods to validate.
	 */
	private <T> void validateAllMethodsHaveAnnotation(final Class<T> clazz, 
			final Method[] methods) {
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

	/** Validates that the interface does not extend any other interfaces.
	 * 
	 * @param clazz The interface to validate.
	 */
	private <T> void validateNoParentage(final Class<T> clazz) {
		if (clazz.getInterfaces().length > 0) {
			throw new IllegalArgumentException(clazz.getName() + " extends an interface.");
		}
	}

	/** Validates that this interface has some methods to glue.
	 * 
	 * @param clazz  The interface to validate.
	 * @param methods The methods to validate.
	 */
	private <T> void validateHasMethods(final Class<T> clazz, final Method[] methods) {
		if (methods.length == 0) {
			throw new IllegalArgumentException(clazz.getName() + " has no methods");
		}
	}

	/** Validate that this really is an interface.
	 * 
	 * @param clazz The interface (hopefully!) to validate.
	 */
	private <T> void validateIsInterface(final Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException(clazz.getName() + " is not an interface");
		}
	}

}
