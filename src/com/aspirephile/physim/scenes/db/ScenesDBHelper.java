package com.aspirephile.physim.scenes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ScenesDBHelper extends SQLiteOpenHelper {

	ScenesDBHelper(Context context) {
		super(context, ScenesDB.properties.DATABASE_NAME, null,
				ScenesDB.properties.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(ScenesDB.TAG, ScenesDB.commands.DATABASE_CREATE);
		db.execSQL(ScenesDB.commands.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(ScenesDBHelper.class.getName(), "Database upgrade initiating");
		if (oldVersion < newVersion) {
			switch (oldVersion) {
			// cases from 1 to current db...

			default:
				Log.w(ScenesDBHelper.class.getName(),
						"Unexpected case in which DB is being upgraded from oldVersion="
								+ oldVersion + " and newVersion=" + newVersion
								+ ", so table is being recreated");
				db.execSQL("DROP TABLE IF EXISTS "
						+ ScenesDB.properties.tables.SCENES);
				onCreate(db);
				break;
			}
		} else if (oldVersion > newVersion) {
			switch (oldVersion) {
			// cases from 1 to current db...

			default:
				Log.w(ScenesDBHelper.class.getName(),
						"Unexpected case in which DB is being downgraded from oldVersion="
								+ oldVersion + " and newVersion=" + newVersion
								+ ", so table is being recreated");
				db.execSQL("DROP TABLE IF EXISTS "
						+ ScenesDB.properties.tables.SCENES);
				onCreate(db);
				break;
			}
		} else {
			Log.w(ScenesDBHelper.class.getName(),
					"Unexpected case in which DB is being upgraded from oldVersion and newVersion of "
							+ newVersion);
			db.execSQL(ScenesDB.commands.dropTable.TABLE_SCENES);
			onCreate(db);
		}
		Log.i(ScenesDBHelper.class.getName(), "Database upgrade completed");
	}
}
