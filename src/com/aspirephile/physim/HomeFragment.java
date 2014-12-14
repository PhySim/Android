package com.aspirephile.physim;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.aspirephile.debug.NullPointerAsserter;
import com.aspirephile.physim.engine.Scene;
import com.aspirephile.physim.scenes.db.ScenesCursorAdapter;
import com.aspirephile.physim.scenes.db.ScenesDB;
import com.aspirephile.physim.scenes.db.ScenesDBAdapter;

public class HomeFragment extends Fragment {
	NullPointerAsserter asserter = new NullPointerAsserter(HomeFragment.class);
	ListView scenes;
	ScenesCursorAdapter scenesAdapter;
	ScenesDBAdapter dbAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		bridgeXML(v);
		intializeFeilds();
		displayListView();
		return v;
	}

	private void bridgeXML(View v) {
		scenes = (ListView) v.findViewById(R.id.lv_scene_selection);
		asserter.assertPointer(scenes);
	}

	private void intializeFeilds() {
		dbAdapter = new ScenesDBAdapter(this.getActivity());
		if (asserter.assertPointer(dbAdapter)) {
			dbAdapter.open();
			insertScenes();
		}

	}

	private void insertScenes() {
		Scene scene1 = new Scene();
		scene1.setName("Planetary");
		dbAdapter.insertScene(scene1);
		Scene scene2 = new Scene();
		scene2.setName("Cellular");
		scene2.lock();
		dbAdapter.insertScene(scene2);
	}

	private void displayListView() {

		Cursor cursor = dbAdapter.fetchAllScenes();
		scenesAdapter = new ScenesCursorAdapter(getActivity(), cursor, 0);
		scenes.setAdapter(scenesAdapter);

		scenes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				// Get the state's capital from this row in the database.
				String sceneName = cursor.getString(cursor
						.getColumnIndexOrThrow(ScenesDB.tables.scenes.column.NAME));
				Toast.makeText(getActivity(), sceneName, Toast.LENGTH_SHORT)
						.show();

			}
		});

		// TODO Add a search box for scenes

		/*
		 * EditText myFilter = (EditText) findViewById(R.id.myFilter);
		 * myFilter.addTextChangedListener(new TextWatcher() {
		 * 
		 * public void afterTextChanged(Editable s) { }
		 * 
		 * public void beforeTextChanged(CharSequence s, int start, int count,
		 * int after) { }
		 * 
		 * public void onTextChanged(CharSequence s, int start, int before, int
		 * count) { scenesAdapter.getFilter().filter(s.toString()); } });
		 * 
		 * scenesAdapter.setFilterQueryProvider(new FilterQueryProvider() {
		 * public Cursor runQuery(CharSequence constraint) { return
		 * dbAdapter.fetchCountriesByName(constraint.toString()); } });
		 */

	}
}