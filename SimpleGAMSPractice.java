package fileProcessing;

import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSJob;
import java.io.PrintStream;
import java.io.FileNotFoundException;

public class SimpleGAMSPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GAMSWorkspace ws = new GAMSWorkspace();
		PrintStream output = null;
		GAMSJob job = ws.addJobFromFile("C:\\Users\\William\\Desktop\\hw2firstsolve.gms");
		try {
			output = new PrintStream("C:\\Users\\William\\My Documents\\thingsandstuff.txt");
			job.run(output);
		}
		catch (FileNotFoundException e) {
			System.out.print(e.getLocalizedMessage());
		}
		finally {
			if (output != null) {
				output.close();
			}
		}
		
	}

}
