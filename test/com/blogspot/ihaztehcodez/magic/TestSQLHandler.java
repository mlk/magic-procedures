package com.blogspot.ihaztehcodez.magic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.blogspot.ihaztehcodez.magic.utility.ConnectionProvider;
import com.blogspot.ihaztehcodez.magic.utility.SimpleConnectionProvider;
import com.sun.xml.internal.ws.client.Stub;

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
		List<Binding> bindings = new LinkedList<Binding>();
		bindings.add(returnBinding);
		
		replay(connection, statement, returnBinding);
		
		SQLHandler subject = new SQLHandler(bindings, new SimpleConnectionProvider(connection));
		assertNull(subject.invoke(null, Stubby.class.getMethod("stubby"), new Object[0]));
		
		verify(connection, statement, returnBinding);
	}
	
	
	
}

interface Stubby {
	@DatabaseScript("SQL")
	void stubby() throws SQLException;
}