package com.aspirephile.physim.scenes.db;

import com.aspirephile.debug.NullPointerAsserter;

public class ScenesDB {
	NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesDB.class.getName());

	protected static class properties {
		protected static final String DATABASE_NAME = "World";
		protected static final int DATABASE_VERSION = 1;
	}

	public static class tables {
		public static class scenes {
			protected static final String name = "Scenes";

			public static class column {

				public static final String ROWID = "_id";
				public static final String NAME = "name";
				public static final String BOUNDED = "bounded";
				public static final String CONTINENT = "continent";
				public static final String REGION = "region";
			}

			protected static final String[] columns = { column.ROWID,
					column.NAME, column.BOUNDED, column.CONTINENT,
					column.REGION };

			protected static final class commands {
				protected static final String create = "CREATE TABLE if not exists "
						+ scenes.name
						+ " ("
						+ column.ROWID
						+ " integer PRIMARY KEY autoincrement,"
						+ column.BOUNDED
						+ ","
						+ column.NAME
						+ ","
						+ column.CONTINENT
						+ ","
						+ column.REGION
						+ ","
						+ " UNIQUE (" + column.BOUNDED + "));";
				protected static final String drop = "DROP TABLE IF EXISTS "
						+ scenes.name;
			}

		}
	}

}