package viewer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class isViewPartDescriptor {
	// Use Part Descriptor's ID in fragment.e4xmi.
	public static final String VIEW_ID = "project0830-part-descriptor-carey.partdescriptor.viewpartdescriptor";
	// Use Popup Menu's ID in fragment.e4xmi.
	public final static String POPUPMENU_ID = "project0830-part-descriptor-carey.popupmenu.showhello";
	// Text box object.
	private StyledText styledText = null;

	@Inject
	public isViewPartDescriptor() {
	}

	// Called after a class is initialized with its constructor.
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		styledText = new StyledText(container, SWT.BORDER);
		menuService.registerContextMenu(styledText, POPUPMENU_ID);
	}

	// Pass an empty string parameter here to clear the text box.
	public void setText(String str) {
		this.styledText.setText(str);
	}

	public void appendText(String str) {
		this.styledText.append(str);
	}
}