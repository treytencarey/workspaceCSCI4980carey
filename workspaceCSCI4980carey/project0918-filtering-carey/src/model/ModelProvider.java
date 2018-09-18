package model;

import java.util.ArrayList;
import java.util.List;

import util.UtilFile;

public enum ModelProvider {
	INSTANCE;

	private List<Person> persons;

	private ModelProvider() {
		persons = new ArrayList<Person>();
		
		List<String> fileCont = UtilFile.readFile("C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project0918-filtering-carey/inputdata.txt");
		List<List<String>> tableCont = UtilFile.convertTableContents(fileCont);
		for (List<String> personCont : tableCont)
		{
			persons.add(new Person(personCont.get(0), personCont.get(1), personCont.get(2)));
		}
	}

	public List<Person> getPersons() {
		return persons;
	}
}
