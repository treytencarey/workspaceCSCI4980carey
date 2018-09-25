package simpleVisitorPattern.visitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Brake;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;

public class MyFileSaveVisitor extends CarPartVisitor {
	String outputName = "C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project0925-visitor-designpattern-carey/outputdata.csv";
	
	public void visit(Wheel part) {
		String row = part.getName() + "," + part.getModelNumberWheel() + "," + part.getModelYearWheel() + "\n";
		
		BufferedWriter writer;
	    try {
	    	writer = new BufferedWriter(new FileWriter(outputName));
			writer.write(row);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(Engine part) {
		String row = part.getName() + "," + part.getModelNumberEngine() + "," + part.getModelYearEngine() + "\n";
		
		BufferedWriter writer;
	    try {
	    	writer = new BufferedWriter(new FileWriter(outputName, true));
			writer.append(row);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(Body part) {
		String row = part.getName() + "," + part.getModelNumberBody() + "," + part.getModelYearBody() + "\n";
		
		BufferedWriter writer;
	    try {
	    	writer = new BufferedWriter(new FileWriter(outputName, true));
			writer.append(row);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(Brake part) {
		String row = part.getName() + "," + part.getModelNumberBody() + "," + part.getModelYearBody() + "\n";
		
		BufferedWriter writer;
	    try {
	    	writer = new BufferedWriter(new FileWriter(outputName, true));
			writer.append(row);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}