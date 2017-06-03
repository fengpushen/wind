package com.xl.frame.util;

import java.util.HashMap;
import java.util.Map;

public class FrameDbInfo {

	private Map<String, FrameDbTable> tables = new HashMap<String, FrameDbTable>();

	public void putTable(String tableName, FrameDbTable table) {
		tables.put(tableName, table);
	}

	public FrameDbTable getTable(String tableName) {
		return tables.get(tableName);
	}

	public boolean containsTable(String tableName) {
		return tables.containsKey(tableName);
	}

}
