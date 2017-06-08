package com.xl.frame.util;

import java.io.File;

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
}
