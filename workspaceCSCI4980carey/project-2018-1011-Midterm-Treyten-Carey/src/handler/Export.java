
package handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import util.MsgUtil;
import view.SimpleTableView20181011MidtermTreytenCarey;

public class Export {
   @Inject
   private EPartService epartService;
   @Inject
   @Named(IServiceConstants.ACTIVE_SHELL)
   Shell shell;

   @Execute
   public void execute() throws CoreException {
      System.out.println("ExportHandler!!");
      MPart findPart = epartService.findPart(SimpleTableView20181011MidtermTreytenCarey.ID);
      Object findPartObj = findPart.getObject();

      if (findPartObj instanceof SimpleTableView20181011MidtermTreytenCarey) {
         SimpleTableView20181011MidtermTreytenCarey v = (SimpleTableView20181011MidtermTreytenCarey) findPartObj;
         System.out.println(v.getViewer().getSelection().toString());
         String items = "";
         for (Object item : v.getViewer().getStructuredSelection().toList())
         {
        	 items += item;
         }
         
         String output = "C:\\Users\\Treyten\\workspaceCSCI4980\\workspaceCSCI4980carey\\project-2018-1011-Midterm-Treyten-Carey\\output-midterm-1011-Treyten-Carey.csv";
         BufferedWriter writer;
 	    try {
 	    	writer = new BufferedWriter(new FileWriter(output));
 			writer.write(items);
 			writer.close();
 			
 			MsgUtil.openInformation("Saved " + output);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
      }
   }
}