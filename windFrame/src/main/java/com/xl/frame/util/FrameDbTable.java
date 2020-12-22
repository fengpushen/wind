package com.xl.frame.util;

import java.util.HashSet;
import java.util.Set;

public class FrameDbTable {

	private String tableName;

	private Set<String> pkColNames = new HashSet<String>();

	private Set<String> colNames = new HashSet<String>();

	public FrameDbTable(String tableName) {
		this.tableName = tableName;
	}

	public void addColName(String colName) {
		colNames.add(colName);
	}

	public void addPkColName(String colName) {
		pkColNames.add(colName);
	}

	public void removeColName(String colName) {
		colNames.remove(colName);
	}

	public boolean containsColName(String colName) {
		return colNames.contains(colName);
	}

	public String getTableName() {
		return tableName;
	}

	public String[] getPkColNames() {
		return pkColNames.toArray(new String[1]);
	}

}
