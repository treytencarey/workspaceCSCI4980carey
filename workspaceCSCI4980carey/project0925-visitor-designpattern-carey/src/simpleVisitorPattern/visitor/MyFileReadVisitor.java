package simpleVisitorPattern.visitor;

import java.io.File;
import java.util.List;

import simpleVisitorPattern.part.Body;
import simpleVisitorPattern.part.Brake;
import simpleVisitorPattern.part.Engine;
import simpleVisitorPattern.part.Wheel;
import util.UtilFile;

public class MyFileReadVisitor extends CarPartVisitor {

   private String workDir = System.getProperty("user.dir");
   private List<String> contents;

   public MyFileReadVisitor() {
      contents = UtilFile.readFile("C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project0925-visitor-designpattern-carey/inputdata.txt");
   }

   @Override
   public void visit(Wheel part) {
      final int LINE_NUM_WHEEL = 0;
      String[] tokens = contents.get(LINE_NUM_WHEEL).split(",");
      part.setName(tokens[0].trim());
      part.setModelNumberWheel(tokens[1].trim());
      part.setModelYearWheel(tokens[2].trim());
   }

   @Override
   public void visit(Engine part) {
      final int LINE_NUM_ENGINE = 1;
      String[] tokens = contents.get(LINE_NUM_ENGINE).split(",");
      part.setName(tokens[0].trim());
      part.setModelNumberEngine(tokens[1].trim());
      part.setModelYearEngine(tokens[2].trim());
   }

   @Override
   public void visit(Body part) {
      final int LINE_NUM_BODY = 2;
      String[] tokens = contents.get(LINE_NUM_BODY).split(",");
      part.setName(tokens[0].trim());
      part.setModelNumberBody(tokens[1].trim());
      part.setModelYearBody(tokens[2].trim());
   }

   @Override
   public void visit(Brake part) {
      final int LINE_NUM_BODY = 3;
      String[] tokens = contents.get(LINE_NUM_BODY).split(",");
      part.setName(tokens[0].trim());
      part.setModelNumberBody(tokens[1].trim());
      part.setModelYearBody(tokens[2].trim());
   }
}
