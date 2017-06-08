package com.xl.frame.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class ToolForFile {

	/**
	 * 判断文件是否为excel文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isExcelFile(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		return "xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension);
	}
}
