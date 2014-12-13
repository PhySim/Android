package com.aspirephile.exception;

import com.aspirephile.debug.NullPointerAsserter;

public class ObjectLockedException extends Exception {
	NullPointerAsserter asserter = new NullPointerAsserter(
			ObjectLockedException.class);

	/**
	 * Exception thrown when an attempt is made to set/modify an argument of a
	 * locked object
	 */
	private static final long serialVersionUID = 6859641333683205071L;

	private String argumentName = "<Unknown>";
	private Class<?> argumentType;
	private String objectName = "<Unknown>";
	private Class<?> objectType;

	public void setArgumentName(String name) throws NullPointerException {
		if (asserter.assertPointer(name))
			this.argumentName = name;
		else
			throw new NullPointerException();
	}

	public void setArgumentType(Class<?> argumentType)
			throws NullPointerException {
		if (asserter.assertPointer(argumentType))
			this.argumentType = argumentType;
		else
			throw new NullPointerException();
	}

	public void setObjectName(String name) throws NullPointerException {
		if (asserter.assertPointer(name))
			this.argumentName = name;
		else
			throw new NullPointerException();
	}

	public void setObjectType(Class<?> objectType) throws NullPointerException {
		if (asserter.assertPointer(objectType))
			this.objectType = objectType;
		else
			throw new NullPointerException();
	}

	public String getArgumentName() {
		return argumentName;
	}

	public String getArgumentType() {
		return argumentName;
	}

	public String getObjectName() {
		return argumentName;
	}

	public String getObjectType() {
		return argumentName;
	}

}
