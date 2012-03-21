package com.github.mlk.magic.utility;

import org.hibernate.*;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A session that runs the Work with the given connection.
 */
@SuppressWarnings("deprecation")
class PassWorkOnSession implements Session {
    private final Connection connection;

    PassWorkOnSession(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void doWork(Work work) throws HibernateException {
        try {
            work.execute(connection);
        } catch (SQLException e) {
            throw new HibernateException(e);
        }
    }

    // Null implementations

    @Override
    public SharedSessionBuilder sessionWithOptions() {
        return null;
    }

    @Override
    public void flush() throws HibernateException {

    }

    @Override
    public void setFlushMode(FlushMode flushMode) {

    }

    @Override
    public FlushMode getFlushMode() {
        return null;
    }

    @Override
    public void setCacheMode(CacheMode cacheMode) {

    }

    @Override
    public CacheMode getCacheMode() {
        return null;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return null;
    }

    @Override
    public Connection close() throws HibernateException {
        return null;
    }

    @Override
    public void cancelQuery() throws HibernateException {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isDirty() throws HibernateException {
        return false;
    }

    @Override
    public boolean isDefaultReadOnly() {
        return false;
    }

    @Override
    public void setDefaultReadOnly(boolean b) {

    }

    @Override
    public Serializable getIdentifier(Object o) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public void evict(Object o) {

    }

    @Override
    public Object load(Class aClass, Serializable serializable, LockMode lockMode) {
        return null;
    }

    @Override
    public Object load(Class aClass, Serializable serializable, LockOptions lockOptions) {
        return null;
    }

    @Override
    public Object load(String s, Serializable serializable, LockMode lockMode) {
        return null;
    }

    @Override
    public Object load(String s, Serializable serializable, LockOptions lockOptions) {
        return null;
    }

    @Override
    public Object load(Class aClass, Serializable serializable) {
        return null;
    }

    @Override
    public Object load(String s, Serializable serializable) {
        return null;
    }

    @Override
    public void load(Object o, Serializable serializable) {

    }

    @Override
    public void replicate(Object o, ReplicationMode replicationMode) {

    }

    @Override
    public void replicate(String s, Object o, ReplicationMode replicationMode) {

    }

    @Override
    public Serializable save(Object o) {
        return null;
    }

    @Override
    public Serializable save(String s, Object o) {
        return null;
    }

    @Override
    public void saveOrUpdate(Object o) {

    }

    @Override
    public void saveOrUpdate(String s, Object o) {

    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void update(String s, Object o) {

    }

    @Override
    public Object merge(Object o) {
        return null;
    }

    @Override
    public Object merge(String s, Object o) {
        return null;
    }

    @Override
    public void persist(Object o) {

    }

    @Override
    public void persist(String s, Object o) {

    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public void delete(String s, Object o) {

    }

    @Override
    public void lock(Object o, LockMode lockMode) {

    }

    @Override
    public void lock(String s, Object o, LockMode lockMode) {

    }

    @Override
    public LockRequest buildLockRequest(LockOptions lockOptions) {
        return null;
    }

    @Override
    public void refresh(Object o) {

    }

    @Override
    public void refresh(String s, Object o) {

    }

    @Override
    public void refresh(Object o, LockMode lockMode) {

    }

    @Override
    public void refresh(Object o, LockOptions lockOptions) {

    }

    @Override
    public void refresh(String s, Object o, LockOptions lockOptions) {

    }

    @Override
    public LockMode getCurrentLockMode(Object o) {
        return null;
    }

    @Override
    public Query createFilter(Object o, String s) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(Class aClass, Serializable serializable) {
        return null;
    }

    @Override
    public Object get(Class aClass, Serializable serializable, LockMode lockMode) {
        return null;
    }

    @Override
    public Object get(Class aClass, Serializable serializable, LockOptions lockOptions) {
        return null;
    }

    @Override
    public Object get(String s, Serializable serializable) {
        return null;
    }

    @Override
    public Object get(String s, Serializable serializable, LockMode lockMode) {
        return null;
    }

    @Override
    public Object get(String s, Serializable serializable, LockOptions lockOptions) {
        return null;
    }

    @Override
    public String getEntityName(Object o) {
        return null;
    }

    @Override
    public IdentifierLoadAccess byId(String s) {
        return null;
    }

    @Override
    public IdentifierLoadAccess byId(Class aClass) {
        return null;
    }

    @Override
    public NaturalIdLoadAccess byNaturalId(String s) {
        return null;
    }

    @Override
    public NaturalIdLoadAccess byNaturalId(Class aClass) {
        return null;
    }

    @Override
    public SimpleNaturalIdLoadAccess bySimpleNaturalId(String s) {
        return null;
    }

    @Override
    public SimpleNaturalIdLoadAccess bySimpleNaturalId(Class aClass) {
        return null;
    }

    @Override
    public Filter enableFilter(String s) {
        return null;
    }

    @Override
    public Filter getEnabledFilter(String s) {
        return null;
    }

    @Override
    public void disableFilter(String s) {

    }

    @Override
    public SessionStatistics getStatistics() {
        return null;
    }

    @Override
    public boolean isReadOnly(Object o) {
        return false;
    }

    @Override
    public void setReadOnly(Object o, boolean b) {

    }

    @Override
    public <T> T doReturningWork(ReturningWork<T> tReturningWork) throws HibernateException {
        return null;
    }

    @Override
    public Connection disconnect() {
        return null;
    }

    @Override
    public void reconnect(Connection connection) {

    }

    @Override
    public boolean isFetchProfileEnabled(String s) throws UnknownProfileException {
        return false;
    }

    @Override
    public void enableFetchProfile(String s) throws UnknownProfileException {

    }

    @Override
    public void disableFetchProfile(String s) throws UnknownProfileException {

    }

    @Override
    public TypeHelper getTypeHelper() {
        return null;
    }

    @Override
    public LobHelper getLobHelper() {
        return null;
    }

    @Override
    public String getTenantIdentifier() {
        return null;
    }

    @Override
    public Transaction beginTransaction() {
        return null;
    }

    @Override
    public Transaction getTransaction() {
        return null;
    }

    @Override
    public Query getNamedQuery(String s) {
        return null;
    }

    @Override
    public Query createQuery(String s) {
        return null;
    }

    @Override
    public SQLQuery createSQLQuery(String s) {
        return null;
    }

    @Override
    public Criteria createCriteria(Class aClass) {
        return null;
    }

    @Override
    public Criteria createCriteria(Class aClass, String s) {
        return null;
    }

    @Override
    public Criteria createCriteria(String s) {
        return null;
    }

    @Override
    public Criteria createCriteria(String s, String s1) {
        return null;
    }
}
