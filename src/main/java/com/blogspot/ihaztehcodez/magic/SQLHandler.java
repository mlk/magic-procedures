package com.blogspot.ihaztehcodez.magic;

import com.blogspot.ihaztehcodez.magic.utility.ConnectionWorker;
import com.blogspot.ihaztehcodez.magic.utility.SQLWorker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes the SQL specified in the database script parameter for the current method call.
 */
class SQLHandler implements InvocationHandler {
    private final Logger log = Logger.getLogger(getClass().getName());

    /**
     * Describes how to make use of a parameter or return type.
     */
    private final Map<Class<?>, Binding> bindings = new HashMap<Class<?>, Binding>();

    /**
     * Provides access to the database.
     */
    private final ConnectionWorker connectionProvider;

    /**
     * @param bindings           Describes how to make use of a parameter or return type.
     * @param connectionProvider Provides access to the database.
     */
    public SQLHandler(final List<Binding> bindings,
                      final ConnectionWorker connectionProvider) {

        for (Binding binding : bindings) {
            for (Class<?> clazz : binding.worksWith()) {
                this.bindings.put(clazz, binding);
            }
        }

        if (connectionProvider == null) {
            throw new NullPointerException("connectionProvider");
        }
        this.connectionProvider = connectionProvider;
    }

    /**
     * Executes the script defined in the methods {@link DatabaseScript} annotation.
     * {@link Binding} the return type and method parameters as required.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {

        final Object[] rtnArray = new Object[1];

        connectionProvider.execute(new SQLWorker() {
            @Override
            public void work(Connection connection) throws SQLException {
                DatabaseScript script = method.getAnnotation(DatabaseScript.class);

                int bindingIndex = 1;
                Binding returnBinding = bindings.get(method.getReturnType());
                List<Binding> paramBindings = getParametersBindingFor(method);

                CallableStatement statement = null;
                boolean failed = true;
                try {
                    statement = connection.prepareCall(script.value());
                    returnBinding.prepareGet(statement, 1);
                    bindingIndex += returnBinding.parameters();

                    for (int i = 0; i < paramBindings.size(); i++) {
                        paramBindings.get(i).setValue(statement, bindingIndex, args[i]);
                        bindingIndex += paramBindings.get(i).parameters();
                    }

                    statement.execute();
                    Object returnValue = returnBinding.getValue(statement, 1);

                    if (requiresCommitting(method)) {
                        connection.commit();
                    }
                    failed = false;
                    rtnArray[0] = returnValue;
                } finally {
                    if (failed && requiresCommitting(method)) {
                        connection.rollback();
                    }
                    close(statement);
                    close(connection);
                }
            }
        });

        return rtnArray[0];
    }

    /**
     * For the parameters of this method determine the corresponding
     * bindings.
     *
     * @param method A method whom parameters you wish to bind.
     * @return The method of binding the parameters.
     */
    private List<Binding> getParametersBindingFor(Method method) {
        List<Binding> paramBindings = new ArrayList<Binding>();
        for (Class<?> paramClazz : method.getParameterTypes()) {
            paramBindings.add(bindings.get(paramClazz));
        }
        return paramBindings;
    }

    /**
     * Should this method call commit before returning.
     *
     * @param method The method being used.
     * @return true if the method should be committed.
     */
    private boolean requiresCommitting(Method method) {
        return method.getAnnotation(Commit.class) != null;
    }

    /**
     * Closes a connection eating exceptions and ignoring nulls.
     * Yum Exceptions.
     *
     * @param s The connection to close.
     */
    private void close(Connection s) {
        if (s == null) {
            return;
        }
        try {
            s.close();
        } catch (SQLException sqle) {
            log.log(Level.WARNING, "Failed to close connection", sqle);
        }
    }

    /**
     * Closes a statement eating exceptions and ignoring nulls.
     * Yum Exceptions.
     *
     * @param s The statement to close.
     */
    private void close(Statement s) {
        if (s == null) {
            return;
        }
        try {
            s.close();
        } catch (SQLException sqle) {
            log.log(Level.WARNING, "Failed to close statement", sqle);
        }
    }
}
