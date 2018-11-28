/*
 * @(#) ASTAnalyzer.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package analysis;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import graph.provider.GModelProvider;
import model.Organizer;
import model.OrganizerModelProvider;
import util.UtilPlatform;
import visitor.DeclarationVisitor;

public class ProjectAnalyzer {
   private static final String JAVANATURE = "org.eclipse.jdt.core.javanature";
   protected String prjName, pkgName;
   public static Map<String, Map<String, ArrayList<Integer>>> methodsToMove; // Map a class to a method to positions

   public void analyze() {
      GModelProvider.instance().reset();

      // =============================================================
      // 1st step: Project
      // =============================================================
      try {
         IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
         for (IProject project : projects) {
            if (!project.isOpen() || !project.isNatureEnabled(JAVANATURE)) { // Check if we have a Java project.
               continue;
            }
            prjName = project.getName();
            analyzePackages(JavaCore.create(project).getPackageFragments());
         }
      } catch (JavaModelException e) {
         e.printStackTrace();
      } catch (CoreException e) {
         e.printStackTrace();
      }
   }

   protected void analyzePackages(IPackageFragment[] packages) throws CoreException, JavaModelException {
      // =============================================================
      // 2nd step: Packages
      // =============================================================
      for (IPackageFragment iPackage : packages) {
         if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
            if (iPackage.getCompilationUnits().length < 1) {
               continue;
            }
            pkgName = iPackage.getElementName();
            analyzeCompilationUnit(iPackage.getCompilationUnits());
         }
      }
   }

   private void analyzeCompilationUnit(ICompilationUnit[] iCompilationUnits) throws JavaModelException {
      // =============================================================
      // 3rd step: ICompilationUnits
      // =============================================================
      for (ICompilationUnit iUnit : iCompilationUnits) {
         CompilationUnit compilationUnit = parse(iUnit);
         DeclarationVisitor declVisitor = new DeclarationVisitor();
         compilationUnit.accept(declVisitor);
      
		  String source = iUnit.getSource();
		  // System.out.println("Source: " + source);
		  
		  for (String classString : methodsToMove.keySet())
		  {
		 	 // Get positions of methods
		 	 Map<Integer, Integer> orderPositions = new TreeMap<Integer, Integer>();
		 	 for (String methodString : methodsToMove.get(classString).keySet())
		 	 {
		 		 ArrayList<Integer> pos = methodsToMove.get(classString).get(methodString);
		 		 if (orderPositions.get(0) == null || pos.get(0) < orderPositions.get(0))
		 			 orderPositions.put(0, pos.get(0));
		 	 }
		 	 
		 	 for (String methodString : methodsToMove.get(classString).keySet())
		 	 {
		 		 ArrayList<Integer> pos = methodsToMove.get(classString).get(methodString);
		 		 
		 		 // System.out.println("In class (" + classString + "), in method (" + methodString + "), pos is at: (" + pos.get(0) + ", " + pos.get(1) + ")");
		 	 
		 		 for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
		 		 {
		 			 if (organizer.getClassOrPackage() == 0 && methodString.startsWith(organizer.getMatch()))
		 			 {
		 				 // System.out.println("\tMethod should be in order: " + organizer.getOrder());
		 				 
		 				 int begLine = pos.get(0);
		 				 while (source.charAt(begLine) != '\n') { begLine -= 1; }
		 				 
		 				 String methodSource = source.substring(begLine, pos.get(0)+pos.get(1)) + "\n";
		 				 
		 				 try {
		 					 source = source.substring(0, orderPositions.get(0)) + methodSource + source.substring(orderPositions.get(0), begLine) + source.substring(pos.get(0)+pos.get(1));
		 				 } catch (Exception e)
		 				 {
		 					 
		 				 }
		 				 
		 				 // System.out.println("New Source: " + source);
		 			 }
		 		 }
		 	 }
		  }
		  methodsToMove.clear();
		  
		  try {
		         // Modify buffer and reconcile
		         IBuffer buffer = ((IOpenable)iUnit).getBuffer();
		         buffer.setContents(source);
		         iUnit.reconcile(ICompilationUnit.NO_AST, false, null, null);
		         iUnit.commitWorkingCopy(false, null);
		  } catch (Exception e) {
		 	 
		  }
		
		  UtilPlatform.indentAndSave(iUnit);
      }
   }

   private static CompilationUnit parse(ICompilationUnit unit) {
      ASTParser parser = ASTParser.newParser(AST.JLS10);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(unit);
      parser.setResolveBindings(true);
      return (CompilationUnit) parser.createAST(null); // parse
   }
}