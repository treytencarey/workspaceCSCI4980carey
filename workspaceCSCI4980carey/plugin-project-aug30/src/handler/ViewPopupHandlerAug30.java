package handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import viewer.SimpleViewAug30;

public class ViewPopupHandlerAug30 {
	@Inject
	private EPartService epartService;

	@Execute
	public void execute() {
		System.out.println("[DBG] ViewPopupHandler");
		MPart findPart = epartService.findPart(SimpleViewAug30.VIEW_ID);
		Object findPartObj = findPart.getObject();

		if (findPartObj instanceof SimpleViewAug30) {
			SimpleViewAug30 v = (SimpleViewAug30) findPartObj;
			v.appendText("Hello World!");
		}
	}
}