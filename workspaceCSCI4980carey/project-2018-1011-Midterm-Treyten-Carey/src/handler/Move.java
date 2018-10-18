
package handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import model.MyPerson;
import model.MyPersonModelProvider;
import util.MsgUtil;
import view.SimpleTableView20181011MidtermTreytenCarey;

public class Move {
   @Inject
   private EPartService epartService;
   @Inject
   @Named(IServiceConstants.ACTIVE_SHELL)
   Shell shell;

   @Execute
   public void execute() throws CoreException {
      System.out.println("MoveHandler!!");
      MPart findPart = epartService.findPart(SimpleTableView20181011MidtermTreytenCarey.ID);
      Object findPartObj = findPart.getObject();
      
      if (findPartObj instanceof SimpleTableView20181011MidtermTreytenCarey) {
         SimpleTableView20181011MidtermTreytenCarey v = (SimpleTableView20181011MidtermTreytenCarey) findPartObj;
         
         ISelection selection = v.getViewer().getSelection();
         MyPerson person = null;
         if (selection instanceof IStructuredSelection) {
             Iterator iterator = ((IStructuredSelection) selection).iterator();
             while (iterator.hasNext()) {
                 Object obj = iterator.next();
                 MyPersonModelProvider.INSTANCE.persons.remove((MyPerson)obj);
                 MyPersonModelProvider.INSTANCE.persons.add(0, (MyPerson)obj);
             }
         }
         v.getViewer().setInput(MyPersonModelProvider.INSTANCE.getPersons());
         MsgUtil.openInformation("Moved the Selected Line at the First Line");
      }
   }
}