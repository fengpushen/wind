package com.xl.frame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ToolForIdcard {

	/**
	 * ���֤�����ų������ڵĸ�ʽ
	 */
	public static final String idcard_birth_style = "yyyyMMdd";

	private static final String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
	private static final String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8",
			"4", "2" };

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

		if (FrameTool.isEmpty(idStr) || idStr.length() != 18) {
			rst.setDefaultValue("���֤���볤��Ӧ��Ϊ18λ");
			return rst;
		}

		String Ai = idStr.substring(0, 17);
		if (FrameTool.isNumeric(Ai) == false) {
			rst.setDefaultValue("���֤��������һλ�⣬��ӦΪ����");
			return rst;
		}

		// ================ ���������Ƿ���Ч ================
		String strYear = Ai.substring(6, 10);// ���
		String strMonth = Ai.substring(10, 12);// �·�
		String strDay = Ai.substring(12, 14);// �·�
		if (FrameTool.isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			rst.setDefaultValue("���֤������Ч");
			return rst;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			rst.setDefaultValue("���֤�·���Ч");
			return rst;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
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
			rst.setDefaultValue("���֤������Ч");
			return rst;
		} catch (java.text.ParseException e) {
			e.printStackTrace();
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

		if (Ai.equals(idStr) == false) {
			rst.setDefaultValue("���֤��Ч�����ǺϷ������֤����");
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
	 * �жϴ����֤�����Ƿ�Ϊ���Ե�
	 * 
	 * @param idcard
	 * @return
	 */
	public static boolean isMale(String idcard) {
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			throw new RuntimeException("��������֤����");
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
	 * ��ȡ�����֤�ĳ������ڵ�date��ʽ
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
	 * ��ȡ�����֤�ĳ������ڵ�String��ʽ
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
	 * �������֤�ϵĳ����������ȡ��ǰ������
	 * 
	 * @param idcard
	 * @return
	 */
	public static int getAgeFromIdcardYear(String idcard) {
		return getAgeFromIdcardYear(idcard, DateAssis.getCurYear());
	}

	/**
	 * �������֤�ϵĳ����������ȡ��referYear��һ��ʱ���������Ƕ���
	 * 
	 * @param idcard
	 * @param referYear
	 * @return
	 */
	public static int getAgeFromIdcardYear(String idcard, int referYear) {
		ExecuteResult rst = ToolForIdcard.idcardValidate(idcard);
		if (!rst.isSucc()) {
			throw new RuntimeException("��������֤����");
		} else {
			int year = Integer.parseInt(idcard.substring(6, 10));
			return referYear - year;
		}
	}
}
