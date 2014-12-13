package com.aspirephile.physim.scenes;

import com.aspirephile.physim.R;
import com.aspirephile.physim.R.id;
import com.aspirephile.physim.R.layout;
import com.aspirephile.physim.R.menu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SceneCreator extends ActionBarActivity {

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

}