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
		l.d("Openning DB: " + ScenesDB.properties.DATABASE_NAME);
		dbHelper = new ScenesDBHelper(mCtx);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		l.d("Closing DB: " + ScenesDB.properties.DATABASE_NAME);
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public long insertScene(Scene scene) throws SceneLockException,
			NullPointerException {

		l.d("Inserting scene: " + scene.toString());

		try {
			if (scene.isLocked()) {
				ContentValues initialValues = new ContentValues();
				initialValues.put(ScenesDB.tables.scenes.column.NAME,
						scene.getName());
				initialValues.put(ScenesDB.tables.scenes.column.BOUNDED,
						(scene.getBounded() ? 1 : 0));
				initialValues.put(ScenesDB.tables.scenes.column.CONTINENT,
						"Sample continent");
				initialValues.put(ScenesDB.tables.scenes.column.REGION,
						"Sample region");
				long result = db.insert(ScenesDB.tables.scenes.name, null,
						initialValues);
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
		doneDelete = db.delete(ScenesDB.tables.scenes.name, null, null);
		l.w(Integer.toString(doneDelete));
		return doneDelete > 0;

	}

	public Cursor fetchCountriesByName(String inputText) throws SQLException {
		l.w(inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = query(ScenesDB.tables.scenes.name,
					ScenesDB.tables.scenes.allColumns, null, null, null, null,
					null);

		} else {
			mCursor = db.query(true, ScenesDB.tables.scenes.name,
					ScenesDB.tables.scenes.allColumns,
					ScenesDB.tables.scenes.column.NAME + " like '%" + inputText
							+ "%'", null, null, null, null, null);
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
		l.d("Querying database: " + ScenesDB.properties.DATABASE_NAME
				+ ", table: " + asserted(table) + ", selection: "
				+ asserted(selection) + ", selectionArgs: "
				+ asserted(selectionArgs) + ", groupBy: " + asserted(groupBy)
				+ ", having: " + asserted(having) + ", orderBy: "
				+ asserted(orderBy));
		if (asserter.assertPointer(db))
			return db.query(table, columns, selection, selectionArgs, groupBy,
					having, orderBy);
		else
			return null;
	}

	private String asserted(String s) {
		return (asserter.assertPointerQuietly(s) == true ? s : "");
	}

	private String[] asserted(String[] s) {
		String[] emptyStringArray = { "", "" };
		return (asserter.assertPointerQuietly((Object[]) s) == true ? ((String[]) s)
				: emptyStringArray);
	}

	public Cursor fetchAllScenes() {
		return queryScenes(ScenesDB.tables.scenes.allColumns);
	}

	public Cursor fetchAllSceneNames() {
		String[] columns = { ScenesDB.tables.scenes.column.ROWID,
				ScenesDB.tables.scenes.column.NAME };
		return queryScenes(columns);
	}

	public Cursor queryScenes(String[] columns) {
		Cursor mCursor = query(ScenesDB.tables.scenes.name, columns, null,
				null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor getSceneByID(String sceneID) {
		return db.query(ScenesDB.properties.DATABASE_NAME,
				ScenesDB.tables.scenes.allColumns, "_ID=?",
				new String[] { sceneID }, null, null,
				ScenesDB.tables.scenes.column.NAME + " asc ");
	}

	public int update(ContentValues contentValues, String contactID) {
		int cnt = db.update(ScenesDB.properties.DATABASE_NAME, contentValues,
				"_id=?", new String[] { contactID });
		return cnt;
	}

	public long insert(ContentValues values) {
		long rowID = db.insert(ScenesDB.properties.DATABASE_NAME, null, values);
		return rowID;
	}

	public int delete(String contactID) {
		// TODO Implement protection from SQL injections
		int cnt = db.delete(ScenesDB.tables.scenes.name, "_id=" + contactID,
				null);
		return cnt;
	}

}
