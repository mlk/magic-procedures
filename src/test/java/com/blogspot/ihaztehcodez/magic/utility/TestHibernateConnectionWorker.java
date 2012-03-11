package com.blogspot.ihaztehcodez.magic.utility;

import com.google.common.base.Suppliers;
import org.hibernate.Session;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class TestHibernateConnectionWorker {

    @Test
    public void execute_passesExecutionToHibernate() throws SQLException {

        final Connection connection = mock(Connection.class);
        Session session = new PassWorkOnSession(connection);

        HibernateConnectionWorker subject = new HibernateConnectionWorker(Suppliers.ofInstance(session));

        subject.execute(new SQLWorker() {
            @Override
            public void work(Connection connection) throws SQLException {
                connection.createBlob();
            }
        });

        verify(connection).createBlob();
    }

    @Test
    public void execute_closingTheConnectionDoesNotCloseTheConnection() throws SQLException {
        final Connection connection = mock(Connection.class);
        Session session = new PassWorkOnSession(connection);

        HibernateConnectionWorker subject = new HibernateConnectionWorker(Suppliers.ofInstance(session));

        subject.execute(new SQLWorker() {
            @Override
            public void work(Connection connection) throws SQLException {
                connection.close();
            }
        });

        verify(connection, never()).close();
    }

    @Test
    public void execute_sqlExceptionsUnwrapped() throws SQLException {
        final Connection connection = mock(Connection.class);
        Session session = new PassWorkOnSession(connection);

        HibernateConnectionWorker subject = new HibernateConnectionWorker(Suppliers.ofInstance(session));

        final SQLException expected = new SQLException();
        Exception actual = null;

        try {
            subject.execute(new SQLWorker() {
                @Override
                public void work(Connection connection) throws SQLException {
                    throw expected;
                }
            });
        } catch (Exception e) {
            actual = e;
        }

        assertSame(expected, actual);
    }
}

