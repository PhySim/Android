package com.aspirephile.physim;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.aspirephile.physim.engine.Scene;
import com.aspirephile.physim.scenes.db.ScenesCursorAdapter;
import com.aspirephile.physim.scenes.db.ScenesDB;
import com.aspirephile.physim.scenes.db.ScenesDBAdapter;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;
import com.aspirephile.shared.exception.SceneLockException;

public class HomeFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			HomeFragment.class);
	private Logger l = new Logger(HomeFragment.class);
	private ListView scenes;
	private ScenesCursorAdapter scenesAdapter;
	private ScenesDBAdapter dbAdapter;

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
			// insertScenes();
		}
		getLoaderManager().initLoader(0, null, this);

	}

	private void insertScenes() {
		Scene scene1 = new Scene();
		scene1.setName("Planetary");
		scene1.lock();
		Scene scene2 = new Scene();
		scene2.setName("Cellular");
		scene2.lock();
		try {
			dbAdapter.insertScene(scene1);
			dbAdapter.insertScene(scene2);
		} catch (SceneLockException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
			// TODO Find method to indicate to the user when a scene name
			// is not unique
			/*
			 * Toast.makeText( getActivity(), getActivity().getResources()
			 * .getString(R.string.hello_world), Toast.LENGTH_LONG) .show();
			 */
		}

	}

	private void displayListView() {

		// Cursor cursor = dbAdapter.fetchAllSceneNames();

		// TODO Asyncronously fetch scene names

		scenesAdapter = new ScenesCursorAdapter(getActivity(), null, 0);
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

	public void insertScene(Scene scene) {
		if (asserter.assertPointer(scene)) {
			try {
				dbAdapter.insertScene(scene);
			} catch (SceneLockException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), uri, projection, selection, selectionArgs, sortOrder).fetchAllSceneNames();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		scenesAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		scenesAdapter.swapCursor(null);

	}
}