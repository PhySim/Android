package com.aspirephile.physim.scenes.db;

import com.aspirephile.debug.NullPointerAsserter;

public class ScenesDB {
	NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesDB.class.getName());

	protected static final String TAG = "SCENES_DB";

	public static class keys {

		public static final String ROWID = "_id";
		public static final String NAME = "name";
		public static final String BOUNDED = "bounded";
		public static final String CONTINENT = "continent";
		public static final String REGION = "region";
	}

	protected static class properties {
		protected static final String DATABASE_NAME = "World";

		protected static class tables {
			protected static final String SCENES = "Scenes";
		}

		protected static final int DATABASE_VERSION = 1;

	}

	protected static class commands {
		protected static final String DATABASE_CREATE = "CREATE TABLE if not exists "
				+ ScenesDB.properties.tables.SCENES
				+ " ("
				+ ScenesDB.keys.ROWID
				+ " integer PRIMARY KEY autoincrement,"
				+ ScenesDB.keys.BOUNDED
				+ ","
				+ ScenesDB.keys.NAME
				+ ","
				+ ScenesDB.keys.CONTINENT
				+ ","
				+ ScenesDB.keys.REGION
				+ ","
				+ " UNIQUE (" + ScenesDB.keys.BOUNDED + "));";

		protected static class dropTable {
			protected static final String TABLE_SCENES = "DROP TABLE IF EXISTS "
					+ properties.tables.SCENES;
		}
	}

}
