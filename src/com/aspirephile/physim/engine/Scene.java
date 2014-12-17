package com.aspirephile.physim.engine;

import android.os.Bundle;

import com.aspirephile.physim.PhySim;
import com.aspirephile.shared.Vector;
import com.aspirephile.shared.Vector.Vector3;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class Scene {
	NullPointerAsserter asserter = new NullPointerAsserter(Scene.class);

	private String name;
	private boolean bounded;

	// TODO Remove boundary feature
	Vector3<Double> primaryCorner, secondaryCorner;

	private boolean locked;

	public Scene() {
		locked = false;
	}

	public Scene(Bundle sceneInfo) {
		try {
			setName(sceneInfo.getString(PhySim.keys.sceneCreatorName));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void lock() {
		locked = true;
	}

	public void setName(String name) {
		if (!locked && asserter.assertPointer(name)) {
			this.name = name;
		}
	}

	// TODO Remove boundary feature
	public void setBounds(Vector<Double> primaryCorner,
			Vector<Double> secondaryCorner) {
		if (!locked) {
		}
	}

	// TODO Remove boundary feature
	public void disableBounding() {
		if (!locked)
			this.bounded = false;
	}

	public String getName() {
		return name;
	}

	public boolean getBounded() {
		return bounded;
	}

	@Override
	public String toString() {
		return Scene.class.toString() + "{" + "Name:" + name + ", locked:"
				+ Boolean.toString(locked) + "}";
	}

	public boolean isLocked() {
		return locked;
	}

	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putString(PhySim.keys.sceneCreatorName, getName());
		return bundle;
	}

}
