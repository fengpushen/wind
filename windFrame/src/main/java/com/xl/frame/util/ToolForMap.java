package com.xl.frame.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ToolForMap {

	public static <K, V> List<V> getMapValueList(Map<K, V> map, List<K> keys) {
		List<V> values = null;
		if (!FrameTool.isEmpty(map) && !FrameTool.isEmpty(keys)) {
			values = new ArrayList<V>();
			for (K key : keys) {
				values.add(map.get(key));
			}
		}
		return values;
	}

	public static List<String> getMapValueListStr(Map map, List keys) {
		List<String> values = null;
		if (!FrameTool.isEmpty(map) && !FrameTool.isEmpty(keys)) {
			values = new ArrayList<String>();
			for (Object key : keys) {
				Object value = map.get(key);
				if (value != null && !value.getClass().equals(String.class)) {
					value = value.toString();
					values.add(value.toString());
				} else {
					values.add((String) value);
				}
			}
		}
		return values;
	}

	public static void lowerKey(Map map) {
		if (!FrameTool.isEmpty(map)) {
			Set<String> upperKeys = new HashSet<String>();
			for (Object key : map.keySet()) {
				if (key instanceof String) {
					String strKey = (String) key;
					String lowerKey = strKey.toLowerCase();
					if (!lowerKey.equals(strKey)) {
						upperKeys.add(strKey);
						map.put(lowerKey, map.get(strKey));
					}
				}
			}
			if (!FrameTool.isEmpty(upperKeys)) {
				for (String key : upperKeys) {
					map.remove(key);
				}
			}
		}
	}
}
