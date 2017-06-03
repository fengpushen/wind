package com.xl.frame.util;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
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
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * 功能：身份证的有效验证
	 * 
	 * @param idStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 */
	public static ExecuteResult idcardValidate(String idStr) {
		ExecuteResult rst = new ExecuteResult();
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (isEmpty(idStr) || idStr.length() != 18) {
			rst.setDefaultValue("身份证号码长度应该为18位");
			return rst;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (idStr.length() == 18) {
			Ai = idStr.substring(0, 17);
		} else if (idStr.length() == 15) {
			Ai = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			rst.setDefaultValue("身份证号码除最后一位外，都应为数字");
			return rst;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			rst.setDefaultValue("身份证生日无效");
			return rst;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				rst.setDefaultValue("身份证生日不在有效范围");
				return rst;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			rst.setDefaultValue("身份证月份无效");
			return rst;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			rst.setDefaultValue("身份证日期无效");
			return rst;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Map<String, String> h = getIdcardAreaCodeMap();
		if (h.get(Ai.substring(0, 2)) == null) {
			rst.setDefaultValue("身份证地区编码错误");
			return rst;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (idStr.length() == 18) {
			if (Ai.equals(idStr) == false) {
				rst.setDefaultValue("身份证无效，不是合法的身份证号码");
				return rst;
			}
		} else {
			rst.setSucc(true);
		}
		// =====================(end)=====================
		return rst;
	}

	private static Map<String, String> idcardAreaCodeMap;

	private static Map<String, String> getIdcardAreaCodeMap() {
		if (idcardAreaCodeMap == null) {
			initIdcardAreaCodeMap();
		}
		return idcardAreaCodeMap;
	}

	private static synchronized void initIdcardAreaCodeMap() {
		if (idcardAreaCodeMap == null) {
			idcardAreaCodeMap = new HashMap<String, String>();
			idcardAreaCodeMap.put("11", "北京");
			idcardAreaCodeMap.put("12", "天津");
			idcardAreaCodeMap.put("13", "河北");
			idcardAreaCodeMap.put("14", "山西");
			idcardAreaCodeMap.put("15", "内蒙古");
			idcardAreaCodeMap.put("21", "辽宁");
			idcardAreaCodeMap.put("22", "吉林");
			idcardAreaCodeMap.put("23", "黑龙江");
			idcardAreaCodeMap.put("31", "上海");
			idcardAreaCodeMap.put("32", "江苏");
			idcardAreaCodeMap.put("33", "浙江");
			idcardAreaCodeMap.put("34", "安徽");
			idcardAreaCodeMap.put("35", "福建");
			idcardAreaCodeMap.put("36", "江西");
			idcardAreaCodeMap.put("37", "山东");
			idcardAreaCodeMap.put("41", "河南");
			idcardAreaCodeMap.put("42", "湖北");
			idcardAreaCodeMap.put("43", "湖南");
			idcardAreaCodeMap.put("44", "广东");
			idcardAreaCodeMap.put("45", "广西");
			idcardAreaCodeMap.put("46", "海南");
			idcardAreaCodeMap.put("50", "重庆");
			idcardAreaCodeMap.put("51", "四川");
			idcardAreaCodeMap.put("52", "贵州");
			idcardAreaCodeMap.put("53", "云南");
			idcardAreaCodeMap.put("54", "西藏");
			idcardAreaCodeMap.put("61", "陕西");
			idcardAreaCodeMap.put("62", "甘肃");
			idcardAreaCodeMap.put("63", "青海");
			idcardAreaCodeMap.put("64", "宁夏");
			idcardAreaCodeMap.put("65", "新疆");
			idcardAreaCodeMap.put("71", "台湾");
			idcardAreaCodeMap.put("81", "香港");
			idcardAreaCodeMap.put("82", "澳门");
			idcardAreaCodeMap.put("91", "国外");
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

}
