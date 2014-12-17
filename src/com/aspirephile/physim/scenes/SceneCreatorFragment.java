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
import com.aspirephile.physim.PhySim;
import com.aspirephile.physim.R;
import com.aspirephile.physim.engine.Scene;

public class SceneCreatorFragment extends Fragment implements
		OnSceneFieldsValidityListener {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			SceneCreatorFragment.class);
	private Logger l = new Logger(SceneCreatorFragment.class);

	private EditText name;
	private TextView nameMessage;
	private TextWatcher nameWatcher;

	public SceneCreatorFragment() {
		validityListener = this;
	}

	private enum SceneNameState {
		EMPTY, GOOD, NOT_UNIQUE
	}

	private SceneNameState nameState;

	private OnSceneFieldsValidityListener validityListener = this;

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
		setSceneNameValidity(SceneNameState.EMPTY);
		nameWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				l.d("Scene name text changed: s=" + s + ", start=" + start
						+ ", before=" + before + ", count=" + count);

				if (s.length() > 0) {

					if (nameState != SceneNameState.GOOD) {
						if (true)// TODO search through cursor of scene names
									// (maybe asynchronously)
									// for
						// match and appropriately provide a message on
						// the screen
						{

							setSceneNameValidity(SceneNameState.GOOD);
						}
					} else if (nameState != SceneNameState.NOT_UNIQUE) {
						setSceneNameValidity(SceneNameState.NOT_UNIQUE);
					}
				} else if (nameState != SceneNameState.EMPTY) {
					setSceneNameValidity(SceneNameState.EMPTY);
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

	public Scene getScene() {
		Scene scene = new Scene();
		String name = getSceneName();
		if (asserter.assertPointer(name))
			scene.setName(name);
		else
			return null;
		// TODO Similarly have separate if conditions checking for validity of
		// other fields with their own separate elses returning null such that
		// it only returns scene if all goes well
		return scene;
	}

	private String getSceneName() {
		if (nameState == SceneNameState.GOOD)
			return name.getText().toString();
		return null;
	}

	public void setOnSceneFieldsValidityListener(
			OnSceneFieldsValidityListener onSceneFieldsValidityListener) {
		if (asserter.assertPointer(onSceneFieldsValidityListener)) {
			validityListener = onSceneFieldsValidityListener;
		}
	}

	@Override
	public void onSceneInfoValid() {
		l.d("Default onSceneInfoValid called");

	}

	@Override
	public void onSceneInfoInvalid(String... invalidFields) {
		l.d("Default onSceneInfoInvalid called with invalid fields: "
				+ invalidFields.toString());

	}

	public void setSceneNameValidity(SceneNameState state) {
		if (nameState != state) {
			nameState = state;
			l.d("Scene name state: " + state);
			switch (state) {
			case GOOD:
				validityListener.onSceneInfoValid();
				nameMessage.setVisibility(View.GONE);
				break;
			case EMPTY:
				nameMessage.setText(R.string.scene_creator_name_message_empty);
			case NOT_UNIQUE:
				nameMessage
						.setText(R.string.scene_creator_name_message_not_unique);
				nameMessage.setVisibility(View.VISIBLE);
				validityListener.onSceneInfoInvalid(
						PhySim.keys.sceneCreatorName, state.toString());
				break;
			default:
				l.d("Unknown scene name state selected (" + state + ")");
				break;
			}
		}
	}
}
