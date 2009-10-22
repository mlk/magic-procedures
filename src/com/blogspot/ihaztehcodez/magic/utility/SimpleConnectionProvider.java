package com.blogspot.ihaztehcodez.magic.utility;

import java.sql.Connection;

public class SimpleConnectionProvider implements ConnectionProvider {
	private Connection connection;
	
	public SimpleConnectionProvider(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection get() {
		return connection;
	}
}
