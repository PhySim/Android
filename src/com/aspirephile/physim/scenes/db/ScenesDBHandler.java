package com.aspirephile.physim.scenes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.aspirephile.physim.engine.Scene;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;
import com.aspirephile.shared.exception.SceneLockException;
import com.aspirephile.shared.utils.StringManipulator;

public class ScenesDBHandler {
	private NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesDBHandler.class);
	private StringManipulator stringManip = new StringManipulator(asserter);
	private Logger l = new Logger(ScenesDBHandler.class);

	private ScenesDBHelper dbHelper;
	private SQLiteDatabase db;

	private final Context mCtx;

	public ScenesDBHandler(Context ctx) {
		this.mCtx = ctx;
	}

	public ScenesDBHandler open() throws SQLException {
		l.d("Openning DB: " + ScenesDBProps.v2.properties.DATABASE_NAME);
		dbHelper = new ScenesDBHelper(mCtx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		l.d("Closing DB: " + ScenesDBProps.v2.properties.DATABASE_NAME);
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public long insertScene(Scene scene) throws SceneLockException,
			NullPointerException {

		l.d("Inserting scene: " + scene.toString());

		try {
			if (scene.isLocked()) {
				scene.formatData();
				ContentValues initialValues = new ContentValues();
				initialValues.put(ScenesDBProps.v2.tables.scenes.column.NAME,
						scene.getName());
				initialValues.put(
						ScenesDBProps.v2.tables.scenes.column.CREATED,
						scene.getCreated());
				initialValues.put(
						ScenesDBProps.v2.tables.scenes.column.LAST_MODIFIED,
						scene.getLastModified());
				long result = db.insert(ScenesDBProps.v2.tables.scenes.name,
						null, initialValues);
				l.d("Scene inserted successfully with result: " + result);
				return result;
			} else
				throw new SceneLockException(scene);
		} catch (NullPointerException e) {
			throw e;
		}
	}

	public boolean deleteAllCountries() {

		int doneDelete = 0;
		doneDelete = db.delete(ScenesDBProps.v2.tables.scenes.name, null, null);
		l.w(Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public Cursor fetchScenesByName(String inputText) throws SQLException {
		l.w(inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = query(ScenesDBProps.v2.tables.scenes.name,
					ScenesDBProps.v2.tables.scenes.allColumns, null, null,
					null, null, null);

		} else {
			mCursor = db.query(true, ScenesDBProps.v2.tables.scenes.name,
					ScenesDBProps.v2.tables.scenes.allColumns,
					ScenesDBProps.v2.tables.scenes.column.NAME + " like '%?%'",
					new String[] { inputText }, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	private Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy)

	{
		l.d("Querying database: " + ScenesDBProps.v2.properties.DATABASE_NAME
				+ ", table: " + asserted(table) + ", columns: "
				+ stringManip.getFormatedStringArrayQuietly(columns)
				+ ", selection: " + asserted(selection) + ", selectionArgs: "
				+ stringManip.getFormatedStringArrayQuietly(selectionArgs)
				+ ", groupBy: " + asserted(groupBy) + ", having: "
				+ asserted(having) + ", orderBy: " + asserted(orderBy));
		if (asserter.assertPointer(db))
			return db.query(table, columns, selection, selectionArgs, groupBy,
					having, orderBy);
		else
			return null;
	}

	private String asserted(String s) {
		return (asserter.assertPointerQuietly(s) == true ? s : "null");
	}

	public Cursor fetchAllScenes() {
		l.d("Fetching all scenes(columns: "
				+ stringManip
						.getFormatedStringArray(ScenesDBProps.v2.tables.scenes.allColumns));
		return queryScenes(ScenesDBProps.v2.tables.scenes.allColumns);
	}

	public Cursor fetchAllSceneNames() {
		String[] columns = { ScenesDBProps.v2.tables.scenes.column.ROWID,
				ScenesDBProps.v2.tables.scenes.column.NAME };
		l.d("Fetching all scene names(columns: "
				+ stringManip.getFormatedStringArray(columns));
		return queryScenes(columns);
	}

	public Cursor queryScenes(String[] columns) {
		l.d("Attempting to query scenes with columns: "
				+ stringManip.getFormatedStringArray(columns));
		Cursor mCursor = query(ScenesDBProps.v2.tables.scenes.name, columns,
				null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor getSceneByID(String sceneID) {
		l.d("Querying scene with sceneID: " + sceneID);
		return db.query(ScenesDBProps.v2.tables.scenes.name,
				ScenesDBProps.v2.tables.scenes.allColumns, "_ID=?",
				new String[] { sceneID }, null, null,
				ScenesDBProps.v2.tables.scenes.column.NAME + " asc ");
	}

	public int update(ContentValues contentValues, String sceneID) {
		l.d("Attempting to update scene with ID: " + sceneID
				+ " and content values: " + contentValues);
		int cnt = db.update(ScenesDBProps.v2.tables.scenes.name, contentValues,
				"_id=?", new String[] { sceneID });
		return cnt;
	}

	public long insert(Scene scene) {
		if (asserter.assertPointer(scene)) {
			scene.formatData();
			return insert(scene.toContentValues());
		}
		return -1;
	}

	public long insert(ContentValues values) {
		l.d("Attempting to insert scene with content values: " + values);
		long rowID = db.insert(ScenesDBProps.v2.tables.scenes.name, null,
				values);
		return rowID;
	}

	public int delete(String sceneID) {
		// TODO Implement protection from SQL injections
		l.d("Attempting to delete scene with ID: " + sceneID);
		int cnt = db.delete(ScenesDBProps.v2.tables.scenes.name, "_id=?",
				new String[] { sceneID });
		l.d(cnt + " rows deleted");
		return cnt;
	}

}
