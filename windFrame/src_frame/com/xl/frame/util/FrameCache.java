package com.xl.frame.util;

import java.util.HashMap;
import java.util.Map;

import com.xl.frame.util.tree.TreeView;

public final class FrameCache {

	private static Map<String, TreeView> trees = new HashMap<String, TreeView>();

	private static Map<String, FrameDbInfo> dbInfos = new HashMap<String, FrameDbInfo>();

	private static Map<String, String> configs = new HashMap<String, String>();

	public static void addTree(String key, TreeView tree) {
		trees.put(key, tree);
	}

	public static TreeView getTree(String key) {
		return trees.get(key);
	}

	public static void addDbInfo(String key, FrameDbInfo info) {
		dbInfos.put(key, info);
	}

	public static FrameDbInfo getDbInfo(String key) {
		if (!dbInfos.containsKey(key)) {
			newDbInfo(key);
		}
		return dbInfos.get(key);
	}

	public static synchronized void newDbInfo(String key) {
		if (!dbInfos.containsKey(key)) {
			dbInfos.put(key, new FrameDbInfo());
		}
	}

	public static void addConfig(String name, String value) {
		configs.put(name, value);
	}

	public static String getFrameConfig(String name) {
		return configs.get(name);
	}
}
