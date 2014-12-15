package com.aspirephile.exception;

import com.aspirephile.physim.engine.Scene;

public class SceneLockException extends ObjectLockException {

	/**
	 * Exception thrown when an attempt is made to set/modify an argument of a
	 * locked scene
	 */
	private static final long serialVersionUID = 880701834776490523L;

	public SceneLockException(Scene scene) {
		super.setObjectClass(Scene.class);
		super.setObjectName(scene.getName());
	}

}
