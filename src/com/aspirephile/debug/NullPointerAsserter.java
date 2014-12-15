package com.aspirephile.debug;

import android.util.Log;

public class NullPointerAsserter {

	String tag;

	public NullPointerAsserter(String tag) {
		this.tag = tag;
	}

	public NullPointerAsserter(Class<?> cls) {
		this.tag = cls.getCanonicalName();
	}

	public boolean assertPointer(Object... objects) {
		boolean allGood = true;
		if (objects != null) {
			boolean array = objects.length > 1;
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] == null) {
					allGood = false;
					Log.e(tag, "NullPointerException of object"
							+ (array ? ("[" + i + "]") : ""));
				}
			}
		} else {
			allGood = false;
			Log.e(tag, "NullPointerException of object");
		}

		return allGood;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
