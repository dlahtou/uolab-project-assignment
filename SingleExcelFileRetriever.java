package fileProcessing;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.File;

public class SingleExcelFileRetriever {
	
	public SingleExcelFileRetriever() {
		
	}
	
	public Student retrieveStudentData(File file) {
		BufferedInputStream stream = null;
		//POIFSFileSystem fs = null;
		XSSFWorkbook wb = null;
		XSSFSheet studentworksheet = null;
		
		// Open file and store excel sheet in studentworksheet
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
			//fs = new POIFSFileSystem(stream);
			wb = new XSSFWorkbook(stream);
			studentworksheet = wb.getSheetAt(1);
		} catch (IOException e) {
			System.out.println("Error reading excel input file");
			e.printStackTrace();
		} finally {
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					System.out.println("Error closing stream");
					e.printStackTrace();
				}
			}
		}
		
		Student student = new Student();
		
		// Process student worksheet if successfully retrieved
		if (studentworksheet != null) {
			Row choicesrow = studentworksheet.getRow(2);
			for (Cell cell : choicesrow) {
				student.setStudentData(cell.getColumnIndex(), cell.getStringCellValue());
			}
			/* Confirmation of proper read via console
			student.printPreferences();
			student.printAvailabilities();
			*/
		}
		
		return student;
	}

}
