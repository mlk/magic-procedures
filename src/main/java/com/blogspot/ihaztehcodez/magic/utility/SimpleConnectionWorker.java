package com.blogspot.ihaztehcodez.magic.utility;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleConnectionWorker implements ConnectionWorker {
	private Connection connection;
	
	public SimpleConnectionWorker(Connection connection) {
		this.connection = connection;
	}

    @Override
    public void execute(SQLWorker worker) throws SQLException {
        worker.work(connection);
    }
}
