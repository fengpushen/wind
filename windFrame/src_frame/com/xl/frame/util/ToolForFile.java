package com.xl.frame.util;

import java.io.File;
import java.io.IOException;

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

	/**
	 * 当file不存在时则在文件系统中建立此file
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
