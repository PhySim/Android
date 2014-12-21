package com.aspirephile.physim.scenes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

class ScenesDBHelper extends SQLiteOpenHelper {
	NullPointerAsserter asserter = new NullPointerAsserter(ScenesDBHelper.class);
	Logger l = new Logger(ScenesDBHelper.class);

	ScenesDBHelper(Context context) {
		super(context, ScenesDBProps.v2.properties.DATABASE_NAME, null,
				ScenesDBProps.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		l.d("Creating SceneDB");
		execSQL(db, ScenesDBProps.v2.tables.scenes.commands.create);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion < oldVersion) {
			switch (oldVersion) {
			// cases from 1 to current db...
			case 3:
				if (newVersion == 3)
					return;
				break;
			case 2:
				l.w("Executing downgrade from "
						+ ScenesDBProps.v2.properties.DATABASE_NAME
						+ " (v"
						+ ScenesDBProps.DATABASE_VERSION
						+ "): Removing columns: created, last_modified and Adding columns: bounded, continent");
				// ************** do not change db version class!!! ***********
				execSQL(db, ScenesDBProps.v2.tables.scenes.commands.downgrade());
			case 1:
				break;

			default:
				l.e("Unexpected case in which DB is being downgraded from oldVersion="
						+ oldVersion
						+ " and newVersion="
						+ newVersion
						+ ", attempting to recover to v"
						+ ScenesDBProps.v2.properties.DATABASE_VERSION);
				try {
					execSQL(db,
							ScenesDBProps.v2.tables.scenes.commands
									.recover(System.currentTimeMillis()));
				} catch (SQLiteException e) {
					e.printStackTrace();
					l.e("Attempt to upgrade failed, so recreating database!");
					// TODO warn the user before this process
					recreateDatabase(db);
				}
				if (newVersion == 2)
					return;
				break;
			}
		} else {
			l.e("Unexpected case in which DB is being downgraded from oldVersion and newVersion of "
					+ newVersion + ", so table is being recreated");
			recreateDatabase(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		l.i("Database upgrade initiating");
		if (newVersion > oldVersion) {
			try {
				switch (oldVersion) {
				// cases from 1 to current db...
				case 1:
					l.w("Executing upgrade from "
							+ ScenesDBProps.v1.properties.DATABASE_NAME
							+ " (v"
							+ ScenesDBProps.v1.properties.DATABASE_VERSION
							+ "): Removing columns: bounded, continent, region and Adding columns: created, last_modified");
					// ************** do not change db version class!!!
					// ***********
					execSQL(db,
							ScenesDBProps.v1.tables.scenes.commands
									.upgrade(System.currentTimeMillis()));
					break;

				default:
					l.e("Unexpected case in which DB is being upgraded from oldVersion="
							+ oldVersion
							+ " and newVersion="
							+ newVersion
							+ ", attempting to recover to v"
							+ ScenesDBProps.v2.properties.DATABASE_VERSION);
					execSQL(db,
							ScenesDBProps.v2.tables.scenes.commands
									.recover(System.currentTimeMillis()));
					l.w("Attempt to upgrade failed, so recreating database!");
					// TODO warn the user before this process
					recreateDatabase(db);
					break;
				}
			} catch (SQLiteException e) {
				e.printStackTrace();
			}
		} else {
			Log.e(ScenesDBHelper.class.getName(),
					"Unexpected case in which DB is being upgraded from oldVersion and newVersion of "
							+ newVersion + ", so table is being recreated");
			recreateDatabase(db);
		}
		Log.i(ScenesDBHelper.class.getName(), "Database upgrade completed");
	}

	public void execSQL(SQLiteDatabase db, String command) {

		l.w(command);
		db.execSQL(command);
	}

	private void execSQL(SQLiteDatabase db, String[] commands) {
		if (asserter.assertPointer((Object[]) commands)) {
			for (String command : commands)
				execSQL(db, command);
		}
	}

	public void recreateDatabase(SQLiteDatabase db) {
		l.w("Scenes table being recreated");
		destroyDatabase(db);
		onCreate(db);
		l.w("Scenes table recreation complete");
	}

	private void destroyDatabase(SQLiteDatabase db) {
		l.w("Scenes table being destroyed");
		execSQL(db, ScenesDBProps.v2.tables.scenes.commands.drop);
	}
}
