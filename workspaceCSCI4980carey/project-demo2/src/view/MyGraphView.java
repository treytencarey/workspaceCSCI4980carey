/*
 * @(#) View.java
 *
 */
package view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EventObject;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import analysis.MoveMethodAnalyzer;
import analysis.ProjectAnalyzer;
import graph.model.GClassNode;
import graph.model.GMethodNode;
import graph.model.GNode;
import graph.model.GNodeType;
import graph.model.GPackageNode;
import graph.provider.GLabelProvider;
import graph.provider.GModelProvider;
import graph.provider.GNodeContentProvider;
import model.OrganizerModelProvider;
import util.UtilMsg;
import util.UtilNode;

public class MyGraphView {
   public static final String VIEW_ID = "simplezestproject5.partdescriptor.simplezestview5";

   private GraphViewer gViewer;
   private int layout = 0;
   private Menu mPopupMenu = null;
   private MenuItem menuItemAddOrganizerMethod = null, menuItemMoveMethod = null, menuItemOpenMethod = null, menuItemRefresh = null;
   private GraphNode selectedSrcGraphNode = null, selectedDstGraphNode = null;
   private GraphNode prevSelectedDstGraphNode = null;

   private GNode selectedGMethodNode = null, selectedGClassNode = null, selectedGPackageNode = null, selectedGClassOpenNode = null;
   private GNode prevSelectedGClassNode = null, prevSelectedGPackageNode = null;

   @PostConstruct
   public void createControls(Composite parent) {
      gViewer = new GraphViewer(parent, SWT.BORDER);
      gViewer.setContentProvider(new GNodeContentProvider());
      gViewer.setLabelProvider(new GLabelProvider());
      gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      gViewer.applyLayout();

      addPoupMenu();
      addMouseListenerGraphViewer();
   }

   private void addPoupMenu() {
      mPopupMenu = new Menu(gViewer.getControl());
      gViewer.getControl().setMenu(mPopupMenu);
      
      menuItemAddOrganizerMethod = new MenuItem(mPopupMenu, SWT.CASCADE);
      menuItemAddOrganizerMethod.setText("Add Organizer");
      addSelectionListenerMenuItemAddOrganizerMethod();

      menuItemMoveMethod = new MenuItem(mPopupMenu, SWT.CASCADE);
      menuItemMoveMethod.setText("Move Method");
      addSelectionListenerMenuItemMoveMethod();
      
      menuItemOpenMethod = new MenuItem(mPopupMenu, SWT.CASCADE);
      menuItemOpenMethod.setText("Open");
      addSelectionListenerMenuItemOpenMethod();

      menuItemRefresh = new MenuItem(mPopupMenu, SWT.CASCADE);
      menuItemRefresh.setText("Refresh");
      addSelectionListenerMenuItemRefresh();
   }

