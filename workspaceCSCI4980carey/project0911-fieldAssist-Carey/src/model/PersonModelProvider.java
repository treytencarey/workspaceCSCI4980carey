package model;

import java.util.ArrayList;
import java.util.List;

import util.UtilFile;

public enum PersonModelProvider {
	// INSTANCE;
	INSTANCE(getFilePath()); // Call a constructor with a parameter. 

	private List<Person> persons;

        // Load the data sets from a file dynamically. 
	private PersonModelProvider(String inputdata) {
		List<String> contents = UtilFile.readFile(inputdata);
		List<List<String>> tableContents = UtilFile.convertTableContents(contents);

		persons = new ArrayList<Person>();
		for (List<String> iList : tableContents) {
			persons.add(new Person(iList.get(0), iList.get(1), iList.get(2), iList.get(3).equals("true")));
		}
	}

	private static String getFilePath() {
		return "C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project0911-fieldAssist-Carey/inputdata.txt";
	}

	public List<Person> getPersons() {
		return persons;
	}
}
