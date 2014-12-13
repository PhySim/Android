package com.aspirephile.physim.scenes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aspirephile.physim.engine.Scene;

public class ScenesDBAdapter {

	private ScenesDBHelper dbHelper;
	private SQLiteDatabase db;

	private final Context mCtx;

	public ScenesDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public ScenesDBAdapter open() throws SQLException {
		dbHelper = new ScenesDBHelper(mCtx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public long createScene(Scene scene) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(ScenesDB.keys.NAME, scene.getName());
		initialValues.put(ScenesDB.keys.BOUNDED, scene.getBounded());
		initialValues.put(ScenesDB.keys.CONTINENT, "Sample continent");
		initialValues.put(ScenesDB.keys.REGION, "Sample region");

		return db
				.insert(ScenesDB.properties.tables.SCENES, null, initialValues);
	}

	public boolean deleteAllCountries() {

		int doneDelete = 0;
		doneDelete = db.delete(ScenesDB.properties.tables.SCENES, null, null);
		Log.w(ScenesDB.TAG, Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public Cursor fetchCountriesByName(String inputText) throws SQLException {
		Log.w(ScenesDB.TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = db.query(ScenesDB.properties.tables.SCENES, new String[] {
					ScenesDB.keys.ROWID, ScenesDB.keys.BOUNDED,
					ScenesDB.keys.NAME, ScenesDB.keys.CONTINENT,
					ScenesDB.keys.REGION }, null, null, null, null, null);

		} else {
			mCursor = db.query(true, ScenesDB.properties.tables.SCENES,
					new String[] { ScenesDB.keys.ROWID, ScenesDB.keys.BOUNDED,
							ScenesDB.keys.NAME, ScenesDB.keys.CONTINENT,
							ScenesDB.keys.REGION }, ScenesDB.keys.NAME
							+ " like '%" + inputText + "%'", null, null, null,
					null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchAllScenes() {

		Cursor mCursor = db.query(ScenesDB.properties.tables.SCENES,
				new String[] { ScenesDB.keys.ROWID, ScenesDB.keys.BOUNDED,
						ScenesDB.keys.NAME, ScenesDB.keys.CONTINENT,
						ScenesDB.keys.REGION }, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
