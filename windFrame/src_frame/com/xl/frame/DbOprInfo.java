package com.xl.frame;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator 数据库操作信息
 *
 */
public class DbOprInfo implements Serializable {

	public enum DbOprType {
		/**
		 * 通用的insert，会调用FrameDAO的anyInsert方法
		 */
		anyInsert,
		/**
		 * 通用的update，会调用DAOHelper的anyUpdateByPk方法
		 */
		anyUpdateByPk
	}

	private DbOprType oprType;

	private String tableName;

	private Object params;

	private String pkValue;

	public DbOprInfo() {
		super();
	}

	public DbOprInfo(DbOprType oprType, String tableName, Object params) {
		super();
		this.oprType = oprType;
		this.tableName = tableName;
		this.params = params;
	}

	public DbOprInfo(DbOprType oprType, String tableName, Object params, String pkValue) {
		super();
		this.oprType = oprType;
		this.tableName = tableName;
		this.params = params;
		this.pkValue = pkValue;
	}

	public DbOprType getOprType() {
		return oprType;
	}

	public String getTableName() {
		return tableName;
	}

	public Object getParams() {
		return params;
	}

	public void setOprType(DbOprType oprType) {
		this.oprType = oprType;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public String getPkValue() {
		return pkValue;
	}

	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	@Override
	public String toString() {
		return oprType + " " + tableName + " " + params + " " + pkValue;
	}

}
