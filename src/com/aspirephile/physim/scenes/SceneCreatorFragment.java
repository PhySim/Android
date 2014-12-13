package com.aspirephile.physim.scenes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspirephile.physim.R;

public class SceneCreatorFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scene_creator,
				container, false);
		bridgeXML(v);
		initializeFeilds();
		return v;
	}

	private void bridgeXML(View v) {
		
	}

	private void initializeFeilds() {
		
	}
}
