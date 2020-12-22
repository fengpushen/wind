package com.xl.frame.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.bidimap.DualTreeBidiMap;

import com.xl.frame.util.tree.TreeNode;

public class FrameCode {

	private static Map<String, DualTreeBidiMap<String, String>> codes = new HashMap<String, DualTreeBidiMap<String, String>>();

	private static Map<String, String> codeJsons = new HashMap<String, String>();

	private static Map<String, CodeInfo> loadedCodes = new HashMap<String, CodeInfo>();

	public static CodeInfo loadCode(String codeName, String codeKey) {
		String codeVaule = null;
		if ("area_code".equals(codeName)) {
			TreeNode node = FrameCache.getTree(FrameConstant.busi_com_area_tree).getNode(codeKey);
			if (node != null) {
				codeVaule = node.getName();
			}
		} else {
			codeVaule = getCodeValue(codeName, codeKey);
		}
		if (codeVaule == null) {
			return null;
		}
		String loadedKey = codeName + codeKey;
		if (!loadedCodes.containsKey(loadedKey)) {
			CodeInfo info = new CodeInfo(codeName, codeKey, codeVaule, 0L);
			loadedCodes.put(loadedKey, info);
		}
		CodeInfo info = loadedCodes.get(loadedKey);
		info.increaseLoadCount();
		return info;
	}

	public static Collection<CodeInfo> loadCodes(String codeName, String codeKey) {
		loadCode(codeName, codeKey);
		return loadCodes();
	}

	public static Collection<CodeInfo> loadCodes() {
		return loadedCodes.values();
	}

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
