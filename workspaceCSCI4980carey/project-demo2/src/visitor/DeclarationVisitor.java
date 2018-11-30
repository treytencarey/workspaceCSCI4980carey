/*
 * @(#) MethodVisitor.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import analysis.ProjectAnalyzer;
import graph.model.GClassNode;
import graph.model.GConnection;
import graph.model.GMethodNode;
import graph.model.GNode;
import graph.model.GOrganizerNode;
import graph.model.GPackageNode;
import graph.provider.GModelProvider;
import model.Organizer;
import model.OrganizerModelProvider;
import util.UtilNode;

public class DeclarationVisitor extends ASTVisitor {
	
	public static Map<String, ArrayList<String>> methodVariables = new HashMap<String, ArrayList<String>>();

	public boolean visit(PackageDeclaration pkgDecl) {
		insertPackageNode(pkgDecl);
		return super.visit(pkgDecl);
	}

	private void insertPackageNode(PackageDeclaration pkgDecl) {
		IPackageBinding rBinding = pkgDecl.resolveBinding();
		String prjName = rBinding.getJavaElement().getJavaProject().getElementName();
		String nodeName = pkgDecl.getName().getFullyQualifiedName();
		String id = prjName + "." + nodeName;
		GPackageNode pkgNode = null;
		if (GModelProvider.instance().getNodeMap().containsKey(id) == false) {
			pkgNode = new GPackageNode(id, nodeName, prjName);
			addNode(pkgNode);
		}
		else
			return;
		
		for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
		{
			if (organizer.getClassOrPackage() == 1 && nodeName.startsWith(organizer.getClassOrPackageName()))
			{
				GOrganizerNode orgNode = new GOrganizerNode(organizer.toString(), organizer.getMethodOrVariableName(), id);
				addNode(orgNode);
				addConnection(pkgNode, orgNode, 0);
			}
		}
	}

	/**
	 * A type declaration is the union of a class declaration
	 * and an interface declaration.
	 */
	@Override
	public boolean visit(TypeDeclaration typeDecl) {
		GNode typeNode = insertTypeNode(typeDecl);
		String typeName = typeDecl.getName().getFullyQualifiedName();
		GNode pkgGNode = GModelProvider.instance().getNodeMap().get(typeNode.getParent());
		if (pkgGNode == null) {
			throw new RuntimeException();
		}
		addConnection(pkgGNode, typeNode, typeDecl.getStartPosition());
		
		for (Organizer organizer : OrganizerModelProvider.INSTANCE.getOrganizers())
		{
			if (organizer.getClassOrPackage() == 0 && typeName.startsWith(organizer.getClassOrPackageName()))
			{
				GOrganizerNode orgNode = new GOrganizerNode(organizer.toString(), organizer.getMethodOrVariableName(), typeNode.getId());
				addNode(orgNode);
				addConnection(typeNode, orgNode, 0);
			}
		}
		
		return super.visit(typeDecl);
	}

	private GNode insertTypeNode(TypeDeclaration typeDecl) {
		ITypeBinding rBinding = typeDecl.resolveBinding();
		String prjName = rBinding.getPackage().getJavaElement().getJavaProject().getElementName();
		String pkgName = rBinding.getPackage().getName();

		String typeName = typeDecl.getName().getFullyQualifiedName();
		String id = prjName + "." + pkgName + "." + typeName;
		GClassNode n = new GClassNode(id, typeName, prjName + "." + pkgName);
		n.setPkgName(pkgName);
		return addNode(n);
	}

	@Override
	public boolean visit(MethodDeclaration methodDecl) {
		GMethodNode methodNode = (GMethodNode) insertMethodNode(methodDecl);
		GNode typeNode = GModelProvider.instance().getNodeMap().get(methodNode.getParent());
		if (typeNode == null) {
			throw new RuntimeException();
		}
		addConnection(typeNode, methodNode, methodDecl.getStartPosition());
		
		ASTNode astNode = UtilNode.getOuterClass(methodDecl);
      // System.out.println("METHOD FOUND (" + UtilNode.getName(astNode) + ", " + mNode.getStartPosition() + ", " + mNode.getLength() + "): " + mNode.getName().getFullyQualifiedName());
      
      if (ProjectAnalyzer.methodsToMove == null)
    	  ProjectAnalyzer.methodsToMove = new HashMap<String, Map<String, ArrayList<Integer>>>();
      
      Map<String, ArrayList<Integer>> classToPos = ProjectAnalyzer.methodsToMove.get(UtilNode.getName(astNode));
      if (classToPos == null)
    	  classToPos = new HashMap<String, ArrayList<Integer>>();
      ArrayList<Integer> pos = new ArrayList<Integer>();
      pos.add(methodDecl.getStartPosition()); pos.add(methodDecl.getLength());
      classToPos.put(methodDecl.getName().getFullyQualifiedName(), pos);
      
      ProjectAnalyzer.methodsToMove.put(UtilNode.getName(astNode), classToPos);
		
	  return super.visit(methodDecl);
	}

	private GNode insertMethodNode(MethodDeclaration methodDecl) {
		IMethodBinding rBinding = methodDecl.resolveBinding();
		ITypeBinding typeBinding = rBinding.getDeclaringClass();
		String prjName = typeBinding.getPackage().getJavaElement().getJavaProject().getElementName();
		String pkgName = typeBinding.getPackage().getName();
		String className = typeBinding.getName();

		String methodName = methodDecl.getName().getFullyQualifiedName();
		String parent = prjName + "." + pkgName + "." + className;
		String id = parent + "." + methodName;
		GMethodNode n = new GMethodNode(id, methodName, parent);
		n.setPrjName(prjName).setPkgName(pkgName).setClassName(className);
		
		ArrayList<String> variables = new ArrayList<String>();
	      methodDecl.accept(new ASTVisitor() {
	          public boolean visit(VariableDeclarationFragment fd) {
	              variables.add(fd.toString());
	              return false;
	          }
	      });
	    for (Object param : methodDecl.parameters())
	    	variables.add(((SingleVariableDeclaration)param).getName().toString());
	    methodVariables.put(prjName + "." + pkgName + "." + className + "." + methodName, variables);
		
		return addNode(n);
	}

	private void addConnection(GNode srcNode, GNode dstNode, int offset) {
		String conId = srcNode.getId() + dstNode.getId();
		String conLabel = "offset: " + offset;
		GConnection con = new GConnection(conId, conLabel, srcNode, dstNode);
		GModelProvider.instance().getConnections().add(con);
		srcNode.getConnectedTo().add(dstNode);
	}

	private GNode addNode(GNode n) {
		GModelProvider.instance().getNodes().add(n);
		GModelProvider.instance().getNodeMap().put(n.getId(), n);
		return n;
	}
}
