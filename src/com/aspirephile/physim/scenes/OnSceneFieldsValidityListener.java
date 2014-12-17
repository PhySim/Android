package com.aspirephile.physim.scenes;

public interface OnSceneFieldsValidityListener {

	public void onSceneInfoValid();

	/*
	 * correspondingFields[] represents the fields which are invalid
	 */
	public void onSceneInfoInvalid(String... invalidFields);
}
