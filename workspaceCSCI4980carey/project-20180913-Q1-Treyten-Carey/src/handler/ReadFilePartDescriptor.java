package handler;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import util.UtilFile;
import view.SimpleView20180913Q1TreytenCarey;

public class ReadFilePartDescriptor {

	@Inject
	private EPartService epartService;

	@Execute
	public void execute() {
		System.out.println("[DBG] ViewPopupHandler");
		MPart findPart = epartService.findPart(SimpleView20180913Q1TreytenCarey.VIEW_ID);
		Object findPartObj = findPart.getObject();

		if (findPartObj instanceof SimpleView20180913Q1TreytenCarey) {
			SimpleView20180913Q1TreytenCarey v = (SimpleView20180913Q1TreytenCarey) findPartObj;
			List<String> contents = UtilFile.readFile("C:/Users/Treyten/workspaceCSCI4980/workspaceCSCI4980carey/project-20180913-Q1-Treyten-Carey/config.txt");
			for (String str: contents)
			{
				if (str.split(": ").length > 0 && str.split(": ")[0].equals("Name"))
				{
					v.appendText(str.split(": ")[1]);
					break;
				}
			}
		}
	}

} 