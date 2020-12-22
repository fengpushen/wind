package com.xl.frame.util;

public class CodeInfo {

	private String item;

	private String code;

	private String info;

	private long loadCount;

	public CodeInfo(String item, String code, String info, long loadCount) {
		super();
		this.item = item;
		this.code = code;
		this.info = info;
		this.loadCount = loadCount;
	}

	public CodeInfo(String item, String code) {
		super();
		this.item = item;
		this.code = code;
	}

	public String getItem() {
		return item;
	}

	public String getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

	public long getLoadCount() {
		return loadCount;
	}

	public void increaseLoadCount() {
		loadCount++;
	}
}
