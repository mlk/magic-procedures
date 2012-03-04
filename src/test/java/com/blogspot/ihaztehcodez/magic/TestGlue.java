package com.blogspot.ihaztehcodez.magic;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;

public class TestGlue {
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesPassedClassIsInterface() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(TestGlue.class);
	}
	
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesPassedInterfaceHasNoMethods() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(NoMethods.class);
	}
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesPassedInterfaceDoesNotExtendsAnything() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(ExtendsFine.class);
	}
	
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesPassedInterfaceHasMethodsWithoutAnnotation() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(OneMethodsWithAnnotation.class);
	}
	
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesAllMethodsWithoutAnnotation() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(NoMethodsWithAnnotation.class);
	}
	
	@Test( expected = IllegalArgumentException.class)
	public void toDatabasePackage_validatesMethodsThrowSQLException() {
		Glue glue = new Glue(mock(InvocationHandler.class));
		glue.toDatabasePackage(DoesNotThrow.class);
	}
	
	@Test
	public void toDatabasePackage_returnsProxyWhenGivenAValidInterface() {
		InvocationHandler handler = mock(InvocationHandler.class);
		Glue glue = new Glue(handler);
		
		Fine fine = glue.toDatabasePackage(Fine.class);
		
		assertNotNull("Should return an object", fine);
		assertTrue("The returned object should be a proxy", 
				Proxy.isProxyClass(fine.getClass()));
		assertSame(handler, Proxy.getInvocationHandler(fine));
	}
	
}

interface NoMethods {
}

interface DoesNotThrow {
	@DatabaseScript("test")
	void yeay();
}

interface Fine {
	@DatabaseScript("test")
	void yeay() throws SQLException;
}

interface ExtendsFine extends Fine {
	@DatabaseScript("test")
	void boo() throws SQLException;
}

interface OneMethodsWithAnnotation {
	void boo() throws SQLException;
	@DatabaseScript("test")
	void yeay() throws SQLException;
}

interface NoMethodsWithAnnotation {
	void yeay() throws SQLException;
}