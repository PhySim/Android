package com.aspirephile.physim.scenes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.aspirephile.physim.PhySimProps;
import com.aspirephile.physim.R;
import com.aspirephile.physim.engine.Scene;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class SceneCreator extends ActionBarActivity implements OnClickListener,
		OnSceneFieldsValidityListener {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			SceneCreatorFragment.class);
	private Logger l = new Logger(SceneCreatorFragment.class);

	private Button done;

	private SceneCreatorFragment sceneCreatorF;

	private String invalidFeilds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_creator);

		if (savedInstanceState == null) {
			sceneCreatorF = new SceneCreatorFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, sceneCreatorF).commit();
		}
		bridgeXML();
		initializeFeilds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scene_creator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bridgeXML() {
		done = (Button) findViewById(R.id.b_scene_creator_done);
		asserter.assertPointer(done);
	}

	private void initializeFeilds() {
		done.setOnClickListener(this);
		sceneCreatorF.setOnSceneFieldsValidityListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_scene_creator_done:
			Intent i = new Intent();
			Scene scene = sceneCreatorF.getScene();
			Bundle sceneInfo;
			if (scene != null) {
				sceneInfo = scene.toBundle();
				i.putExtra(PhySimProps.keys.sceneCreatorBundle, sceneInfo);
				setResult(Activity.RESULT_OK, i);
				finish();
			} else// else case is more or less redundant since done button is
					// expected to be disabled
			{

				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.scene_creator_toast_field_invalid)
								+ invalidFeilds, Toast.LENGTH_LONG).show();
			}

			break;
		default:
			l.w("Unknown button clicked (id: " + v.getId() + ")");
			break;
		}
	}

	@Override
	public void onSceneInfoValid() {
		l.d("Listener received scene info valid");
		done.setEnabled(true);
		invalidFeilds = null;
	}

	@Override
	public void onSceneInfoInvalid(String... invalidFields) {
		this.invalidFeilds = formatedStringArray(invalidFields);
		l.d("Listener received scene info invalid" + invalidFeilds);
		done.setEnabled(false);
	}

	public String formatedStringArray(String[] stringArray) {
		if (asserter.assertPointer((Object[]) stringArray)) {
			String result = "{";
			for (int i = 0; i < stringArray.length; i += 2) {
				result += (' ' + stringArray[i] + ':' + stringArray[i + 1] + ',');
			}
			result = result.substring(0, result.length() - 1);
			result += '}';
			return result;
		}
		return "";
	}
}
