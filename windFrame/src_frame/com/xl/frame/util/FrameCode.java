package com.xl.frame.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.bidimap.DualTreeBidiMap;

public class FrameCode {

	private static Map<String, DualTreeBidiMap<String, String>> codes = new HashMap<String, DualTreeBidiMap<String, String>>();

	private static Map<String, String> codeJsons = new HashMap<String, String>();

	public static void addCode(String codeName, DualTreeBidiMap<String, String> code) {
		codes.put(codeName, code);
		codeJsons.remove(codeName);
	}

	public static String getCodeValue(String codeName, String codeKey) {
		DualTreeBidiMap<String, String> code = codes.get(codeName);
		if (code != null) {
			return code.get(codeKey);
		}
		return null;
	}

	public static String getCodeKey(String codeName, String codeValue) {
		DualTreeBidiMap<String, String> code = codes.get(codeName);
		if (code != null) {
			return code.getKey(codeValue);
		}
		return null;
	}

	public static String getCodeJson(String codeName) {
		String json = null;
		if (codes.containsKey(codeName)) {
			if (!codeJsons.containsKey(codeName)) {
				makeCodeJson(codeName);
			}
			json = codeJsons.get(codeName);
		}
		return json;

	}

	private synchronized static void makeCodeJson(String codeName) {
		if (codes.containsKey(codeName) && !codeJsons.containsKey(codeName)) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			DualTreeBidiMap<String, String> code = codes.get(codeName);
			Set<String> keys = code.keySet();
			for (String key : keys) {
				sb.append("{");
				sb.append("\"id\":");
				sb.append("\"").append(key).append("\",");
				sb.append("\"text\":");
				sb.append("\"").append(code.get(key)).append("\"");
				sb.append("}").append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
			codeJsons.put(codeName, sb.toString());
		}
	}

}
