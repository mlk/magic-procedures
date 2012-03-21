package com.github.mlk.magic.utility;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Executes a chunk of SQL.
 * This is to allow hibernate to work without rewriting the handler.
 */
public interface SQLWorker {
    public void work(Connection connection) throws SQLException;
}
