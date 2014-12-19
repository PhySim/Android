package com.aspirephile.physim;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.aspirephile.physim.scenes.db.ScenesCursorAdapter;
import com.aspirephile.physim.scenes.db.ScenesDBProps;
import com.aspirephile.physim.scenes.db.ScenesProvider;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class HomeFragment extends Fragment implements OnItemClickListener,
		LoaderManager.LoaderCallbacks<Cursor> {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			HomeFragment.class);
	private Logger l = new Logger(HomeFragment.class);
	private ListView scenes;
	private ScenesCursorAdapter scenesAdapter;

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
		getLoaderManager().initLoader(PhySimProps.loaders.scenesLoader, null,
				this);

	}

	private void displayListView() {

		// Cursor cursor = dbHandler.fetchAllSceneNames();

		// TODO Asyncronously fetch scene names

		scenesAdapter = new ScenesCursorAdapter(getActivity(), null, 0);
		scenes.setAdapter(scenesAdapter);

		scenes.setOnItemClickListener(this);

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
		 * dbHandler.fetchCountriesByName(constraint.toString()); } });
		 */

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == PhySimProps.loaders.scenesLoader) {
			Uri uri = ScenesProvider.CONTENT_URI;
			uri = Uri.withAppendedPath(uri, ScenesDBProps.v1.tables.scenes.column.NAME);
			l.d("Instantiating new loader with id: " + id + "and URI: " + uri);
			return new CursorLoader(getActivity(), uri, null, null, null, null);
		} else
			return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		l.d("Cursor load finished");
		scenesAdapter.swapCursor(data);
		if (asserter.assertPointer(data)) {
			data.moveToFirst();
			while (data.moveToNext()) {
				l.d("Cursor position: "
						+ data.getPosition()
						+ " contains scene name: "
						+ data.getString(data
								.getColumnIndex(ScenesDBProps.v1.tables.scenes.column.NAME)));
			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		l.d("Cursor loader reset");
		scenesAdapter.swapCursor(null);

	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position,
			long id) {
		// Get the cursor, positioned to the corresponding row in the
		// result set
		Cursor cursor = (Cursor) listView.getItemAtPosition(position);

		// Get the state's capital from this row in the database.
		String sceneName = cursor.getString(cursor
				.getColumnIndexOrThrow(ScenesDBProps.v1.tables.scenes.column.NAME));
		Toast.makeText(getActivity(), sceneName + " having id: " + id,
				Toast.LENGTH_SHORT).show();
	}
}