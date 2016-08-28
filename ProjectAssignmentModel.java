package fileProcessing;

import com.gams.api.GAMSCheckpoint;
import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSException;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSParameter;
import com.gams.api.GAMSSet;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSWorkspace;

import java.io.PrintStream;
import java.io.IOException;

public class ProjectAssignmentModel {

	private GAMSSet studentnames, projectIDs;
	private GAMSParameter groupsizemin, groupsizemax, studentprefscore;
	private GAMSVariable projectapproval, totalscore;
	private GAMSDatabase fdbIn, fdbOut;
	private GAMSOptions fopt;
	
	private GAMSWorkspace fws;
	private GAMSJob job;
	
	public ProjectAssignmentModel(GAMSWorkspace ws) {
		fws = ws;
		
		// specifies the name of the database as it is seen from inside GAMS program
		fdbIn = ws.addDatabase("dbIn");
		
		// relates the incoming added dbin to the in program dbin
		fopt.defines("dbIn", "dbIn");
		fopt.setAllModelTypes("Cplex");
		fopt.defines("dbOut", "dbOut");
		
		studentnames = fdbIn.addSet("s", "students");
		projectIDs = fdbIn.addSet("p", "projects");
		
		groupsizemin = fdbIn.addParameter("groupsizemin", "minimum group size", projectIDs);
		groupsizemax = fdbIn.addParameter("groupsizemax", "maximum group size", projectIDs);
		studentprefscore = fdbIn.addParameter("studentprefscore", "point score of student preference rank", new Object[] {studentnames,projectIDs});
		
		job = ws.addJobFromFile("C:\\Users\\William\\Documents\\ProjectAssignment.gms");
		
		PrintStream output = null;
		try {
			output = new PrintStream("C:\\Users\\William\\Documents\\ProjectAssignmentOutput.txt");
			job.run(output);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	finally {
			if (output != null) {
				output.close();
			}
		}
	}
	
	
	// Getters for GAMS objects
	public GAMSSet getStudentnames() {
		return studentnames;
	}
	public GAMSSet getProjectIDs() {
		return projectIDs;
	}
	public GAMSParameter getGroupsizemin() {
		return groupsizemin;
	}
	public GAMSParameter getGroupsizemax() {
		return groupsizemax;
	}
	public GAMSParameter getStudentprefscore() {
		return studentprefscore;
	}
	public GAMSVariable getProjectapproval() {
		return projectapproval;
	}
	public GAMSVariable getTotalscore() {
		return totalscore;
	}
	public GAMSWorkspace getFws() {
		return fws;
	}
	public GAMSJob getJob() {
		return job;
	};
	public GAMSOptions getFopt() { return fopt;};
}
