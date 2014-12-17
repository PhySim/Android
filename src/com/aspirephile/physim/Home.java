package com.aspirephile.physim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aspirephile.debug.Logger;
import com.aspirephile.physim.engine.Scene;
import com.aspirephile.physim.scenes.SceneCreator;

public class Home extends ActionBarActivity {
	Logger l = new Logger(Home.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new HomeFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_add_scene:
			Intent i = new Intent(getApplicationContext(), SceneCreator.class);
			startActivityForResult(i, PhySim.codes.sceneCreate);
			break;
		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		l.d("Home activity received requestCode: " + requestCode
				+ ", resultCode: " + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PhySim.codes.sceneCreate:
				Bundle sceneInfo=data.getBundleExtra(PhySim.keys.sceneCreatorBundle);
				Scene scene=new Scene(sceneInfo);
				
				break;

			default:
				break;
			}
		} else {
		}

	}
}
