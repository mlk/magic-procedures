package com.blogspot.ihaztehcodez.magic.utility;

import com.google.common.base.Supplier;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The simplest form of connection worker.
 */
public class SimpleConnectionWorker implements ConnectionWorker {
    private Supplier<Connection> connection;

    public SimpleConnectionWorker(Supplier<Connection> connection) {
        this.connection = connection;
    }

    @Override
    public void execute(SQLWorker worker) throws SQLException {
        worker.work(connection.get());
    }
}
