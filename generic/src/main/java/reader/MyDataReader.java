package reader;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import java.io.FileInputStream;

public class MyDataReader {
	
	protected XSSFSheet ExcelWSheet;
	protected XSSFWorkbook excelWBook;
	protected XSSFCell Cell;
	protected XSSFRow Row;

	public MyDataReader(){}

	public MyDataReader(String path) {
		try{
			setExcelFile(path);
		} catch(Exception e) {
			Reporter.log(e.getMessage());
		}
	}

	public void setExcelFile(String Path) throws Exception {
		 
			try {
			FileInputStream ExcelFile = new FileInputStream(Path);
			excelWBook = new XSSFWorkbook(ExcelFile);
			///ExcelWSheet = ExcelWBook.getSheet(SheetName);
			} catch (Exception e){
				throw (e);
			}

	}// end of setexcelfile
	
	@SuppressWarnings("null")
	public String[][] getExcelSheetData(String sheetname) throws Exception {
		String[][] data = null;
		String[][] mydata = null;

		if (excelWBook != null) {
			XSSFSheet sheet = excelWBook.getSheet(sheetname);
			if (sheet != null) {
				int rows = sheet.getLastRowNum();
				int cols = sheet.getRow(sheet.getLastRowNum()).getPhysicalNumberOfCells();
				int arrayrow = rows+1;
				data = new String[arrayrow][cols];
				mydata = new String[rows][cols];


				for (int i = 0; i < arrayrow; i++) {
					for (int j = 0; j < cols; j++) {
						Cell cell = sheet.getRow(i).getCell(j);
						String cellData = cell.getStringCellValue();
						data[i][j] = cellData;
					}
					}
					// copied array to get rid of header row
					for(int m=0; m<rows; m++){
						for (int n=0; n<cols; n++){
							mydata[m][n] = data[m+1][n];

						}
					}

				}
			}
			
			return mydata;
		} //// end of getexcel sheet data

	public String[][] getSpecifiedRecordsFromExcelSheet(String sheetname, int from, int to) throws Exception {
         MyDataReader myDataReader = new MyDataReader();
		String[][] data = myDataReader.getExcelSheetData(sheetname);

		String[][] specifiedData = null;

		/**
		 *
		 * Manipulate data object(full table) to get only records as required eg. 1 -2 , 4- 6 records and store in specifiedData 2D object
		 *
		 * */

    return specifiedData;

	}

} // end of class
