package fileProcessing;

import java.util.HashMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSSet;
import com.gams.api.GAMSParameter;

public class GAMSSolver {
	public static void main(String[] args) {
		
	    // create a directory
        File workingDirectory = new File("C:\\UOLab GAMS Directory", "ProjectAssignment");
        workingDirectory.mkdir();

        // create a workspace
        GAMSWorkspaceInfo  wsInfo  = new GAMSWorkspaceInfo();
        wsInfo.setWorkingDirectory(workingDirectory.getAbsolutePath());

        GAMSWorkspace ws = new GAMSWorkspace(wsInfo);
		
		//Prepare input data
        HashMap<String,Student> studentlist = new StudentListBuilder().buildStudentList();
        
		//Sets are entered as strings one-by-one using GAMSSet.addRecord(String s)
		List<String> studentnames = new ArrayList<String>();
		 
		 for (Student joestudent : studentlist.values()) {
			System.out.println(joestudent.getName() + " added to studentnames set");
			studentnames.add(joestudent.getName());
			joestudent.printPreferences();
		 }
		 
		List<String> projectnames = Arrays.asList("Coolproject", "Lameproject", "Newproject");
		 
		 //Multi-Dimensional parameters are entered using GAMSParamter.addRecord(Vector<String> v, Double d)
		Map<Vector<String>,Double> studentprefscoreshashmap = new HashMap<Vector<String>, Double>();
		
		//Assign scores to each key <studentname,project>
		for (Student joestudent : studentlist.values()) {
			for (String project : projectnames) {
				Vector<String> mapkey = new Vector<String>(Arrays.asList(joestudent.getName(),project));
				double k = 0;
				if (project.equals(joestudent.getFirstChoice()))
					k = 1.1;
				else if (project.equals(joestudent.getSecondChoice()))
					k = 1.01;
				else if (project.equals(joestudent.getThirdChoice()))
					k = 1;
				studentprefscoreshashmap.put(mapkey, k);
			}
		}
		
		//instantiate GAMS Database
		GAMSDatabase db = ws.addDatabase();
		
		//Add Sets to GAMSDatabase db
		GAMSSet s = db.addSet("s", 1, "students");
		GAMSSet p = db.addSet("p", 1, "projects");
		
		//Place values in sets from input data section
		for (String name : studentnames)
			s.addRecord(name);
		
		for (String project : projectnames)
			p.addRecord(project);
		
		//Add Parameter studentprefscore
		GAMSParameter studentprefscore = db.addParameter("studentprefscore", 2, "scored values of students' preference rankings");
		
		//Place values in parameters from input data section
		for (Vector<String> studentandproject : studentprefscoreshashmap.keySet())
			studentprefscore.addRecord(studentandproject).setValue(studentprefscoreshashmap.get(studentandproject));
	
		//Set GAMS Database input file name to 'dbIn' or whatever is specified in the .gms file
		GAMSOptions opt = ws.addOptions();
		opt.defines("dbIn", db.getName());
		
		//Set .gms file location and add as GAMSJob
		GAMSJob simpleassign = ws.addJobFromFile("C:\\Users\\William\\Desktop\\ProjectAssignment.gms");
		
		simpleassign.run(opt, db);
		
		Map<String, Integer> groupsizetotals = new HashMap<String, Integer>();
		for (String project : projectnames)
			groupsizetotals.put(project, 0);
		
		//gets results of the student-project assignment from the GAMS binary variable 'x' levels after running
        GAMSVariable assignment = simpleassign.OutDB().getVariable("x");
        for(GAMSVariableRecord rec : assignment) {
        	String studentname = rec.getKey(0);
        	String projectname = rec.getKey(1);
        	if ((int) rec.getLevel() == 1) {
        		System.out.println(studentname + " assigned to project " + projectname);
        		int sizeholder = groupsizetotals.get(projectname);
        		groupsizetotals.put(projectname, sizeholder+1);
        	}
            	//The following line displays unnecessary data
        		//System.out.println("assignment(" + rec.getKeys()[0] + ", " + rec.getKeys()[1] + "): level=" + rec.getLevel() + " marginal=" + rec.getMarginal());
        }
        
        //prints results of project approvals from the GAMS binary variable 'a' levels after running
        GAMSVariable approval = simpleassign.OutDB().getVariable("a");
        for(GAMSVariableRecord rec : approval) {
        	String projectname = rec.getKey(0);
        	if ((int) rec.getLevel() == 1) 
        		System.out.println(projectname + " approved with " + groupsizetotals.get(projectname) + " students");
        }
        
        //print results of extraordinary sized groups, binary variable 'e'
        int extcount = 0;
        GAMSVariable extraordinary = simpleassign.OutDB().getVariable("e");
        for(GAMSVariableRecord rec : extraordinary) {
        	String projectname = rec.getKey(0);
        	if ((int) rec.getLevel() == 1) {
        		extcount++;
        		System.out.println(projectname + " has extraordinary size");
        	}
        }
        if (extcount == 0)
        	System.out.println("No groups of extraordinary size");
	}
	
	

}
