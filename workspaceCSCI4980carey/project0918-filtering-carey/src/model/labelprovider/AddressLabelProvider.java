package model.labelprovider;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Text;

import model.Person;

public class AddressLabelProvider extends FirstNameLabelProvider {

	public AddressLabelProvider(Text searchText) {
		super(searchText);
	}

	@Override
	public void update(ViewerCell cell) {
		super.update(cell);
	}

	protected String getCellText(ViewerCell cell) {
		Person person = (Person) cell.getElement();
		String cellText = person.getAddress();
		return cellText;
	}
}