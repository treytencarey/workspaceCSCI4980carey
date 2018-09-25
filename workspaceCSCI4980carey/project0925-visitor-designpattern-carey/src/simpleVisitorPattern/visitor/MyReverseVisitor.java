package simpleVisitorPattern.visitor;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Brake;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;

public class MyReverseVisitor extends CarPartVisitor {
	public String reverse(String name)
	{
		String newName = "";
		
		String strEachWord[] = name.split("\\s+");
		for (String word : strEachWord)
		{
			StringBuilder sb = new StringBuilder(word);
			sb.reverse();
			
			newName += sb + " ";
		}
		// Get rid of ending space
		if (newName.length() > 0)
			newName = newName.substring(0, newName.length()-1);
		
		return newName;
	}
	
   public void visit(Wheel part) {
      part.setName(reverse(part.getName()));
      part.setModelNumberWheel(reverse(part.getModelNumberWheel()));
      part.setModelYearWheel(reverse(part.getModelYearWheel()));
   }

   public void visit(Engine part) {
	   part.setName(reverse(part.getName()));
	   part.setModelNumberEngine(reverse(part.getModelNumberEngine()));
	   part.setModelYearEngine(reverse(part.getModelYearEngine()));
   }

   public void visit(Body part) {
	   part.setName(reverse(part.getName()));
	   part.setModelNumberBody(reverse(part.getModelNumberBody()));
	   part.setModelYearBody(reverse(part.getModelYearBody()));
   }

   public void visit(Brake part) {
	   part.setName(reverse(part.getName()));
	   part.setModelNumberBody(reverse(part.getModelNumberBody()));
	   part.setModelYearBody(reverse(part.getModelYearBody()));
   }
}