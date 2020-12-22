package com.xl.frame.util;

import java.util.HashMap;

/**
 * 操作结果
 * 
 * @author wind
 * 
 */
public class ExecuteResult implements java.io.Serializable {

	/**
	 * 默认返回值的key
	 */
	public static final String INFO_KEY_DEFAULT = "INFO_KEY_DEFAULT";

	/**
	 * 操作是否成功
	 */
	private boolean isSucc = false;

	/**
	 * 操作附加信息
	 */
	private HashMap<String, Object> info;

	public ExecuteResult() {
	}

	public ExecuteResult(boolean isSucc, String msg) {
		setSucc(isSucc);
		setDefaultValue(msg);
	}

	public boolean isSucc() {
		return isSucc;
	}

	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}

	public HashMap<String, Object> getInfo() {
		return info;
	}

	public Object getInfoOne(String key) {
		if (info != null && key.length() > 0) {
			return info.get(key);
		}
		return "";
	}

	public void setDefaultValue(String value) {
		addInfo(ExecuteResult.INFO_KEY_DEFAULT, value);
	}

	public String getDefaultValue() {

		return (String) getInfoOne(INFO_KEY_DEFAULT);
	}

	public void addInfo(String key, Object value) {
		if (info == null) {
			info = new HashMap<String, Object>();
		}
		info.put(key, value);
	}

	public void setExecuteSucc(String msg) {
		setSucc(true);
		setDefaultValue(msg);
	}

}
