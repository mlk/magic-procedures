package com.blogspot.ihaztehcodez.magic.utility;

import com.google.common.base.Supplier;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;

/** Hibernate compatible connection worker. */
public class HibernateConnectionWorker implements ConnectionWorker {
    private final Supplier<Session> sessionSupplier;

    public HibernateConnectionWorker(final Supplier<Session> sessionSupplier) {
        this.sessionSupplier = sessionSupplier;
    }

    @Override
    public void execute(final SQLWorker worker) throws SQLException {
        Session session = sessionSupplier.get();
        try {
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    worker.work(new UnclosableConnectionWrapper(connection));
                }
            });
        } catch(HibernateException he) {
            if(he.getCause() instanceof SQLException) {
                throw (SQLException) he.getCause();
            }
            throw he;
        }
    }
}