   private void addMouseListenerGraphViewer() {
      MouseAdapter mouseAdapter = new MouseAdapter() {
         public void mouseDown(MouseEvent e) {
            menuItemMoveMethod.setEnabled(false);
            menuItemAddOrganizerMethod.setEnabled(false);
            menuItemOpenMethod.setEnabled(false);
            resetSelectedSrcGraphNode();

            if (UtilNode.isMethodNode(e)) {
               System.out.println("single clicked");
               menuItemMoveMethod.setEnabled(true);

               selectedSrcGraphNode = (GraphNode) ((Graph) e.getSource()).getSelection().get(0);
               selectedSrcGraphNode.setBorderWidth(1);

               selectedGMethodNode = (GMethodNode) selectedSrcGraphNode.getData();
               selectedGMethodNode.setNodeType(GNodeType.UserSelection);
            }
            if (UtilNode.isClassNode(e) || UtilNode.isPackageNode(e))
            {
            	menuItemAddOrganizerMethod.setEnabled(true);
            }
            if (UtilNode.isClassNode(e))
            {
            	selectedGClassOpenNode = (GClassNode)((GraphNode)((Graph) e.getSource()).getSelection().get(0)).getData();
            	menuItemOpenMethod.setEnabled(true);
            }
         }

         @Override
         public void mouseDoubleClick(MouseEvent e) {
             if (UtilNode.isClassNode(e)) {
                System.out.println("double clicked");
                prevSelectedDstGraphNode = selectedDstGraphNode;
                selectedDstGraphNode = (GraphNode) ((Graph) e.getSource()).getSelection().get(0);

                prevSelectedGClassNode = selectedGClassNode;
                selectedGClassNode = (GClassNode) selectedDstGraphNode.getData();

                if (selectedGClassNode.eq(prevSelectedGClassNode)) {
                   // same node => marked => unmarked
                   if (selectedGClassNode.getNodeType().equals(GNodeType.UserDoubleClicked)) {
                      UtilNode.resetDstNode(selectedDstGraphNode, selectedGClassNode);
                   }
                   // same node => unmarked => marked
                   else if (selectedGClassNode.getNodeType().equals(GNodeType.InValid)) {
                      changeColorDDClikedNode(selectedGClassNode);
                   }
                } else {
                   // different node => marked && unmarked previous marked node
                   changeColorDDClikedNode(selectedGClassNode);
                   UtilNode.resetDstNode(prevSelectedDstGraphNode, prevSelectedGClassNode);
                }
             } else if (UtilNode.isPackageNode(e)) {
                 // TODO: Class Exercise
                prevSelectedDstGraphNode = selectedDstGraphNode;
                selectedDstGraphNode = (GraphNode) ((Graph) e.getSource()).getSelection().get(0);
             
                prevSelectedGPackageNode = selectedGPackageNode;
                selectedGPackageNode = (GPackageNode) selectedDstGraphNode.getData();
             
                if (selectedGPackageNode.eq(prevSelectedGPackageNode)) {
                   if (selectedGPackageNode.getNodeType().equals(GNodeType.UserDoubleClicked)) {
                      UtilNode.resetPackageNode(selectedDstGraphNode, selectedGPackageNode);
                   } else if (selectedGPackageNode.getNodeType().equals(GNodeType.InValid)) {
                      changeColorDDClikedNode(selectedGPackageNode);
                   }
                } else { 
                   changeColorDDClikedNode(selectedGPackageNode);
                   UtilNode.resetPackageNode(prevSelectedDstGraphNode, prevSelectedGPackageNode);
                } 
             }
          }
       };
      gViewer.getControl().addMouseListener(mouseAdapter);
   }

   private void changeColorDDClikedNode(GNode node) {
      if (this.selectedDstGraphNode == null)
         return;
      selectedDstGraphNode.setForegroundColor(ColorConstants.red);
      selectedDstGraphNode.setBackgroundColor(ColorConstants.blue);
      selectedDstGraphNode.setBorderColor(ColorConstants.blue);
      selectedDstGraphNode.setHighlightColor(ColorConstants.blue);
      selectedDstGraphNode.setBorderHighlightColor(ColorConstants.black);
      node.setNodeType(GNodeType.UserDoubleClicked);
   }
   
   private void addSelectionListenerMenuItemAddOrganizerMethod() {
	      SelectionListener menuItemListenerAddOrganizerMethod = new SelectionListener() {
	         @Override
	         public void widgetSelected(SelectionEvent e) {
	        	 AddDialog dialog = new AddDialog(new Shell());
	        	 dialog.open();
	        	 if (dialog.getOrganizer() != null) {
	            	 OrganizerModelProvider.INSTANCE.getOrganizers().add(dialog.getOrganizer());
	                 
	            	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(MyTableViewer.filePath, true))) {
	            	    writer.write(dialog.getOrganizer().toString() + "\n");
	            	 } catch (IOException ioe) {
	            	    System.err.format("IOException: %s%n", ioe);
	            	 }
	              }
	        	 System.out.println("[DBG] Add Organizer Dialog Shell");
	         }

