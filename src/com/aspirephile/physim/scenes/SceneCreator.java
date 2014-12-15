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

import com.aspirephile.debug.Logger;
import com.aspirephile.debug.NullPointerAsserter;
import com.aspirephile.physim.R;

public class SceneCreator extends ActionBarActivity implements OnClickListener {
	NullPointerAsserter asserter = new NullPointerAsserter(
			SceneCreatorFragment.class);
	Logger l = new Logger(SceneCreatorFragment.class);

	Button done;

	SceneCreatorFragment sceneCreatorF;

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.b_scene_creator_done:
			Intent i=new Intent();
			setResult(Activity.RESULT_OK, i);
			finish();
			break;
		default:
			l.w("Unknown button clicked (id: " + v.getId() + ")");
			break;
		}
	}
}
