package com.xl.frame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ToolForIdcard {

	/**
	 * 身份证号码存放出生日期的格式
	 */
	public static final String idcard_birth_style = "yyyyMMdd";

	private static final String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
	private static final String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8",
			"4", "2" };

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

		if (FrameTool.isEmpty(idStr) || idStr.length() != 18) {
			rst.setDefaultValue("身份证号码长度应该为18位");
			return rst;
		}

		String Ai = idStr.substring(0, 17);
		if (FrameTool.isNumeric(Ai) == false) {
			rst.setDefaultValue("身份证号码除最后一位外，都应为数字");
			return rst;
		}

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (FrameTool.isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			rst.setDefaultValue("身份证生日无效");
			return rst;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			rst.setDefaultValue("身份证月份无效");
			return rst;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			rst.setDefaultValue("身份证日期无效");
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
			rst.setDefaultValue("身份证生日无效");
			return rst;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			rst.setDefaultValue("身份证生日无效");
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

		if (Ai.equals(idStr) == false) {
			rst.setDefaultValue("身份证无效，不是合法的身份证号码");
			return rst;
		}
		rst.setSucc(true);
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
	 * 判断此身份证号码是否为男性的
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isMale(String idcard) {
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			throw new RuntimeException("错误的身份证号码");
		} else {
			int sexCodeIdcard = Integer.parseInt(idcard.substring(16, 17));
			if (sexCodeIdcard % 2 == 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 获取此身份证的出生日期的date格式
	 * 
	 * @param idcard
	 * @return
	 */
	public static Date getBirthFromIdcard(String idcard) {
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			return null;
		} else {
			String birthIdcard = idcard.substring(6, 14);
			return DateAssis.parseStringToDate(birthIdcard, ToolForIdcard.idcard_birth_style);
		}
	}

	/**
	 * 获取此身份证的出生日期的String格式
	 * 
	 * @param idcard
	 * @param style
	 * @return
	 */
	public static String getBirthFromIdcard(String idcard, String style) {

		Date birth = getBirthFromIdcard(idcard);
		return DateAssis.formatDate(birth, style);
	}

	/**
	 * 根据身份证上的出生年份来获取当前的年龄
	 * 
	 * @param idcard
	 * @return
	 */
	public static int getAgeFromIdcardYear(String idcard) {
		return getAgeFromIdcardYear(idcard, DateAssis.getCurYear());
	}

	/**
	 * 根据身份证上的出生年份来获取在referYear这一年时此人年龄是多少
	 * 
	 * @param idcard
	 * @param referYear
	 * @return
	 */
	public static int getAgeFromIdcardYear(String idcard, int referYear) {
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			throw new RuntimeException("错误的身份证号码");
		} else {
			int year = Integer.parseInt(idcard.substring(6, 10));
			return referYear - year;
		}
	}
}
