package fileProcessing;

import java.util.List;
import java.util.ArrayList;

public class Student {
	
	private String name = "";
	private List<String> preferences = new ArrayList<String>();
	private List<String> availabilities = new ArrayList<String>();
	
	public void setStudentData(int column, String inputdata) {
		switch(column) {
		case 0:
			name = inputdata;
			System.out.println(inputdata + " successfully entered");
			break;
		case 1:
		case 2:
		case 3:
			preferences.add(inputdata);
			break;
		case 4:
		case 5:
		case 6:
			availabilities.add(inputdata);
			break;
		default:
			break;
		}

	}
	
	public void printPreferences() {
		preferences.forEach(preference -> System.out.println(preference));
	}
	
	public void printAvailabilities() {
		availabilities.forEach(availability -> System.out.println(availability));
	}
	
	public String getName() {
		return name;
	}
	
	public String getFirstChoice() {
		if (preferences.size() > 0)
			return preferences.get(0);
		else {
			System.out.println(name + "  has no first choice!");
			return "Coolproject";
		}
	}
	
	public String getSecondChoice() {
		if (preferences.size() > 1) {
			return preferences.get(1);
		}
		else {
			System.out.println(name + "  has no second choice!");
			return "Coolproject";
		}
	}
	
	public String getThirdChoice() {
		if (preferences.size() > 2) {
			return preferences.get(2);
		}
		else {
			System.out.println(name + "  has no third choice!");
			return "Coolproject";
		}
	}
	
	public Student() {
		// TODO Auto-generated constructor stub
	}

}
