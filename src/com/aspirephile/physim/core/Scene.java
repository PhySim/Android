package com.aspirephile.physim.core;

import java.io.Serializable;

import android.content.ContentValues;
import android.os.Bundle;

import com.aspirephile.physim.scenes.db.ScenesDBProps;
import com.aspirephile.shared.Vector;
import com.aspirephile.shared.Vector.Vector3;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class Scene implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7995238174074629633L;

	public static class defaults {

		public static final String name = "Unnamed";
	}

	NullPointerAsserter asserter = new NullPointerAsserter(Scene.class);

	private String name;
	private long created;
	private long lastModified;
	private boolean bounded;

	// TODO Remove boundary feature
	Vector3<Double> primaryCorner, secondaryCorner;

	private boolean locked;

	public Scene() {
		locked = bounded = false;
		name = null;
		created = 0;
		lastModified = 0;
	}

	public Scene(Bundle sceneInfo) {
		try {
			setName(sceneInfo
					.getString(ScenesDBProps.v2.tables.scenes.column.NAME));
			setCreated(sceneInfo
					.getLong(ScenesDBProps.v2.tables.scenes.column.CREATED));
			setLastModified(sceneInfo
					.getLong(ScenesDBProps.v2.tables.scenes.column.CREATED));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public Scene(ContentValues values) {
		String name = values
				.getAsString(ScenesDBProps.v2.tables.scenes.column.NAME);
		if (asserter.assertPointer(name)) {
			setName(name);
		}
		long l = values
				.getAsLong(ScenesDBProps.v2.tables.scenes.column.CREATED);
		setCreated(l);
		l = values
				.getAsLong(ScenesDBProps.v2.tables.scenes.column.LAST_MODIFIED);
		setLastModified(l);
	}

	public void lock() {
		locked = true;
	}

	public void setName(String name) {
		if (!locked && asserter.assertPointer(name)) {
			this.name = name;
		}
	}

	private void setCreated(long created) {
		if (!locked && asserter.assertPointer(name)) {
			this.created = created;
		}

	}

	private void setLastModified(long lastModified) {
		if (!locked && asserter.assertPointer(name)) {
			this.lastModified = lastModified;
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

	public long getCreated() {
		return created;
	}

	public long getLastModified() {
		return lastModified;
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

	public boolean formatData() {
		boolean isSuccess = true;
		if (asserter.assertPointer(name))
			if (name == "")
				setName(Scene.defaults.name);
			else
				isSuccess = false;
		else
			isSuccess = false;
		if (getCreated() == 0)
			created = System.currentTimeMillis();
		if (getLastModified() == 0)
			lastModified = System.currentTimeMillis();
		return isSuccess;
	}

	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putString(ScenesDBProps.v2.tables.scenes.column.NAME, getName());
		bundle.putLong(ScenesDBProps.v2.tables.scenes.column.CREATED,
				getCreated());
		bundle.putLong(ScenesDBProps.v2.tables.scenes.column.LAST_MODIFIED,
				getLastModified());
		return bundle;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(ScenesDBProps.v2.tables.scenes.column.NAME, getName());
		values.put(ScenesDBProps.v2.tables.scenes.column.CREATED, getCreated());
		values.put(ScenesDBProps.v2.tables.scenes.column.LAST_MODIFIED,
				getLastModified());
		return values;
	}

}
