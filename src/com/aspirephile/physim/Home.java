package com.aspirephile.physim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aspirephile.physim.engine.Scene;
import com.aspirephile.physim.scenes.SceneCreator;
import com.aspirephile.physim.scenes.db.ScenesProvider;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class Home extends ActionBarActivity {
	private NullPointerAsserter asserter = new NullPointerAsserter(Home.class);
	private Logger l = new Logger(Home.class);
	private HomeFragment homeF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			homeF = new HomeFragment();
			if (homeF != null) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, homeF).commit();
			}
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
			startActivityForResult(i, PhySimProps.codes.sceneCreate);
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
			case PhySimProps.codes.sceneCreate:
				Bundle sceneInfo = data
						.getBundleExtra(PhySimProps.keys.sceneCreatorBundle);
				if (asserter.assertPointer(sceneInfo)) {
					Scene scene = new Scene(sceneInfo);
					if (asserter.assertPointer(scene)) {

						if (!scene.isLocked())
							scene.lock();
						getContentResolver().insert(ScenesProvider.CONTENT_URI,
								scene.toContentValues());
						// homeF.insertScene(scene);
					}
				}

				break;

			default:
				break;
			}
		} else {
		}

	}
}
