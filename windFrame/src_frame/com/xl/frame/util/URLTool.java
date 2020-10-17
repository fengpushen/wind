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
 * Title: URL操作辅助类
 * </p>
 * <p>
 * Description: 此类提供一般性URL操作的辅助。
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
	 * 以post方式向url提交请求，请求的参数放在map中，由此函数负责写到指定的url，取得返回信息，将此信息以字符串方式返回。
	 * 
	 * @param url
	 *            提交的地址
	 * @param params
	 *            参数列表
	 * @return 回应信息
	 * @throws IOException
	 */
	public String postParamAndGetReturnValue(URL url, Map<String, List<String>> params) throws IOException {

		return postStringAndGetReturnValue(url, getOutParamString(params));
	}

	/**
	 * 以post方式将传入的s写到url中去，取得返回信息，将此信息以字符串方式返回。
	 * 
	 * @param url
	 *            提交的地址
	 * @param s
	 *            要提交的内容
	 * @return 回应信息
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
	 * 根据参数的map生成请求的串
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
	 * 将一个字符输入流中的所有字符取出放在一个字符串中返回
	 * 
	 * @param reader
	 *            字符输入流
	 * @return 包含此输入流中所有字符的字符串
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