package com.xl.frame.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class FrameTool {

	private static Log log = LogFactory.getLog(FrameTool.class);

	private static Gson gson;

	private static MessageDigest messageDigest;

	private synchronized static void initGson() {
		if (gson == null) {
			gson = new GsonBuilder().create();
		}
	}

	private synchronized static void initMessageDigest() {
		if (messageDigest == null) {
			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				log.error("", e);
			}
		}
	}

	/**
	 * 把输入的对象转换为json字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		if (gson == null) {
			initGson();
		}
		return gson.toJson(o);
	}

	public static String getMd5(String s) {
		if (messageDigest == null) {
			initMessageDigest();
		}
		messageDigest.update(s.getBytes());
		// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
		// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
		return new BigInteger(1, messageDigest.digest()).toString(16);

	}

	public static boolean isEmpty(Object o) {

		if (o == null) {
			return true;
		} else {
			if (o instanceof String) {
				return ((String) o).length() == 0;
			}
			if (o instanceof Collection) {
				return ((Collection) o).size() == 0;
			}
			if (o instanceof Map) {
				return ((Map) o).size() == 0;
			}
			if (o.getClass().isArray()) {
				return Array.getLength(o) == 0;
			}
		}

		return false;
	}

	public static Map<String, Object> getRequestParameterMap(ServletRequest request) {
		Map<String, Object> rst = new HashMap<String, Object>();
		Map<String, String[]> map = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			setParameterMapValue(rst, entry.getKey(), entry.getValue());
		}
		return rst;
	}

	private static void setParameterMapValue(Map<String, Object> map, String key, String[] values) {
		if (key.contains(".")) {
			String groupName = key.substring(0, key.indexOf("."));
			Map group = (Map) map.get(groupName);
			if (group == null) {
				group = new HashMap();
				map.put(groupName, group);
			}
			setParameterMapValue(group, key.substring(key.indexOf(".") + 1, key.length()), values);
		} else {
			if (!isEmpty(values) && values.length == 1) {
				map.put(key, values[0]);
			} else {
				map.put(key, values);
			}
		}
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?{1}");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void closeDbResource(Object o) {
		if (o == null) {
			return;
		}
		if (o instanceof ResultSet) {
			try {
				((ResultSet) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Statement) {
			try {
				((Statement) o).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (o instanceof Connection) {
			Connection c = (Connection) o;
			try {
				if (!c.isClosed()) {
					c.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String[] getStringArray(Map params, String key) {
		String[] rst = null;
		if (!FrameTool.isEmpty(params)) {
			rst = getStringArray(params.get(key));
		}
		return rst;
	}

	public static String[] getStringArray(Object o) {
		String[] rst = null;
		if (o != null) {
			if (o.getClass().isArray()) {
				rst = (String[]) o;
			} else {
				rst = new String[] { (String) o };
			}
		}
		return rst;

	}

	public static List getColumnList(List<Map> list, String key) {
		if (FrameTool.isEmpty(list) || FrameTool.isEmpty(key)) {
			return null;
		}
		List rst = new ArrayList();
		for (Map map : list) {
			rst.add(map.get(key));
		}
		return rst;
	}

	public static void replaceMapValue(Map map, String[] keys, String original, String replacement) {
		if (!FrameTool.isEmpty(map) && !FrameTool.isEmpty(keys) && !FrameTool.isEmpty(original)
				&& !FrameTool.isEmpty(replacement)) {
			for (String key : keys) {
				String value = (String) map.get(key);
				if (!FrameTool.isEmpty(value)) {
					map.put(key, value.replaceAll(original, replacement));
				}
			}
		}
	}

	/**
	 * 获得以iso-8859-1编码的字符串
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getIso8859Str(String str) throws UnsupportedEncodingException {

		if (FrameTool.isEmpty(str)) {
			return str;
		} else {
			return new String(str.getBytes(FrameConstant.frame_sys_charset), "iso-8859-1");
		}
	}

}
