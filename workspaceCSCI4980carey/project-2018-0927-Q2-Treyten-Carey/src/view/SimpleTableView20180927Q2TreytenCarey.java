package view;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import model.MyPerson;
import model.MyPersonModelProvider;
import model.filter.PersonFilter;
import model.sorter.PersonSorter;

public class SimpleTableView20180927Q2TreytenCarey {
   public final static String ID        = "project-2018-0927-q2-treyten-carey.partdescriptor.simpletableview20180927q2treytencarey";
   private TableViewer        viewer;
   private PersonFilter 	  filter;
   private Text         	  searchText;
   private PersonSorter       personSorter;

   public SimpleTableView20180927Q2TreytenCarey() {
   }

   /**
    * Create contents of the view part.
    */
   @PostConstruct
   public void createControls(Composite parent) {
      GridLayout layout = new GridLayout(2, false);
      parent.setLayout(layout);
      createSearchTextControl(parent);
      createTableViewerControl(parent);
      addKeyEventToSearchText();
   }

   private void createSearchTextControl(Composite parent) {
      new Label(parent, SWT.NONE).setText("Search: ");
      searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
      searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
   }
   
   private void addKeyEventToSearchText() {
      searchText.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent ke) {
            filter.setSearchText(searchText.getText());
            viewer.refresh();
         }
      });
   }

   private void createTableViewerControl(Composite parent) {
      viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
      createColumns(parent, viewer);
      final Table table = viewer.getTable();
      table.setHeaderVisible(true);
      table.setLinesVisible(true);

      viewer.setContentProvider(ArrayContentProvider.getInstance());
      // get the content for the viewer, setInput will call getElements in the contentProvider
      viewer.setInput(MyPersonModelProvider.INSTANCE.getPersons());
      // make the selection available to other views
      viewer.addSelectionChangedListener(new ISelectionChangedListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
            Object firstElement = selection.getFirstElement();
            System.out.println("[DBG] The selected first element: " + firstElement);
         }
      });

      GridData gridData = new GridData();
      gridData.verticalAlignment = GridData.FILL;
      gridData.horizontalSpan = 2;
      gridData.grabExcessHorizontalSpace = true;
      gridData.grabExcessVerticalSpace = true;
      gridData.horizontalAlignment = GridData.FILL;
      viewer.getControl().setLayoutData(gridData);

      filter = new PersonFilter();
      viewer.addFilter(filter);
      personSorter = new PersonSorter();
      viewer.setComparator(personSorter);
   }

   private void createColumns(final Composite parent, final TableViewer viewer) {
      String[] titles = { "First name", "Last name", "Phone", "Address" };
      int[] bounds = { 100, 100, 100, 100 };

      TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
      col.setLabelProvider(new ColumnLabelProvider() {
         @Override
         public String getText(Object element) {
            return ((MyPerson) element).getFirstName();
         }
      });
      col = createTableViewerColumn(titles[1], bounds[1], 1);
      col.setLabelProvider(new ColumnLabelProvider() {
         @Override
         public String getText(Object element) {
            return ((MyPerson) element).getLastName();
         }
      });
      
      col = createTableViewerColumn(titles[2], bounds[2], 2);
      col.setLabelProvider(new ColumnLabelProvider() {
         @Override
         public String getText(Object element) {
            return ((MyPerson) element).getPhone();
         }
      });

      col = createTableViewerColumn(titles[3], bounds[3], 3);
      col.setLabelProvider(new ColumnLabelProvider() {
         @Override
         public String getText(Object element) {
            return ((MyPerson) element).getAddress();
         }
      });
   }

   private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
      final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
      final TableColumn column = viewerColumn.getColumn();
      column.setText(title);
      column.setWidth(bound);
      column.setResizable(true);
      column.setMoveable(true);
      column.addSelectionListener(getSelectionAdapter(column, colNumber));
      return viewerColumn;
   }
   
   private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
      SelectionAdapter selectionAdapter = new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            personSorter.setColumn(index);
            int dir = personSorter.getDirection();
            viewer.getTable().setSortDirection(dir);
            viewer.getTable().setSortColumn(column);
            viewer.refresh();
         }
      };
      return selectionAdapter;
   }

   public TableViewer getViewer() {
      return viewer;
   }

   @PreDestroy
   public void dispose() {
   }

   @Focus
   public void setFocus() {
   }
}