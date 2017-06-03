package com.xl.frame.util;

import java.util.HashMap;

/**
 * �������
 * 
 * @author wind
 * 
 */
public class ExecuteResult implements java.io.Serializable {

	/**
	 * Ĭ�Ϸ���ֵ��key
	 */
	public static final String INFO_KEY_DEFAULT = "INFO_KEY_DEFAULT";

	/**
	 * �����Ƿ�ɹ�
	 */
	private boolean isSucc = false;

	/**
	 * ����������Ϣ
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
