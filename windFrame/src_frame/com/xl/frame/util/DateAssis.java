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
 * Title: java.util.Date���͵İ�����
 * </p>
 * <p>
 * Description:�������ṩһЩ��ݵķ�����ʹ��Date����ʱ�����һЩ���õĲ�����
 * </p>
 * 
 * @version 1.0
 */
public class DateAssis {

	private static Map<String, DateFormat> formaterMap = new HashMap<String, DateFormat>();

	/**
	 * ����style����ʽ��ʽ��date����ʽ����д��ʽ�ο�java.text.SimpleDateFormat
	 * 
	 * @param date
	 *            ��Ҫ��ʽ������������
	 * @param style
	 *            ��ʽ������ʽ
	 * @return
	 */
	public static String formatDate(Date date, String style) {

		DateFormat formater = getDateFormat(style);

		return formater.format(date);
	}

	/**
	 * ����ʽ�����ַ���ת��Ϊ��Ӧ��Date
	 * 
	 * @param dateString
	 *            ��ʽ�����ַ���
	 * @param style
	 *            ��ʽ������ʽ
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
	 * �ж�����������Ƿ�Ϊһ���µ������
	 * 
	 * @param date
	 *            Ҫ�жϵ�����
	 * @param count
	 *            1����ʾҪ�ж������Ƿ�Ϊ����1���ڣ�2��ʾ�Ƿ�Ϊ����2���ڣ���������
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
	 * �������ʱ���е����
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
	 * ��õ�ǰʱ������
	 * 
	 * @return
	 */
	public static int getCurYear() {
		return DateAssis.getYear(new Date());
	}

	/**
	 * ����style������ڸ�ʽ����
	 * 
	 * @param style
	 *            ��ʽ������ʽ
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