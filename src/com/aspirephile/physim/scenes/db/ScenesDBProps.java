package com.aspirephile.physim.scenes.db;

import com.aspirephile.shared.debug.NullPointerAsserter;

public class ScenesDBProps {
	NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesDBProps.class.getName());

	protected static final int DATABASE_VERSION = 1;

	public static class v1 {

		public static class properties {
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

				public static final String[] allColumns = { column.ROWID,
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
							+ " UNIQUE (" + column.NAME + "));";
					protected static final String drop = "DROP TABLE IF EXISTS "
							+ scenes.name;
				}

			}
		}

	}

	public static class v2 {

		protected static class properties {
			protected static final String DATABASE_NAME = "World";
			protected static final int DATABASE_VERSION = 1;
		}

		public static class tables {
			public static class scenes {
				protected static final String name = "Scenes";

				/*
				 * If changing table structure, remember to: - change the create
				 * command
				 */

				public static class column {

					public static final String ROWID = "_id";
					public static final String NAME = "name";
					public static final String LAST_MODIFIED = "last_modified";
				}

				public static class columnTypes {

					public static final String ROWID = "integer PRIMARY KEY autoincrement";
					public static final String NAME = "";
					public static final String LAST_MODIFIED = "integer";
				}

				public static class columnDefinition {

					public static final String ROWID = getColumnDefinition(
							column.ROWID, columnTypes.ROWID);
					public static final String NAME = getColumnDefinition(
							column.NAME, columnTypes.NAME);
					public static final String LAST_MODIFIED = getColumnDefinition(
							column.LAST_MODIFIED, columnTypes.LAST_MODIFIED);

					private static String getColumnDefinition(String column,
							String columnDefinition) {
						return column + " " + columnDefinition;
					}
				}

				public static class columnSpecification {
					public static final String NAME = getColumnSpecification(
							column.NAME, new String[] { "UNIQUE" });

					private static String getColumnSpecification(
							String columnName, String[] specifications) {
						String result = specifications[0];
						result += " (" + columnName + ")";
						return result;
					}
				}

				public static final String[] allColumns = { column.ROWID,
						column.NAME, column.LAST_MODIFIED };

				protected static final class commands {
					protected static final String create = "CREATE TABLE if not exists "
							+ scenes.name
							+ " ("
							+ columnDefinition.ROWID
							+ ", "
							+ columnDefinition.LAST_MODIFIED
							+ ", "
							+ column.NAME
							+ ", "
							+ columnSpecification.NAME
							+ ");";
					protected static final String drop = "DROP TABLE IF EXISTS "
							+ scenes.name;
				}

			}
		}

	}

}