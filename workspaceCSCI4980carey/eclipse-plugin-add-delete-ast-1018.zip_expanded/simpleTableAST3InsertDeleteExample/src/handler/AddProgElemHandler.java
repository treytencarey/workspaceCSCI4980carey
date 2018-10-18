
package handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import analysis.InsertProgElemAnalyzer;
import model.ModelProvider;
import view.AddProgElemDialog;
import view.MyTableViewer;

public class AddProgElemHandler {
   @Inject
   private EPartService epartService;
   @Inject
   @Named(IServiceConstants.ACTIVE_SHELL)
   Shell shell;

   @Execute
   public void execute() {
      System.out.println("AddProgElemHandler!!");

      AddProgElemDialog dialog = new AddProgElemDialog(shell);
      dialog.open();
      if (dialog.getProgElem() != null) {
         ModelProvider.INSTANCE.getProgramElements().add(dialog.getProgElem());
         MPart findPart = epartService.findPart(MyTableViewer.ID);
         Object findPartObj = findPart.getObject();
         if (findPartObj instanceof MyTableViewer) {
            MyTableViewer v = (MyTableViewer) findPartObj;
            InsertProgElemAnalyzer analyzer = new InsertProgElemAnalyzer(dialog.getProgElem());
            analyzer.insertNewProgElement();
            v.refresh();
         }
      }
   }
}