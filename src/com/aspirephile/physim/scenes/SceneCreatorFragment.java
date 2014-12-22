package com.aspirephile.physim.scenes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirephile.physim.PhySimProps;
import com.aspirephile.physim.R;
import com.aspirephile.physim.core.Scene;
import com.aspirephile.physim.scenes.db.ScenesDBProps;
import com.aspirephile.physim.scenes.db.ScenesProvider;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class SceneCreatorFragment extends Fragment implements TextWatcher,
		OnSceneFieldsValidityListener, LoaderManager.LoaderCallbacks<Cursor>,
		OnClickListener {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			SceneCreatorFragment.class);
	private Logger l = new Logger(SceneCreatorFragment.class);

	private EditText name;
	private TextView nameMessage;
	Cursor sceneNamesCursor;

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
		updateSceneNameValidity(SceneNameState.EMPTY);
		name.addTextChangedListener(this);
		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				boolean keepOriginal = true;
				StringBuilder sb = new StringBuilder(end - start);
				for (int i = start; i < end; i++) {
					char c = source.charAt(i);
					if (isCharAllowed(c)) // put your condition here
						sb.append(c);
					else
						keepOriginal = false;
				}
				if (keepOriginal)
					return null;
				else {
					if (source instanceof Spanned) {
						SpannableString sp = new SpannableString(sb);
						TextUtils.copySpansFrom((Spanned) source, start,
								sb.length(), null, sp, 0);
						return sp;
					} else {
						return sb;
					}
				}
			}

			private boolean isCharAllowed(char c) {
				return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
			}
		};
		name.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					Toast.makeText(getActivity(), name.getText(),
							Toast.LENGTH_SHORT).show();
					// TODO Finish the scene creator
					return true;
				}
				return false;
			}
		});
		name.setFilters(new InputFilter[] { filter });
		sceneNamesCursor = null;
		getLoaderManager().initLoader(PhySimProps.loaders.scenesLoader, null,
				this);
	}

	public Scene getScene() {
		Scene scene = new Scene();
		String name = getSceneName();
		scene.setName(name);
		// TODO Similarly have separate if conditions checking for validity of
		// other fields with their own separate elses returning null such that
		// it only returns scene if all goes well
		return scene;
	}

	private String getSceneName() {
		if (nameState == SceneNameState.GOOD) {
			String sceneName = name.getText().toString();
			int toTrim = 0;
			for (int i = sceneName.length() - 1; i >= 0; i--) {
				if (sceneName.charAt(i) == ' ') {
					toTrim++;
				} else
					return sceneName.substring(0, sceneName.length() - toTrim);
			}
		}
		return null;
	}

	public void setOnSceneFieldsValidityListener(
			OnSceneFieldsValidityListener onSceneFieldsValidityListener) {
		if (asserter.assertPointer(onSceneFieldsValidityListener)) {
			validityListener = onSceneFieldsValidityListener;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		l.d("Scene name text changed: s=" + s + ", start=" + start
				+ ", before=" + before + ", count=" + count);

		if (s.length() > 0) {

			if (querySceneName(s.toString()))
			// TODO search asynchronously through cursor for scene name
			// match and appropriately provide a message on
			// the screen
			{
				updateSceneNameValidity(SceneNameState.NOT_UNIQUE);
			} else
				updateSceneNameValidity(SceneNameState.GOOD);
		} else
			updateSceneNameValidity(SceneNameState.EMPTY);

	}

	private boolean querySceneName(String userInput) {
		l.d("Querying scene name: " + userInput);
		if (asserter.assertPointer(sceneNamesCursor)) {
			int columnIndex = sceneNamesCursor
					.getColumnIndex(ScenesDBProps.v2.tables.scenes.column.NAME);
			l.d("Found " + sceneNamesCursor.getCount() + " names");
			if (sceneNamesCursor.moveToFirst())
				do {
					String sceneName = sceneNamesCursor.getString(columnIndex);
					boolean isMatch = userInput.equals(sceneName);
					l.d("Checking if userInput: " + userInput + " matches "
							+ sceneName + "(" + isMatch + ")");
					if (isMatch) {
						l.d("User input matched to scene name: " + userInput);
						return true;
					}

				} while (sceneNamesCursor.moveToNext());
		}
		return false;
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

	@Override
	public void onSceneInfoValid() {
		l.d("Default onSceneInfoValid called");

	}

	@Override
	public void onSceneInfoInvalid(String... invalidFields) {
		l.d("Default onSceneInfoInvalid called with invalid fields: "
				+ invalidFields.toString());

	}

	public void updateSceneNameValidity(SceneNameState state) {
		if (nameState != state) {
			nameState = state;
			l.d("Scene name state: " + state);
			switch (state) {
			case GOOD:
				validityListener.onSceneInfoValid();
				nameMessage.setVisibility(View.GONE);
				break;
			case NOT_UNIQUE:
			case EMPTY:
				nameMessage.setVisibility(View.VISIBLE);
				validityListener.onSceneInfoInvalid(
						PhySimProps.keys.sceneCreatorName, state.toString());
				if (nameState == SceneNameState.NOT_UNIQUE)
					nameMessage
							.setText(R.string.scene_creator_name_message_not_unique);
				else if (nameState == SceneNameState.EMPTY)
					nameMessage
							.setText(R.string.scene_creator_name_message_empty);
				break;
			default:
				l.d("Unknown scene name state selected (" + state + ")");
				break;
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == PhySimProps.loaders.scenesLoader) {
			Uri uri = ScenesProvider.CONTENT_URI;
			// TODO use projection instead of URI!
			uri = Uri.withAppendedPath(uri,
					ScenesDBProps.v2.tables.scenes.column.NAME);
			l.d("Instantiating new loader with id: " + id + "and URI: " + uri);
			return new CursorLoader(getActivity(), uri, null, null, null, null);
		} else
			return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		l.d("Cursor load finished");
		if (asserter.assertPointer(data)) {
			sceneNamesCursor = data;
			data.moveToFirst();
			while (data.moveToNext()) {
				String sceneName = data
						.getString(data
								.getColumnIndex(ScenesDBProps.v2.tables.scenes.column.NAME));
				l.d("Cursor position: " + data.getPosition()
						+ " contains scene name: " + sceneName);

			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		l.d("Cursor loader reset");
		sceneNamesCursor = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_scene_creator_done:
			Activity parentActivity = getActivity();
			Intent i = new Intent();
			Scene scene = getScene();
			Bundle sceneInfo;
			if (asserter.assertPointer(scene)) {
				sceneInfo = scene.toBundle();
				i.putExtra(PhySimProps.keys.sceneCreatorBundle, sceneInfo);
				parentActivity.setResult(Activity.RESULT_OK, i);
				parentActivity.finish();
			} else// else case is more or less redundant since done button is
					// expected to be disabled when scene is invalid
			{

				Toast.makeText(
						parentActivity.getApplicationContext(),
						getResources().getString(
								R.string.scene_creator_toast_field_invalid),
						Toast.LENGTH_LONG).show();
			}

			break;
		default:
			l.w("Unknown button clicked (id: " + v.getId() + ")");
			break;
		}
	}

}
