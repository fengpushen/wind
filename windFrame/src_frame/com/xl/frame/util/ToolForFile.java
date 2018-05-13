package com.xl.frame.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class ToolForFile {

	/**
	 * �ж��ļ��Ƿ�Ϊexcel�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExcelFile(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		return "xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension);
	}

	/**
	 * ��file������ʱ�����ļ�ϵͳ�н�����file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void makeSureFileExists(File file) throws IOException {

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}
}
