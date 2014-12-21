package com.aspirephile.physim.scenes.db;

import com.aspirephile.shared.debug.NullPointerAsserter;

public class ScenesDBProps {
	NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesDBProps.class.getName());

	protected static final int DATABASE_VERSION = 2;

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

				public static class columnDefinition {
					protected static final String CONTINENT = column.CONTINENT
							+ "";
					public static final String REGION = column.REGION + "";
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

					protected static class alter {
						protected static final String addContinentColumn = "alter table "
								+ tables.scenes.name
								+ " add "
								+ columnDefinition.CONTINENT + ";";;
						protected static final String addRegionColumn = "alter table "
								+ tables.scenes.name
								+ " add "
								+ columnDefinition.REGION + ";";;
					}

					public static String[] upgrade(long currentTimeMillis) {
						return new String[] {
								"begin transaction;",
								"create table "
										+ v2.tables.scenes.name
										+ "("
										+ getCommaSeparatedColumns(new String[] {
												v2.tables.scenes.column.ROWID,
												v2.tables.scenes.column.NAME })
										+ ")" + ";",
								v2.tables.scenes.commands.alter.addCreatedColumn,
								v2.tables.scenes.commands.alter.addLastModifiedColumn,
								"insert into "
										+ v2.tables.scenes.name
										+ " select "
										+ getCommaSeparatedColumns(new String[] {
												scenes.column.ROWID,
												scenes.column.NAME,
												Long.toString(currentTimeMillis),
												Long.toString(currentTimeMillis) })
										+ " from " + tables.scenes.name,
								"drop table " + tables.scenes.name + ";",
								"commit;" };
					}

				}

			}
		}

	}

	public static class v2 {

		protected static class properties {
			protected static final String DATABASE_NAME = "World";
			protected static final int DATABASE_VERSION = 2;
		}

		public static class tables {
			public static class scenes {
				protected static final String name = "Scenes_v"
						+ properties.DATABASE_VERSION;

				/*
				 * If changing table structure, remember to: - change the create
				 * command - add the appropriate columnType - add the
				 * appropriate columnDefinition
				 */

				public static class column {

					public static final String ROWID = "_id";
					public static final String NAME = "name";
					public static final String CREATED = "created";
					public static final String LAST_MODIFIED = "last_modified";
				}

				public static class columnType {

					public static final String ROWID = "integer PRIMARY KEY autoincrement";
					public static final String NAME = "";
					public static final String CREATED = "integer";
					public static final String LAST_MODIFIED = "integer";
				}

				public static class columnDefinition {

					public static final String ROWID = column.ROWID + " "
							+ columnType.ROWID;
					public static final String NAME = column.NAME + " "
							+ columnType.NAME;
					public static final String CREATED = column.CREATED + " "
							+ columnType.CREATED;
					public static final String LAST_MODIFIED = column.LAST_MODIFIED
							+ " " + columnType.LAST_MODIFIED;

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
						column.NAME, column.CREATED, column.LAST_MODIFIED };

				protected static final class commands {
					protected static final String create = "CREATE TABLE if not exists "
							+ scenes.name
							+ " ("
							+ columnDefinition.ROWID
							+ ", "
							+ columnDefinition.CREATED
							+ ", "
							+ columnDefinition.LAST_MODIFIED
							+ ", "
							+ column.NAME
							+ ", "
							+ columnSpecification.NAME
							+ ");";
					protected static final String drop = "DROP TABLE IF EXISTS "
							+ scenes.name;

					protected static class alter {
						protected static final String addCreatedColumn = "alter table "
								+ tables.scenes.name
								+ " add "
								+ columnDefinition.CREATED + ";";
						protected static final String addLastModifiedColumn = "alter table "
								+ tables.scenes.name
								+ " add "
								+ columnDefinition.LAST_MODIFIED + ";";
					}

					public static String[] downgrade() {
						return new String[] {
								"begin transaction;",
								"create table if not exists "
										+ v1.tables.scenes.name
										+ "("
										+ getCommaSeparatedColumns(new String[] {
												v1.tables.scenes.column.ROWID,
												v1.tables.scenes.column.NAME })
										+ ")" + ";",
								"insert into "
										+ v1.tables.scenes.name
										+ " select "
										+ getCommaSeparatedColumns(new String[] {
												v2.tables.scenes.column.ROWID,
												v2.tables.scenes.column.NAME })
										+ " from " + v2.tables.scenes.name,
								v1.tables.scenes.commands.alter.addContinentColumn,
								v1.tables.scenes.commands.alter.addRegionColumn,
								"update " + v1.tables.scenes.name + " set "
										+ v1.tables.scenes.column.CONTINENT
										+ "=" + "\"Unknown\"" + ";",
								"update " + v1.tables.scenes.name + " set "
										+ v1.tables.scenes.column.REGION + "="
										+ "\"Unknown\"" + ";",
								"drop table " + v2.tables.scenes.name + ";",
								"commit;" };
					}

					public static String[] recover(long currentTimeMillis) {
						return new String[] {
								"begin transaction;",
								"create table "
										+ v2.tables.scenes.name
										+ "("
										+ getCommaSeparatedColumns(new String[] {
												v2.tables.scenes.column.ROWID,
												v2.tables.scenes.column.NAME })
										+ ")" + ";",
								v2.tables.scenes.commands.alter.addCreatedColumn,
								v2.tables.scenes.commands.alter.addLastModifiedColumn,
								"update " + v2.tables.scenes.name + " set "
										+ v2.tables.scenes.column.CREATED + "="
										+ currentTimeMillis + ";",
								"update " + v2.tables.scenes.name + " set "
										+ v2.tables.scenes.column.LAST_MODIFIED
										+ "=" + currentTimeMillis + ";",
								"insert into "
										+ v2.tables.scenes.name
										+ " select "
										+ getCommaSeparatedColumns(new String[] {
												v1.tables.scenes.column.ROWID,
												v1.tables.scenes.column.NAME })
										+ " from " + v1.tables.scenes.name,
								"drop table " + tables.scenes.name + ";",
								"commit;" };
					}
				}

			}
		}

	}

	private static String getCommaSeparatedColumns(String[] columns) {
		String result = new String();
		for (String s : columns) {
			result += s + ",";
		}
		return result.substring(0, result.length() - 1);
	}

}