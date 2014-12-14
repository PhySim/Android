package com.aspirephile.exception;

import com.aspirephile.physim.engine.Scene;

public class SceneLockedException extends ObjectLockedException {

	/**
	 * Exception thrown when an attempt is made to set/modify an argument of a
	 * locked scene
	 */
	private static final long serialVersionUID = 880701834776490523L;

	public SceneLockedException() {
		super.setObjectClass(Scene.class);
	}

}
