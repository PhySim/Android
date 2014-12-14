package com.aspirephile.physim.scenes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.debug.Logger;
import com.aspirephile.physim.engine.Scene;

public class ScenesDBAdapter {
	Logger l = new Logger(ScenesDBAdapter.class);

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

	public long insertScene(Scene scene) {

		l.d("Inserting scene: " + scene.toString());

		ContentValues initialValues = new ContentValues();
		initialValues.put(ScenesDB.tables.scenes.column.NAME, scene.getName());
		initialValues.put(ScenesDB.tables.scenes.column.BOUNDED,
				scene.getBounded());
		initialValues.put(ScenesDB.tables.scenes.column.CONTINENT,
				"Sample continent");
		initialValues
				.put(ScenesDB.tables.scenes.column.REGION, "Sample region");
		return db.insert(ScenesDB.tables.scenes.name, null, initialValues);
	}

	public boolean deleteAllCountries() {

		int doneDelete = 0;
		doneDelete = db.delete(ScenesDB.tables.scenes.name, null, null);
		l.w(Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public Cursor fetchCountriesByName(String inputText) throws SQLException {
		l.w(inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = query(ScenesDB.tables.scenes.name,
					ScenesDB.tables.scenes.columns, null, null, null, null,
					null);

		} else {
			mCursor = db.query(true, ScenesDB.tables.scenes.name,
					ScenesDB.tables.scenes.columns,
					ScenesDB.tables.scenes.column.NAME + " like '%" + inputText
							+ "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchAllScenes() {
		Cursor mCursor = query(ScenesDB.tables.scenes.name,
				ScenesDB.tables.scenes.columns, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy)

	{
		l.d("Querying database: " + ScenesDB.properties.DATABASE_NAME
				+ ", table: " + table + ", selection: " + selection
				+ ", selectionArgs: " + selectionArgs.toString()
				+ ", groupBy: " + groupBy + ", having: " + having
				+ ", orderBy: " + orderBy);
		return db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
	}
}
