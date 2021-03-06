package com.github.mlk.magic.binders;

import com.github.mlk.magic.Binding;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class VoidBinding implements Binding {

    @Override
    public Object getValue(CallableStatement statement, int index)
            throws SQLException {
        return null;
    }

    @Override
    public void prepareGet(CallableStatement statement, int index)
            throws SQLException {
    }

    @Override
    public void setValue(CallableStatement statement, int index, Object value)
            throws SQLException {
    }

    @Override
    public Class<?>[] worksWith() {
        return new Class<?>[]{void.class};
    }

    @Override
    public int parameters() {
        return 0;
    }

}
