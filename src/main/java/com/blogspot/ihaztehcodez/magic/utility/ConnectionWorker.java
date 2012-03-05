package com.blogspot.ihaztehcodez.magic.utility;

import java.sql.SQLException;

public interface ConnectionWorker {
	void execute(SQLWorker worker) throws SQLException;
}
