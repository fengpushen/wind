package com.xl.frame.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title: URL����������
 * </p>
 * <p>
 * Description: �����ṩһ����URL�����ĸ�����
 * </p>
 * 
 * @author zhiqiang.liu
 * @version 1.0
 */
public class URLTool {

	private static URLTool tool;

	public static URLTool getInstance() {

		if (tool == null) {
			tool = new URLTool();
		}
		return tool;
	}

	private URLTool() {
	}

	/**
	 * ��post��ʽ��url�ύ��������Ĳ�������map�У��ɴ˺�������д��ָ����url��ȡ�÷�����Ϣ��������Ϣ���ַ�����ʽ���ء�
	 * 
	 * @param url
	 *            �ύ�ĵ�ַ
	 * @param params
	 *            �����б�
	 * @return ��Ӧ��Ϣ
	 * @throws IOException
	 */
	public String postParamAndGetReturnValue(URL url, Map<String, List<String>> params) throws IOException {

		return postStringAndGetReturnValue(url, getOutParamString(params));
	}

	/**
	 * ��post��ʽ�������sд��url��ȥ��ȡ�÷�����Ϣ��������Ϣ���ַ�����ʽ���ء�
	 * 
	 * @param url
	 *            �ύ�ĵ�ַ
	 * @param s
	 *            Ҫ�ύ������
	 * @return ��Ӧ��Ϣ
	 * @throws IOException
	 */
	public String postStringAndGetReturnValue(URL url, String s) throws IOException {

		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		if (s != null) {
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(s);
			out.close();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String value = getStringFromReader(in);
		in.close();

		return value.trim();
	}

	/**
	 * ���ݲ�����map��������Ĵ�
	 * 
	 * @param params
	 * @return
	 */
	protected String getOutParamString(Map<String, List<String>> params) {

		StringBuffer outParamsBuffer = new StringBuffer();
		Set<Map.Entry<String, List<String>>> entrySet = params.entrySet();
		Iterator<Map.Entry<String, List<String>>> it = entrySet.iterator();
		Map.Entry<String, List<String>> entry = null;
		String key = null;
		while (it.hasNext()) {
			entry = it.next();
			key = entry.getKey();
			for (String value : entry.getValue()) {
				outParamsBuffer.append(key).append("=").append(value).append("&");
			}
		}
		if (entrySet.size() > 0) {
			outParamsBuffer.deleteCharAt(outParamsBuffer.length() - 1);
		}
		return outParamsBuffer.toString();
	}

	/**
	 * ��һ���ַ��������е������ַ�ȡ������һ���ַ����з���
	 * 
	 * @param reader
	 *            �ַ�������
	 * @return �������������������ַ����ַ���
	 * @throws IOException
	 */
	protected String getStringFromReader(BufferedReader reader) throws IOException {

		StringBuffer outStringBuffer = new StringBuffer();
		char[] buffer = new char[512];
		int readCharNum = 0;
		while ((readCharNum = reader.read(buffer)) != -1) {
			for (int i = 0; i < readCharNum; i++) {
				outStringBuffer.append(buffer[i]);
			}
		}
		return outStringBuffer.toString();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String info = URLTool.getInstance().getStringFromReader(
				new BufferedReader(new InputStreamReader(new FileInputStream(new File("d:/aa.xml")))));
		System.out.println(info);
		System.out.println(URLTool.getInstance().postStringAndGetReturnValue(
				new URL("http://localhost:8080/xtzj/soap/getUnitRecruitInfoAction"), info));
	}
}