package com.github.mlk.magic;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * This class handles bindings
 *
 * @author <a href="http://tinyurl.com/magic-procedures">Mlk</a>
 */
public interface Binding {

    /**
     * Sets the parameter.
     */
    void setValue(CallableStatement statement, int index, Object value) throws SQLException;

    /**
     * Sets up a statement to be able to return a value.
     */
    void prepareGet(CallableStatement statement, int index) throws SQLException;

    /**
     * Retrieves a value from a statement.
     */
    Object getValue(CallableStatement statement, int index) throws SQLException;

    /**
     * @return The types this binding can bind.
     */
    Class<?>[] worksWith();

    /**
     * @return The number of parameters this uses to retrieve the content.
     */
    int parameters();
}
