package com.blogspot.ihaztehcodez.magic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.blogspot.ihaztehcodez.magic.utility.SimpleConnectionProvider;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

public class TestSQLHandler {
	@Test( expected = NullPointerException.class)
	public void ctor_rejectsNullDatabase() {
		new SQLHandler(new ArrayList<Binding>(), null);
	}
	
	@Test
	public void invoke_executesStatementGivenInAnnotation() throws Throwable {
		Connection connection = createMock(Connection.class);
		CallableStatement statement = createMock(CallableStatement.class);
		
		expect(connection.prepareCall("SQL"))
			.andReturn(statement);
		expect(statement.execute()).andReturn(false);
		
		statement.close();
		connection.close();
		
		Binding returnBinding = createMock(Binding.class);
		expect(returnBinding.worksWith()).andReturn(new Class<?>[] { void.class } );
		returnBinding.prepareGet(statement, 1);
		expect(returnBinding.getValue(statement, 1)).andReturn(null);
		expect(returnBinding.parameters()).andReturn(0);
		List<Binding> bindings = new LinkedList<Binding>();
		bindings.add(returnBinding);
		
		replay(connection, statement, returnBinding);
		
		SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionProvider(connection));
		assertNull(subject.invoke(null, Stubby.class.getMethod("stubby"), new Object[0]));
		
		verify(connection, statement, returnBinding);
	}
	
	@Test
	public void invoke_passesOnVariables() throws Throwable {
		Connection connection = createMock(Connection.class);
		CallableStatement statement = createMock(CallableStatement.class);
		
		expect(connection.prepareCall("SQL"))
			.andReturn(statement);
		expect(statement.execute()).andReturn(false);
		
		statement.close();
		connection.close();
		
		List<Binding> bindings = new LinkedList<Binding>();
		
		Binding returnBinding = createMock(Binding.class);
		expect(returnBinding.worksWith()).andReturn(new Class<?>[] { void.class } );
		returnBinding.prepareGet(statement, 1);
		expect(returnBinding.parameters()).andReturn(0);
		expect(returnBinding.getValue(statement, 1)).andReturn(null);
		bindings.add(returnBinding);
		
		Binding paramiter = createMock(Binding.class);
		expect(paramiter.worksWith()).andReturn(new Class<?>[] { int.class } );
		paramiter.setValue(statement, 1, 8);
		expect(paramiter.parameters()).andReturn(1);
		bindings.add(paramiter);
		
		replay(connection, statement, returnBinding, paramiter);
		
		SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionProvider(connection));
		assertNull(subject.invoke(null, Stubby.class.getMethod("intIt", new Class<?>[] { int.class } ), 
				new Object[] { 8 }));
		
		verify(connection, statement, returnBinding, paramiter);
	}

	@Test
	public void invoke_commitsOnRequest() throws Throwable {
		Connection connection = createMock(Connection.class);
		CallableStatement statement = createMock(CallableStatement.class);
		
		expect(connection.prepareCall("SQL"))
			.andReturn(statement);
		expect(statement.execute()).andReturn(false);
		connection.commit();
		statement.close();
		connection.close();
		
		Binding returnBinding = createMock(Binding.class);
		expect(returnBinding.worksWith()).andReturn(new Class<?>[] { void.class } );
		returnBinding.prepareGet(statement, 1);
		expect(returnBinding.getValue(statement, 1)).andReturn(null);
		expect(returnBinding.parameters()).andReturn(0);
		List<Binding> bindings = new LinkedList<Binding>();
		bindings.add(returnBinding);
		
		replay(connection, statement, returnBinding);
		
		SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionProvider(connection));
		assertNull(subject.invoke(null, Stubby.class.getMethod("commiting"), new Object[0]));
		
		verify(connection, statement, returnBinding);
	}
	
	@Test
	public void invoke_rollsbackOnRequest() throws Throwable {
		Connection connection = createMock(Connection.class);
		CallableStatement statement = createMock(CallableStatement.class);
		
		expect(connection.prepareCall("SQL"))
			.andReturn(statement);
		expect(statement.execute()).andThrow(new SQLException());
		connection.rollback();
		statement.close();
		connection.close();
		
		Binding returnBinding = createMock(Binding.class);
		expect(returnBinding.worksWith()).andReturn(new Class<?>[] { void.class } );
		returnBinding.prepareGet(statement, 1);
		expect(returnBinding.parameters()).andReturn(0);
		List<Binding> bindings = new LinkedList<Binding>();
		bindings.add(returnBinding);
		
		replay(connection, statement, returnBinding);
		
		SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionProvider(connection));
		try {
			assertNull(subject.invoke(null, Stubby.class.getMethod("commiting"), new Object[0]));
			fail("Expected SQL exception");
		} catch(SQLException e) {
			// Expected.
		}
		
		verify(connection, statement, returnBinding);
	}
	
}

interface Stubby {
	@DatabaseScript("SQL")
	void stubby() throws SQLException;
	@DatabaseScript("SQL")
	void intIt(int it) throws SQLException;
	@Commit
	@DatabaseScript("SQL")
	void commiting() throws SQLException;
}