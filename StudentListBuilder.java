package fileProcessing;

import java.io.File;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class StudentListBuilder {

	public StudentListBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<String, Student> allthestudents = new HashMap<>();
	
	public HashMap<String, Student> buildStudentList() {
		File folder = new File("C:\\UOLab Project Assignment xlsx Files\\");
		File[] listoffiles = folder.listFiles();
		
		for (File file : listoffiles) {
			//System.out.println(file.getName());
			SingleExcelFileRetriever retriever = new SingleExcelFileRetriever();
			Student joestudent = retriever.retrieveStudentData(file);
			allthestudents.put(joestudent.getName(),joestudent);
		}
		
		return allthestudents;
		
		/* for (Student joestudent : allthestudents.values()) {
			System.out.println(joestudent.getName());
			joestudent.printPreferences();
		} */
	}
	
	
}
