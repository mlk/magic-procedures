package com.github.mlk.magic;

import com.github.mlk.magic.binders.VoidBinding;
import com.github.mlk.magic.utility.SimpleConnectionWorker;
import com.google.common.base.Suppliers;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

public class TestSQLHandler {
    @Test(expected = NullPointerException.class)
    public void ctor_rejectsNullDatabase() {
        new SQLHandler(new ArrayList<Binding>(), null);
    }

    @Test
    public void invoke_executesStatementGivenInAnnotation() throws Throwable {
        Connection connection = mock(Connection.class);
        CallableStatement statement = mock(CallableStatement.class);

        when(connection.prepareCall("SQL"))
                .thenReturn(statement);

        Binding returnBinding = new VoidBinding();
        List<Binding> bindings = new LinkedList<Binding>();
        bindings.add(returnBinding);

        SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionWorker(Suppliers.ofInstance(connection)));
        assertNull(subject.invoke(null, Stubby.class.getMethod("stubby"), new Object[0]));

        verify(statement).execute();
    }

    @Test
    public void invoke_passesOnVariables() throws Throwable {
        Connection connection = mock(Connection.class);
        CallableStatement statement = mock(CallableStatement.class);

        when(connection.prepareCall("SQL"))
                .thenReturn(statement);

        List<Binding> bindings = new LinkedList<Binding>();

        Binding returnBinding = new VoidBinding();
        bindings.add(returnBinding);

        Binding paramiter = mock(Binding.class);
        when(paramiter.worksWith()).thenReturn(new Class<?>[]{int.class});

        when(paramiter.parameters()).thenReturn(1);
        bindings.add(paramiter);

        SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionWorker(Suppliers.ofInstance(connection)));
        assertNull(subject.invoke(null, Stubby.class.getMethod("intIt", int.class),
                new Object[]{8}));

        verify(paramiter).setValue(statement, 1, 8);
    }

    @Test
    public void invoke_commitsOnRequest() throws Throwable {
        Connection connection = mock(Connection.class);
        CallableStatement statement = mock(CallableStatement.class);

        when(connection.prepareCall("SQL"))
                .thenReturn(statement);

        List<Binding> bindings = new LinkedList<Binding>();
        bindings.add(new VoidBinding());

        SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionWorker(Suppliers.ofInstance(connection)));
        assertNull(subject.invoke(null, Stubby.class.getMethod("committing"), new Object[0]));

        verify(connection).commit();
    }

    @Test
    public void invoke_rollsbackOnRequest() throws Throwable {
        Connection connection = mock(Connection.class);
        CallableStatement statement = mock(CallableStatement.class);

        when(connection.prepareCall("SQL"))
                .thenReturn(statement);
        when(statement.execute()).thenThrow(new SQLException());

        List<Binding> bindings = new LinkedList<Binding>();
        bindings.add(new VoidBinding());

        SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionWorker(Suppliers.ofInstance(connection)));
        try {
            assertNull(subject.invoke(null, Stubby.class.getMethod("committing"), new Object[0]));
            fail("Expected SQL exception");
        } catch (SQLException e) {
            // Expected.
        }

        verify(connection).rollback();
    }
}

interface Stubby {
    @DatabaseScript("SQL")
    void stubby() throws SQLException;

    @DatabaseScript("SQL")
    void intIt(int it) throws SQLException;

    @Commit
    @DatabaseScript("SQL")
    void committing() throws SQLException;
}