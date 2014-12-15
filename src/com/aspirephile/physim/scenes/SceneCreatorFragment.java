package com.aspirephile.physim.scenes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aspirephile.debug.Logger;
import com.aspirephile.debug.NullPointerAsserter;
import com.aspirephile.physim.R;

public class SceneCreatorFragment extends Fragment {
	NullPointerAsserter asserter = new NullPointerAsserter(
			SceneCreatorFragment.class);
	Logger l = new Logger(SceneCreatorFragment.class);

	EditText name;
	TextView nameMessage;
	TextWatcher nameWatcher;

	private enum SceneNameState {
		EMPTY, GOOD, NOT_UNIQUE
	}

	SceneNameState nameState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_scene_creator, container,
				false);
		bridgeXML(v);
		initializeFeilds();
		return v;
	}

	private void bridgeXML(View v) {
		name = (EditText) v.findViewById(R.id.et_scene_name);
		nameMessage = (TextView) v
				.findViewById(R.id.tv_scene_creator_name_message);
	}

	private void initializeFeilds() {
		nameState = SceneNameState.EMPTY;
		nameWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				l.d("Scene name text changed: s=" + s + ", start=" + start
						+ ", before=" + before + ", count=" + count);

				if (s.length() > 0) {

					if (true)// TODO search through cursor of scene names for
								// match and appropriately provide a message on
								// the screen
					{
						if (nameState != SceneNameState.GOOD) {
							nameState = SceneNameState.GOOD;
							nameMessage.setVisibility(View.GONE);
							l.d("Scene name good");
						}
					} else if (nameState != SceneNameState.NOT_UNIQUE) {
						nameState = SceneNameState.NOT_UNIQUE;
						l.d("Scene name not unique!");
						nameMessage
								.setText(R.string.scene_creator_name_message_empty);
						nameMessage.setVisibility(View.VISIBLE);
					}
				} else if (nameState != SceneNameState.EMPTY) {
					nameState = SceneNameState.EMPTY;
					l.d("Scene name empty!");
					nameMessage
							.setText(R.string.scene_creator_name_message_empty);
					nameMessage.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		name.addTextChangedListener(nameWatcher);
	}
}
