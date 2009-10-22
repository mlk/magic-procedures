package com.blogspot.ihaztehcodez.magic.binders;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.blogspot.ihaztehcodez.magic.Binding;

public class ByteBinding implements Binding {

	@Override
	public Object getValue(CallableStatement statement, int index) throws SQLException {
		byte value = statement.getByte(index);
		if (statement.wasNull()) {
			return null;
		} else {
			return Byte.valueOf(value);
		}
	}

	@Override
	public void prepareGet(CallableStatement statement, int index) throws SQLException {
		statement.registerOutParameter(index, Types.INTEGER);		
	}

	@Override
	public void setValue(CallableStatement statement, int index, Object value) throws SQLException {
		if (value == null) {
			statement.setNull(index, Types.INTEGER);
		} else {
			statement.setByte(index, ((Byte)value).byteValue());
		}
	}

	@Override
	public Class<?>[] worksWith() {
		return new Class<?>[] { Byte.class };
	}
}
