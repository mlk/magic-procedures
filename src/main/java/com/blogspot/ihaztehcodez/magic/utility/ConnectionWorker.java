package com.blogspot.ihaztehcodez.magic.utility;

import java.sql.SQLException;

/**
 * A connection worker executes a chunk of SQL.
 */
public interface ConnectionWorker {
    void execute(SQLWorker worker) throws SQLException;
}
