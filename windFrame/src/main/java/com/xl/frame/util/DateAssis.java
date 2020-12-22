package com.xl.frame.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: java.util.Date类型的帮助类
 * </p>
 * <p>
 * Description:此类中提供一些快捷的方法供使用Date类型时所需的一些常用的操作。
 * </p>
 * 
 * @version 1.0
 */
public class DateAssis {

	private static Map<String, DateFormat> formaterMap = new HashMap<String, DateFormat>();

	/**
	 * 按照style的样式格式化date，样式的书写方式参看java.text.SimpleDateFormat
	 * 
	 * @param date
	 *            需要格式化的日期类型
	 * @param style
	 *            格式化的样式
	 * @return
	 */
	public static String formatDate(Date date, String style) {

		DateFormat formater = getDateFormat(style);

		return formater.format(date);
	}

	/**
	 * 将格式化的字符串转换为对应的Date
	 * 
	 * @param dateString
	 *            格式化的字符串
	 * @param style
	 *            格式化的样式
	 * @return
	 */
	public static Date parseStringToDate(String dateString, String style) {

		DateFormat formater = getDateFormat(style);

		try {
			return formater.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断输入的日期是否为一个月的最后几天
	 * 
	 * @param date
	 *            要判断的日期
	 * @param count
	 *            1，表示要判断日期是否为倒数1天内，2表示是否为倒数2天内，依次类推
	 * @return
	 */
	public static boolean isMonthEnd(Date date, int count) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		int omonth = c.get(Calendar.MONTH);
		c.add(Calendar.MONTH, count);
		int nmonth = c.get(Calendar.MONTH);
		return omonth == nmonth;
	}

	/**
	 * 获得输入时间中的年份
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获得当前时间的年份
	 * 
	 * @return
	 */
	public static int getCurYear() {
		return DateAssis.getYear(new Date());
	}

	/**
	 * 根据style获得日期格式化类
	 * 
	 * @param style
	 *            格式化的样式
	 * @return
	 */
	private static DateFormat getDateFormat(String style) {

		DateFormat formater = formaterMap.get(style);

		if (formater == null) {
			formater = new SimpleDateFormat(style);
			formaterMap.put(style, formater);
		}
		return formater;
	}
}