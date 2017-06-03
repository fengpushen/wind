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
	 * ������Ķ���ת��Ϊjson�ַ���
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
		// digest()���ȷ������md5 hashֵ������ֵΪ8Ϊ�ַ�������Ϊmd5 hashֵ��16λ��hexֵ��ʵ���Ͼ���8λ���ַ�
		// BigInteger������8λ���ַ���ת����16λhexֵ�����ַ�������ʾ���õ��ַ�����ʽ��hashֵ
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
	 * ���֤������֤ 1������Ľṹ ������ݺ�������������룬��ʮ��λ���ֱ������һλУ������ɡ�����˳�������������Ϊ����λ���ֵ�ַ�룬
	 * ��λ���ֳ��������룬��λ����˳�����һλ����У���롣 2����ַ��(ǰ��λ����
	 * ��ʾ�������ס����������(�С��졢��)�������������룬��GB/T2260�Ĺ涨ִ�С� 3�����������루����λ��ʮ��λ��
	 * ��ʾ�������������ꡢ�¡��գ���GB/T7408�Ĺ涨ִ�У��ꡢ�¡��մ���֮�䲻�÷ָ����� 4��˳���루��ʮ��λ��ʮ��λ��
	 * ��ʾ��ͬһ��ַ������ʶ������Χ�ڣ���ͬ�ꡢͬ�¡�ͬ�ճ������˱ඨ��˳��ţ� ˳�����������������ԣ�ż�������Ů�ԡ� 5��У���루��ʮ��λ����
	 * ��1��ʮ��λ���ֱ������Ȩ��͹�ʽ S = Sum(Ai * Wi), i = 0, ... , 16 ���ȶ�ǰ17λ���ֵ�Ȩ���
	 * Ai:��ʾ��iλ���ϵ����֤��������ֵ Wi:��ʾ��iλ���ϵļ�Ȩ���� Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 ��2������ģ Y = mod(S, 11) ��3��ͨ��ģ�õ���Ӧ��У���� Y: 0 1 2 3 4 5 6 7 8 9 10 У����: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * ���ܣ����֤����Ч��֤
	 * 
	 * @param idStr
	 *            ���֤��
	 * @return ��Ч������"" ��Ч������String��Ϣ
	 */
	public static ExecuteResult idcardValidate(String idStr) {
		ExecuteResult rst = new ExecuteResult();
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ ����ĳ��� 15λ��18λ ================
		if (isEmpty(idStr) || idStr.length() != 18) {
			rst.setDefaultValue("���֤���볤��Ӧ��Ϊ18λ");
			return rst;
		}
		// =======================(end)========================

		// ================ ���� �������Ϊ��Ϊ���� ================
		if (idStr.length() == 18) {
			Ai = idStr.substring(0, 17);
		} else if (idStr.length() == 15) {
			Ai = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			rst.setDefaultValue("���֤��������һλ�⣬��ӦΪ����");
			return rst;
		}
		// =======================(end)========================

		// ================ ���������Ƿ���Ч ================
		String strYear = Ai.substring(6, 10);// ���
		String strMonth = Ai.substring(10, 12);// �·�
		String strDay = Ai.substring(12, 14);// �·�
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			rst.setDefaultValue("���֤������Ч");
			return rst;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				rst.setDefaultValue("���֤���ղ�����Ч��Χ");
				return rst;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			rst.setDefaultValue("���֤�·���Ч");
			return rst;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			rst.setDefaultValue("���֤������Ч");
			return rst;
		}
		// =====================(end)=====================

		// ================ ������ʱ����Ч ================
		Map<String, String> h = getIdcardAreaCodeMap();
		if (h.get(Ai.substring(0, 2)) == null) {
			rst.setDefaultValue("���֤�����������");
			return rst;
		}
		// ==============================================

		// ================ �ж����һλ��ֵ ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (idStr.length() == 18) {
			if (Ai.equals(idStr) == false) {
				rst.setDefaultValue("���֤��Ч�����ǺϷ������֤����");
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
			idcardAreaCodeMap.put("11", "����");
			idcardAreaCodeMap.put("12", "���");
			idcardAreaCodeMap.put("13", "�ӱ�");
			idcardAreaCodeMap.put("14", "ɽ��");
			idcardAreaCodeMap.put("15", "���ɹ�");
			idcardAreaCodeMap.put("21", "����");
			idcardAreaCodeMap.put("22", "����");
			idcardAreaCodeMap.put("23", "������");
			idcardAreaCodeMap.put("31", "�Ϻ�");
			idcardAreaCodeMap.put("32", "����");
			idcardAreaCodeMap.put("33", "�㽭");
			idcardAreaCodeMap.put("34", "����");
			idcardAreaCodeMap.put("35", "����");
			idcardAreaCodeMap.put("36", "����");
			idcardAreaCodeMap.put("37", "ɽ��");
			idcardAreaCodeMap.put("41", "����");
			idcardAreaCodeMap.put("42", "����");
			idcardAreaCodeMap.put("43", "����");
			idcardAreaCodeMap.put("44", "�㶫");
			idcardAreaCodeMap.put("45", "����");
			idcardAreaCodeMap.put("46", "����");
			idcardAreaCodeMap.put("50", "����");
			idcardAreaCodeMap.put("51", "�Ĵ�");
			idcardAreaCodeMap.put("52", "����");
			idcardAreaCodeMap.put("53", "����");
			idcardAreaCodeMap.put("54", "����");
			idcardAreaCodeMap.put("61", "����");
			idcardAreaCodeMap.put("62", "����");
			idcardAreaCodeMap.put("63", "�ຣ");
			idcardAreaCodeMap.put("64", "����");
			idcardAreaCodeMap.put("65", "�½�");
			idcardAreaCodeMap.put("71", "̨��");
			idcardAreaCodeMap.put("81", "���");
			idcardAreaCodeMap.put("82", "����");
			idcardAreaCodeMap.put("91", "����");
		}
	}

	/**
	 * ���ܣ��ж��ַ����Ƿ�Ϊ����
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
	 * ���ܣ��ж��ַ����Ƿ�Ϊ���ڸ�ʽ
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
