package com.xl.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToolForExcel {

	public static final String excel_type_2003 = "2003";
	public static final String excel_type_2007 = "2007";
	private static final String sheet_name_default = "sheet1";

	public static Workbook getWorkbookFromExcelFile(String filePath) {
		File excelFile = new File(filePath);
		return getWorkbookFromExcelFile(excelFile);
	}

	public static Workbook getWorkbookFromExcelFile(File excelFile) {
		FileInputStream streamForBook = null;
		try {
			streamForBook = new FileInputStream(excelFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Workbook wb = null;
		try {
			String excelType = getExcelFileType(excelFile);
			if (ToolForExcel.excel_type_2003.equals(excelType)) {
				wb = new HSSFWorkbook(streamForBook);
			}
			if (ToolForExcel.excel_type_2007.equals(excelType)) {
				wb = new XSSFWorkbook(streamForBook);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				streamForBook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

		return buildListFromExcel(file, startRow, startCol, -1);
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

		List<String[]> rst = new ArrayList<String[]>();
		Workbook workbook = getWorkbookFromExcelFile(file);

		if (workbook != null) {
			Sheet worksheet = workbook.getSheetAt(0);
			for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
				Row row = worksheet.getRow(rowIdx);
				if (endCol < 0) {
					endCol = row.getLastCellNum();
				}
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
			workbook.close();
		}

		return rst;
	}

	public static void randomWriteExcelFile(File file, List<CellInfo> writeParams) throws IOException {

		FileOutputStream fout = null;
		try {
			Workbook workbook = getWorkbookFromExcelFile(file);
			if (workbook != null) {
				Sheet worksheet = workbook.getSheetAt(0);
				for (CellInfo writeParam : writeParams) {
					Row row = worksheet.getRow(writeParam.getRow());
					if (row == null) {
						row = worksheet.createRow(writeParam.getRow());
					}
					Cell cell = row.getCell(writeParam.getCol());
					if (cell == null) {
						cell = row.createCell(writeParam.getCol());
					}
					cell.setCellValue(writeParam.getValue());
				}
				fout = new FileOutputStream(file);
				workbook.write(fout);
				workbook.close();
			}
		} finally {
			fout.close();
		}
	}

	public static void cleanColumn(File file, int startRow, int columnNum) throws IOException {
		FileOutputStream fout = null;
		try {
			Workbook workbook = getWorkbookFromExcelFile(file);
			if (workbook != null) {
				Sheet worksheet = workbook.getSheetAt(0);
				for (int rowIdx = startRow, maxRow = worksheet.getLastRowNum(); rowIdx <= maxRow; rowIdx++) {
					Row row = worksheet.getRow(rowIdx);
					if (row != null) {
						Cell cell = row.getCell(columnNum);
						if (cell != null) {
							row.removeCell(cell);
						}
					}
				}
				fout = new FileOutputStream(file);
				workbook.write(fout);
				workbook.close();
			}
		} finally {
			fout.close();
		}
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

	public static String getCellValueAsString(Cell cell) {
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

	public static String getExcelFileType(File file) throws IOException {

		return getExcelFileType(new FileInputStream(file));
	}

	public static String getExcelFileType(InputStream in) throws IOException {
		InputStream inp = null;
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
	}

	public static CellInfo getNewCellInfo(int row, int col, String value) {
		return new ToolForExcel.CellInfo(row, col, value);
	}

	public static class CellInfo {
		private int row;
		private int col;
		private String value;

		public CellInfo(int row, int col, String value) {
			this.row = row;
			this.col = col;
			this.value = value;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public String getValue() {
			return value;
		}

	}

	public static void makeExlFile(File exlFile, List<Map> datas, List<String> keys) throws IOException {
		makeExlFile(exlFile, null, datas, keys);
	}

	public static void makeExlFile(File exlFile, String sheetName, List<Map> datas, List<String> keys)
			throws IOException {
		HSSFWorkbook workbook = fillExlWorkBook(getSheetName(sheetName), datas, keys);
		writeWorkbookToFile(exlFile, workbook);
	}

	private static void writeWorkbookToFile(File exlFile, HSSFWorkbook workbook) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(exlFile);
			workbook.write(fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	private static HSSFWorkbook fillExlWorkBook(String sheetName, List<Map> datas, List<String> keys) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(getSheetName(sheetName));
		fillExlSheet(sheet, datas, keys);
		return workbook;
	}

	private static void fillExlSheet(HSSFSheet sheet, List<Map> datas, List<String> keys) {
		for (int i = 0, len = datas.size(); i < len; i++) {
			fillExcelOneRow(sheet.createRow(i), datas.get(i), keys);
		}

	}

	private static void fillExcelOneRow(HSSFRow row, Map data, List<String> keys) {
		for (int i = 0, len = keys.size(); i < len; i++) {
			Cell cell = row.createCell(i);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(data.get(keys.get(i)).toString());
		}
	}

	private static String getSheetName(String sheetName) {
		if (FrameTool.isEmpty(sheetName)) {
			return ToolForExcel.sheet_name_default;
		}
		return sheetName;
	}

}
