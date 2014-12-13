package com.aspirephile.db;

import java.util.Vector;

import com.aspirephile.debug.NullPointerAsserter;

public class DBColumnsList {
	NullPointerAsserter asserter = new NullPointerAsserter(
			DBColumnsList.class.getName());

	Vector<String> keys, columns;

	public DBColumnsList() {
		keys = new Vector<String>();
		columns = new Vector<String>();
		if (asserter.assertPointer(keys, columns)) {
			addColumn("ROW_ID", "_id");
		}
	}

	public void addColumn(String key, String column)
			throws NullPointerException {
		if (asserter.assertPointer(key, column)) {
			keys.add(key);
			columns.add(column);
		} else
			throw new NullPointerException(
					"Neither Key nor Column can be a null string");
	}

	public String[] getColumns() {
		return (String[]) columns.toArray();
	}

	public String getFormattedColumns() {
		String result = new String();
		for (String s : columns) {
			result += s;
		}
		return result;
	}
}
