package com.blogspot.ihaztehcodez.magic.binders;

import com.blogspot.ihaztehcodez.magic.Binding;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class IntBinding implements Binding {

    @Override
    public Object getValue(CallableStatement statement, int index) throws SQLException {
        int value = statement.getInt(index);
        if (statement.wasNull()) {
            return null;
        } else {
            return value;
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
            statement.setInt(index, (Integer) value);
        }
    }

    @Override
    public Class<?>[] worksWith() {
        return new Class<?>[]{Integer.class, int.class};
    }

    @Override
    public int parameters() {
        return 1;
    }

}
