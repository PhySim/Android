package com.aspirephile.physim.scenes.db;

import com.aspirephile.shared.debug.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ScenesDBHelper extends SQLiteOpenHelper {
	Logger l = new Logger(ScenesDBHelper.class);

	ScenesDBHelper(Context context) {
		super(context, ScenesDB.properties.DATABASE_NAME, null,
				ScenesDB.properties.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		execSQL(db, ScenesDB.tables.scenes.commands.create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		l.i("Database upgrade initiating");
		if (oldVersion < newVersion) {
			switch (oldVersion) {
			// cases from 1 to current db...

			default:
				Log.w(ScenesDBHelper.class.getName(),
						"Unexpected case in which DB is being upgraded from oldVersion="
								+ oldVersion + " and newVersion=" + newVersion
								+ ", so table is being recreated");
				recreateScenesTable(db);
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
				recreateScenesTable(db);
				break;
			}
		} else {
			Log.w(ScenesDBHelper.class.getName(),
					"Unexpected case in which DB is being upgraded from oldVersion and newVersion of "
							+ newVersion + ", so table is being recreated");
			recreateScenesTable(db);
		}
		Log.i(ScenesDBHelper.class.getName(), "Database upgrade completed");
	}

	public void execSQL(SQLiteDatabase db, String command) {

		l.w(command);
		db.execSQL(command);
	}

	public void recreateScenesTable(SQLiteDatabase db) {
		l.w("Scenes table being recreated");
		execSQL(db, ScenesDB.tables.scenes.commands.drop);
		onCreate(db);
		l.w("Scenes table recreation complete");
	}
}
