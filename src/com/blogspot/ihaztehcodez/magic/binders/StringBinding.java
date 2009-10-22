package com.blogspot.ihaztehcodez.magic.binders;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.blogspot.ihaztehcodez.magic.Binding;

public class StringBinding implements Binding {

	@Override
	public Object getValue(CallableStatement statement, int index) throws SQLException {
		return statement.getString(index);
		
	}

	@Override
	public void prepareGet(CallableStatement statement, int index) throws SQLException {
		statement.registerOutParameter(index, Types.INTEGER);		
	}

	@Override
	public void setValue(CallableStatement statement, int index, Object value) throws SQLException {
		statement.setString(index, ((String)value));
	}

	@Override
	public Class<?>[] worksWith() {
		return new Class[] { String.class };
	}
}
