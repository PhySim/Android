package com.aspirephile.physim.core;

import android.content.Context;

import com.aspirephile.shared.debug.NullPointerAsserter;

public class PhySim {
	NullPointerAsserter asserter = new NullPointerAsserter(PhySim.class);

	PhySimOptions options;

	Context context;

	public PhySim(Context context) {
		options = new PhySimOptions();
		this.context = context;
	}

	public void setOptions(PhySimOptions options) {
		if (asserter.assertPointer(options)) {
			this.options = options;
		}
	}

}
