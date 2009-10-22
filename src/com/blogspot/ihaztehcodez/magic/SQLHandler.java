package com.blogspot.ihaztehcodez.magic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.blogspot.ihaztehcodez.magic.utility.ConnectionProvider;
import com.sun.swing.internal.plaf.metal.resources.metal;

class SQLHandler implements InvocationHandler {
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private final Map<Class<?>, Binding> bindings = new HashMap<Class<?>, Binding>();
	private final ConnectionProvider connectionProvider;
	public SQLHandler(final List<Binding> bindings, 
			final ConnectionProvider connectionProvider) {
		
		for (Binding binding : bindings) {
			for(Class<?> clazz : binding.worksWith()) {
				this.bindings.put(clazz, binding);
			}
		}
		
		if (connectionProvider == null) {
			throw new NullPointerException("connectionProvider");
		}
		this.connectionProvider = connectionProvider;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		DatabaseScript script = method.getAnnotation(DatabaseScript.class);
		int currentBinding = 1;
		Binding returnBinding = bindings.get(method.getReturnType());
		List<Binding> paramBindings = new ArrayList<Binding>();
		for (Class<?> paramClazz : method.getParameterTypes()) {
			paramBindings.add(bindings.get(paramClazz));
		}
		
		Connection connection = connectionProvider.get();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall(script.value());
			returnBinding.prepareGet(statement, 1);
			currentBinding += returnBinding.parameters();
			
			for (int i = 0; i<paramBindings.size(); i++) {
				
				paramBindings.get(i).setValue(statement, currentBinding, args[i]);
				currentBinding+=paramBindings.get(i).parameters();
			}
			
			statement.execute();
			
			
			
			return returnBinding.getValue(statement, 1);
		} finally {
			close(statement);
			close(connection);
		}
	}

	private void close(Connection s) {
		if (s == null) {
			return;
		}
		try {
			s.close();
		} catch (SQLException sqle) {
			log.log(Level.WARNING, "Failed to close connection", sqle);
		}
	}
		
	private void close(Statement s) {
		if (s == null) {
			return;
		}
		try {
			s.close();
		} catch (SQLException sqle) {
			log.log(Level.WARNING, "Failed to close statement", sqle);
		}
	}
}
