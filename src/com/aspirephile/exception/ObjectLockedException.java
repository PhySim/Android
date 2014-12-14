package com.aspirephile.exception;

import com.aspirephile.debug.NullPointerAsserter;

public class ObjectLockedException extends Exception {
	private static class defaults {

		public static final String argumentName = "<Unknown>";
		public static final String objectName = "<Unknown>";

	}

	NullPointerAsserter asserter = new NullPointerAsserter(
			ObjectLockedException.class);

	/**
	 * Exception thrown when an attempt is made to set/modify an argument of a
	 * locked object
	 */
	private static final long serialVersionUID = 6859641333683205071L;

	private String argumentName;
	private Class<?> argumentClass;
	private String objectName;
	private Class<?> objectClass;

	public ObjectLockedException() {
		// TODO Use setters for the ObjectLockedException constructor here
		argumentName = ObjectLockedException.defaults.argumentName;
		argumentClass = Object.class;
		objectName = ObjectLockedException.defaults.objectName;
		objectClass = Object.class;
	}

	public void setArgumentName(String name) throws NullPointerException {
		if (asserter.assertPointer(name))
			this.argumentName = name;
		else
			throw new NullPointerException();
	}

	public void setArgumentClass(Class<?> argumentType)
			throws NullPointerException {
		if (asserter.assertPointer(argumentType))
			this.argumentClass = argumentType;
		else
			throw new NullPointerException();
	}

	public void setObjectName(String name) throws NullPointerException {
		if (asserter.assertPointer(name))
			this.argumentName = name;
		else
			throw new NullPointerException();
	}

	public void setObjectClass(Class<?> objectType) throws NullPointerException {
		if (asserter.assertPointer(objectType))
			this.objectClass = objectType;
		else
			throw new NullPointerException();
	}

	public String getArgumentName() {
		return argumentName;
	}

	public Class<?> getArgumentType() {
		return argumentClass;
	}

	public String getObjectName() {
		return objectName;
	}

	public Class<?> getObjectType() {
		return objectClass;
	}

}
