 
package handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import view.SimpleTableView20180927Q2TreytenCarey;;

public class Export {
	@Inject
   private ESelectionService selectionService;

   @Execute
   public void execute(EPartService epartService) {
      MPart findPart = epartService.findPart(SimpleTableView20180927Q2TreytenCarey.ID);
      Object findPartObj = findPart.getObject();

      if (findPartObj instanceof SimpleTableView20180927Q2TreytenCarey) {
    	  SimpleTableView20180927Q2TreytenCarey v = (SimpleTableView20180927Q2TreytenCarey) findPartObj;
         
    	  String str = "";
         for (int i = 0; i < v.getViewer().getTable().getItemCount(); i++)
         {
        	 str += v.getViewer().getElementAt(i) + "\n";
         }
         
         BufferedWriter writer;
 	    try {
 	    	writer = new BufferedWriter(new FileWriter("output-q2-0927-Treyten-Carey.csv"));
 			writer.write(str);
 			writer.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	    
 	   MessageDialog.openInformation(new Shell(), "Success", "Saved Your File Name");
      }
   }
		
}