	         @Override
	         public void widgetDefaultSelected(SelectionEvent e) {
	         }
	      };
	      menuItemAddOrganizerMethod.addSelectionListener(menuItemListenerAddOrganizerMethod);
	   }

   private void addSelectionListenerMenuItemMoveMethod() {
      SelectionListener menuItemListenerMoveMethod = new SelectionListener() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            if (!isNodesSelected()) {
               String msg = "Please select class and method nodes. " //
                     + "Select a class node by double-click and select a method node by single-click";
               UtilMsg.openWarning(msg);
               System.out.println("[DBG] " + msg);
               return;
            }
            if (((GMethodNode) selectedGMethodNode).isParent((GClassNode) selectedGClassNode)) {
               String msg = "Please select a different class as move destination.";
               UtilMsg.openWarning(msg);
               System.out.println("[DBG] " + msg);
               return;
            }
            System.out.println("[DBG] MenuItem MoveMethod");
            MoveMethodAnalyzer moveMethodAnalyzer = new MoveMethodAnalyzer();
            moveMethodAnalyzer.setMethodToBeMoved((GMethodNode) selectedGMethodNode);
            moveMethodAnalyzer.setClassMoveDestination((GClassNode) selectedGClassNode);
            moveMethodAnalyzer.analyze();
            moveMethodAnalyzer.moveMethod();
            resetSelectedSrcGraphNode();
            UtilNode.resetDstNode(selectedDstGraphNode, selectedGClassNode);
            syncZestViewAndJavaEditor();
         }

         private boolean isNodesSelected() {
            return selectedGMethodNode != null && selectedGMethodNode.getNodeType().equals(GNodeType.UserSelection) && //
            selectedGClassNode != null && selectedGClassNode.getNodeType().equals(GNodeType.UserDoubleClicked);
         }

         @Override
         public void widgetDefaultSelected(SelectionEvent e) {
         }
      };
      menuItemMoveMethod.addSelectionListener(menuItemListenerMoveMethod);
   }

   private void addSelectionListenerMenuItemOpenMethod() {
	      SelectionListener menuItemListenerOpenMethod = new SelectionListener() {
	         @Override
	         public void widgetSelected(SelectionEvent e) {
	        	 System.out.println("Should be opening class: " + selectedGClassOpenNode.getName() + ", in package: " + selectedGClassOpenNode.getParent());
	        	 
	        	 String projName = selectedGClassOpenNode.getParent().split("\\.")[0];
	        	 String packageName = selectedGClassOpenNode.getParent().split("\\.")[1];
	        	 String className = selectedGClassOpenNode.getName();
	        	 
	        	// Get the current page

	        	 IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        	 IWorkbenchPage page = window.getActivePage();
	
	        	 // Get IFile
	
	        	 IPath path = new Path("/" + projName + "/src/" + packageName + "/" + className + ".java");
	        	 IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	
	        	 // Open default editor for the file
	
	        	 try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         }

	         @Override
	         public void widgetDefaultSelected(SelectionEvent e) {
	         }
	      };
	      menuItemOpenMethod.addSelectionListener(menuItemListenerOpenMethod);
	   }
   
   private void addSelectionListenerMenuItemRefresh() {
      SelectionListener menuItemListenerRefresh = new SelectionListener() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            System.out.println("[DBG] MenuItem Refresh");
            syncZestViewAndJavaEditor();
         }

         @Override
         public void widgetDefaultSelected(SelectionEvent e) {
         }
      };
      menuItemRefresh.addSelectionListener(menuItemListenerRefresh);
   }

   private void resetSelectedSrcGraphNode() {
      if (selectedSrcGraphNode != null && selectedSrcGraphNode.isDisposed() == false) {
         selectedSrcGraphNode.setBorderWidth(0);
         selectedGMethodNode.setNodeType(GNodeType.InValid);
      }
   }

   public void syncZestViewAndJavaEditor() {
      ProjectAnalyzer analyzer = new ProjectAnalyzer();
      analyzer.analyze();
      gViewer.setInput(GModelProvider.instance().getNodes());
   }

   public void update() {
      gViewer.setInput(GModelProvider.instance().getNodes());
      if (layout % 2 == 0)
         gViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      else
         gViewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      layout++;
   }

   @Focus
   public void setFocus() {
      this.gViewer.getGraphControl().setFocus();
   }
}
