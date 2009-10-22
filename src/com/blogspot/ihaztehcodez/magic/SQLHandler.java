package com.blogspot.ihaztehcodez.magic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
		Binding returnBinding = bindings.get(method.getReturnType());
		Connection connection = connectionProvider.get();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall(script.value());
			returnBinding.prepareGet(statement, 1);
			
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
