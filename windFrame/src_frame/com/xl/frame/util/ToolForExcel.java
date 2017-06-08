package com.xl.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToolForExcel {

	public static final String excel_type_2003 = "2003";
	public static final String excel_type_2007 = "2007";

	public static Workbook getWorkbookFromExcelFile(String filePath) {
		File excelFile = new File(filePath);
		return getWorkbookFromExcelFile(excelFile);
	}

	public static Workbook getWorkbookFromExcelFile(File excelFile) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(excelFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return getWorkbookFromExcelFile(fileInputStream);
	}

	public static Workbook getWorkbookFromExcelFile(FileInputStream excelInputStream) {
		Workbook wb = null;
		try {
			String excelType = getExcelFileType(excelInputStream);
			if (ToolForExcel.excel_type_2003.equals(excelType)) {
				wb = new HSSFWorkbook(excelInputStream);
			}
			if (ToolForExcel.excel_type_2007.equals(excelType)) {
				wb = new XSSFWorkbook(excelInputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

	/**
	 * 从excel文件中读取内容，组成List返回，List中每个元素为String数组，数组的第一个元素为行号，实际内容从第二个元素开始
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> buildListFromExcel(File file) throws IOException {
		return buildListFromExcel(file, 0, 0);
	}

	/**
	 * @param file
	 * @param startRow
	 *            开始行
	 * @param startCol
	 *            开始列
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> buildListFromExcel(File file, int startRow, int startCol) throws IOException {
		String fileType = getExcelFileType(file);
		if ("2003".equals(fileType)) {
			return buildListFromExcel2003(file, startRow, startCol);
		}
		if ("2007".equals(fileType)) {
			return buildListFromExcel2007(file, startRow, startCol);
		}
		return null;
	}

	/**
	 * @param file
	 * @param startRow
	 *            开始行
	 * @param startCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> buildListFromExcel(File file, int startRow, int startCol, int endCol)
			throws IOException {
		String fileType = getExcelFileType(file);
		if ("2003".equals(fileType)) {
			return buildListFromExcel2003(file, startRow, startCol, endCol);
		}
		if ("2007".equals(fileType)) {
			return buildListFromExcel2007(file, startRow, startCol, endCol);
		}
		return null;
	}

	public static void allRowSetStyle(HSSFRow row, CellStyle style) {
		for (int colIdx = 0; colIdx < row.getLastCellNum(); colIdx++) {
			HSSFCell cell = row.getCell(colIdx);
			if (cell == null) {
				cell = row.createCell(colIdx);
			}
			cell.setCellStyle(style);
		}
	}

	public static List<String[]> buildListFromExcel2003(File file, int startRow, int startCol) {

		List<String[]> rst = new ArrayList<String[]>();
		FileInputStream fin = null;
		HSSFWorkbook workbook = null;
		try {
			fin = new FileInputStream(file);
			workbook = new HSSFWorkbook(fin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (workbook != null) {
			HSSFSheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				HSSFRow row = worksheet.getRow(rowIdx);
				int endCol = row.getLastCellNum();
				String[] rowStr = new String[endCol - startCol + 2];
				int idx = 0;
				rowStr[idx] = Integer.toString(rowIdx);
				for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
					idx++;
					String value = ToolForExcel.getCellValueAsString(row.getCell(colIdx));
					if (value != null && value.length() > 0) {
						value = value.trim();
					}
					rowStr[idx] = value;
				}
				rst.add(rowStr);
			}
		}

		if (fin != null) {
			try {
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rst;
	}

	public static List<String[]> buildListFromExcel2003(File file, int startRow, int startCol, int endCol) {

		List<String[]> rst = new ArrayList<String[]>();
		FileInputStream fin = null;
		HSSFWorkbook workbook = null;
		try {
			fin = new FileInputStream(file);
			workbook = new HSSFWorkbook(fin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (workbook != null) {
			HSSFSheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				HSSFRow row = worksheet.getRow(rowIdx);
				String[] rowStr = new String[endCol - startCol + 2];
				int idx = 0;
				rowStr[idx] = Integer.toString(rowIdx);
				for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
					idx++;
					String value = ToolForExcel.getCellValueAsString(row.getCell(colIdx));
					if (value != null && value.length() > 0) {
						value = value.trim();
					}
					rowStr[idx] = value;
				}
				rst.add(rowStr);
			}
		}

		if (fin != null) {
			try {
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rst;
	}

	public static List<String[]> buildListFromExcel2007(File file, int startRow, int startCol) {

		List<String[]> rst = new ArrayList<String[]>();
		FileInputStream fin = null;
		XSSFWorkbook workbook = null;

		try {
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (workbook != null) {
			XSSFSheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				XSSFRow row = worksheet.getRow(rowIdx);
				int endCol = row.getLastCellNum();
				String[] rowStr = new String[endCol - startCol + 2];
				int idx = 0;
				rowStr[idx] = Integer.toString(rowIdx);
				for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
					idx++;
					String value = ToolForExcel.getCellValueAsString(row.getCell(colIdx));
					if (value != null && value.length() > 0) {
						value = value.trim();
					}
					rowStr[idx] = value;
				}
				rst.add(rowStr);
			}
		}

		if (fin != null) {
			try {
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rst;
	}

	public static List<String[]> buildListFromExcel2007(File file, int startRow, int startCol, int endCol) {

		List<String[]> rst = new ArrayList<String[]>();
		FileInputStream fin = null;
		XSSFWorkbook workbook = null;

		try {
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (workbook != null) {
			XSSFSheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				XSSFRow row = worksheet.getRow(rowIdx);
				String[] rowStr = new String[endCol - startCol + 2];
				int idx = 0;
				rowStr[idx] = Integer.toString(rowIdx);
				for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
					idx++;
					String value = ToolForExcel.getCellValueAsString(row.getCell(colIdx));
					if (value != null && value.length() > 0) {
						value = value.trim();
					}
					rowStr[idx] = value;
				}
				rst.add(rowStr);
			}
		}

		if (fin != null) {
			try {
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rst;
	}

	public static List<String[]> buildListFromExcel2007(File file, int startRow, int endRow, int startCol, int endCol) {

		List<String[]> rst = new ArrayList<String[]>();
		FileInputStream fin = null;
		XSSFWorkbook workbook = null;

		try {
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (workbook != null) {
			XSSFSheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum() > endRow ? endRow
					: worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				XSSFRow row = worksheet.getRow(rowIdx);
				String[] rowStr = new String[endCol - startCol + 2];
				int idx = 0;
				rowStr[idx] = Integer.toString(rowIdx);
				for (int colIdx = startCol; colIdx <= endCol; colIdx++) {
					idx++;
					String value = ToolForExcel.getCellValueAsString(row.getCell(colIdx));
					if (value != null && value.length() > 0) {
						value = value.trim();
					}
					rowStr[idx] = value;
				}
				rst.add(rowStr);
			}
		}

		if (fin != null) {
			try {
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rst;
	}

	private static String getCellValueAsString(Cell cell) {
		String value = "";
		if (!FrameTool.isEmpty(cell)) {
			switch (cell.getCellTypeEnum()) {
			case STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			case NUMERIC:
				long num = (long) cell.getNumericCellValue();
				value = Long.toString(num);
				break;
			case BOOLEAN:
				value = Boolean.toString(cell.getBooleanCellValue());
				break;
			case FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				break;
			}
		}
		return value.trim();
	}

	private static String getExcelFileType(File file) throws IOException {

		return getExcelFileType(new FileInputStream(file));
	}

	private static String getExcelFileType(InputStream in) throws IOException {
		InputStream inp = null;
		try {
			if (!in.markSupported()) {
				inp = new PushbackInputStream(in, 8);
			}
			if (POIFSFileSystem.hasPOIFSHeader(inp)) {
				return ToolForExcel.excel_type_2003;
			}
			if (DocumentFactoryHelper.hasOOXMLHeader(inp)) {
				return ToolForExcel.excel_type_2007;
			}
			return null;
		} finally {
			inp.close();
		}
	}

}
