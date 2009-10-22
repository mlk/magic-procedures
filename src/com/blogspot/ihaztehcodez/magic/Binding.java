package com.blogspot.ihaztehcodez.magic;

import java.sql.CallableStatement;
import java.sql.SQLException;

/** This class handles bindings 
 * 
 * @author <a href="http://tinyurl.com/magic-procedures">Mlk</a>
 *
 * @param <T> 
 */
public interface Binding {

	/** Retrieves the value from the statement. 
	 * 
	 * @param statement
	 * @param value
	 */
	void setValue(CallableStatement statement, int index, Object value) throws SQLException ;
	/** Sets up a statement to be able to return a value.
	 * 
	 * @param statement
	 * @param index
	 */
	void prepareGet(CallableStatement statement, int index) throws SQLException ;
	/** Retrieves a value from a statement.
	 * 
	 * @param statement
	 * @param index
	 * @return
	 */
	Object getValue(CallableStatement statement, int index) throws SQLException ;
	
	/** @return The types this binding can bind. */
	Class<?>[] worksWith();
}
