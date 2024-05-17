package com.diabolo.eclipse.bitbucket.views;

import java.util.ArrayList;
import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.BitBucketServices;
import com.diabolo.eclipse.bitbucket.ValuePair;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

/*
 * A JFace view that displays all Pull-Requests extracted from BitBucket
 * using the BitBucket APIs.
 * When a pull-request is selected in the tree-view, some data are displayed
 * in a table.
 */
public class PullRequestsView extends ViewPart {
	
	public PullRequestsView() {
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.diabolo.eclipse.bitbucket.views.PullRequestsView";

	@Inject
	IWorkbench workbench;

	private ViewerPullRequests viewerPullRequests;
	private DrillDownAdapter drillDownAdapter;
	private Action collapseAllAction;
	private Action expandAllAction;
	private Action PullRequestClickAction;
	private Text txtFilter;
	private BitBucketServices services = new BitBucketServices();
	private Combo cboRepositories;
	private Combo cboProjects;
	private Combo cboFilterOn;
	private TableViewer tableViewer;
	private Button btnRefresh;
	private Composite composite;
	private Text descriptionText;
	private int currentCboProjectIndex;
	private int currentCboRepositoriesIndex;
	
	private Cursor waitCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(4, false));

		cboProjects = new Combo(parent, SWT.NONE);
		cboProjects.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboProjects.setText("Project");
	
		Label lblFilter = new Label(parent, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter:");

		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnRefresh = new Button(parent, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnRefresh.setText("Refresh");

		cboRepositories = new Combo(parent, SWT.NONE);
		cboRepositories.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		cboRepositories.setText("Repository");
		cboRepositories.setEnabled(false);
		
		Label lblFilterOn = new Label(parent, SWT.NONE);
		lblFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilterOn.setText("Filter on:");

		ComboViewer comboViewer = new ComboViewer(parent, SWT.NONE);
		cboFilterOn = comboViewer.getCombo();
		cboFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboFilterOn.setItems(new String[] { "Pull Request Title", "Source Branch", "Target Branch" });
		cboFilterOn.setToolTipText("Select on which element the filter must apply to");
		cboFilterOn.select(0);
		new Label(parent, SWT.NONE);

		Composite scPullRequestsViewer = new Composite(parent,SWT.BORDER | SWT.EMBEDDED);
		scPullRequestsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		scPullRequestsViewer.setBackground(Display.getCurrent().getSystemColor(SWT.BACKGROUND));

		scPullRequestsViewer.setLayout(new FillLayout(SWT.HORIZONTAL));

		viewerPullRequests = new ViewerPullRequests(scPullRequestsViewer,SWT.BORDER);
		
		drillDownAdapter = new DrillDownAdapter(viewerPullRequests);
		
		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewerPullRequests.getControl(), "com.diabolo.eclipse.bitbucket.viewer");
		
		composite = new Composite(scPullRequestsViewer, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		tableViewer = new TableViewer(composite, SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		descriptionText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData gd_descriptionText = new GridData(SWT.FILL , SWT.FILL, true, true, 1, 1);
		gd_descriptionText.widthHint = 279;
		descriptionText.setLayoutData(gd_descriptionText);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		TableViewerLabelProvider labelProvider = new TableViewerLabelProvider();
		labelProvider.setViewer(tableViewer);
		
		tableViewer.setLabelProvider(labelProvider);
		
		tableViewer.getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		defineListeners();
		createTableViewerColumns();
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		
		btnRefresh.notifyListeners(SWT.Selection, new Event());
		cboRepositories.notifyListeners(SWT.Selection, new Event());
	}

	
	private void FillViewerPullRequests() {
		
		Display.getCurrent().getActiveShell().setCursor(waitCursor);
        
		com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(currentCboProjectIndex)); 
		com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue = (com.diabolo.eclipse.bitbucket.api.Repositories.Value) cboRepositories.getData(cboRepositories.getItem(currentCboRepositoriesIndex));	
	
		PullRequestsTreeParent treeParent = new PullRequestsTreeParent("Pull Requests", currentProjectValue, currentRepositoryValue, services, txtFilter.getText(), cboFilterOn.getSelectionIndex());
		
		viewerPullRequests.setContentProvider(new ViewPullRequestsContentProvider(getViewSite(),treeParent));
		viewerPullRequests.setLabelProvider(new TreeViewerLabelProvider());
		viewerPullRequests.setInput(getViewSite());
		viewerPullRequests.expandAll();		
		
		Display.getCurrent().getActiveShell().setCursor(null);
        
	}
	
	/**
	 * Create the columns to be used.
	 */	
	private void createTableViewerColumns() {
		TableLayout layout = new TableLayout();
		tableViewer.getTable().setLayout(layout);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		
		String[] columns = new String[] { "Element", "Value" };
		for (String element : columns) {
			TableColumn col = new TableColumn(tableViewer.getTable(), SWT.NONE, 0);
			col.setText(element);
		}
	}
	
	
	/*
	 * Initialize view's default values by
	 * using the BitBucket APIs
	 */
	private void initializeData() {
		System.out.println("initializeData");
		services.Update();
		fillCboProjects();
		fillCboRepositories();
		FillViewerPullRequests();
	}

	
	private void fillCboProjects() {
		System.out.println("fillCboProjects");
		
        
		cboProjects.removeAll();
		cboProjects.add("All");
		
		services.projectsValues.forEach(projectValue -> {
			cboProjects.add(projectValue.getName());
			cboProjects.setData(projectValue.getName(), projectValue);
		});

		cboProjects.select(0);
		cboRepositories.select(0);
		
	}
	
	
	/*
	 * Get all repositories from BitBucket using the APIs
	 * and fill the cboRepositories.
	 * The first entry is always 'All'
	 */
	private void fillCboRepositories() {
System.out.println("fillCboRepositories");

		cboRepositories.removeAll();

		cboRepositories.add("All");

		com.diabolo.eclipse.bitbucket.api.Projects.Value projectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(cboProjects.getSelectionIndex())); 
		
		services.repositoriesValues.forEach(repository -> {

			int projectValueId = -1;
			if (projectValue != null) {
				projectValueId = projectValue.getId();
			}
			
			if (repository.getProject().getId() == projectValueId || cboProjects.getSelectionIndex() == 0) {
				if (repository.getProject().getType().compareTo("NORMAL") == 0) {
					String cboRepositoryValue = repository.getName();
					cboRepositories.add(cboRepositoryValue);
					cboRepositories.setData(cboRepositoryValue, repository);
				}
			}
		});			

		cboRepositories.update();
		cboRepositories.select(0);
		
	}

	
	/*
	 * Define a context-menu on the tree-view
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");

		menuMgr.setRemoveAllWhenShown(true);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				PullRequestsView.this.fillContextMenu(manager);
			}
		});

		Menu menu = menuMgr.createContextMenu(viewerPullRequests.getControl());
		viewerPullRequests.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewerPullRequests);
	}

	/*
	 * Add a toolbar in the view
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(collapseAllAction);
		manager.add(new Separator());
		manager.add(expandAllAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	/*
	 * Define all possible actions of the current view
	 */
	private void makeActions() {
		collapseAllAction = new Action() {
			public void run() {
				viewerPullRequests.collapseAll();
			}
		};

		collapseAllAction.setText("Collapse All");
		collapseAllAction.setToolTipText("Collapse Pull Requests List");

		collapseAllAction.setImageDescriptor(Activator.getImageDescriptor(Activator.ICON_COLLAPSEALL));

		expandAllAction = new Action() {
			public void run() {
				viewerPullRequests.expandAll();
			}
		};
		
		expandAllAction.setText("Expand All");
		expandAllAction.setToolTipText("Expand Pull Requests List");
		
		expandAllAction.setImageDescriptor(Activator.getImageDescriptor(Activator.ICON_EXPANDALL));
	
		/*
		 * Define an action on the tree-view.
		 * When a pull-request in the tree-view is selected,
		 * the corresponding data are displayed in the pull-request table.
		 */
		
		PullRequestClickAction = new Action() {
			private int reviewerNumber;
			private String image;
			public void run() {
			
				ArrayList<ValuePair> tableLines = new ArrayList<>();
   				tableViewer.setInput(null);
   				tableViewer.getTable().clearAll();
   				tableViewer.update(tableViewer.getTable().getItems(), null);
   				
				IStructuredSelection selection = viewerPullRequests.getStructuredSelection();
				
				PullRequestsTreeObject obj = (PullRequestsTreeObject) selection.getFirstElement();
				
				if (obj != null) {
					
					descriptionText.setText("");
					
					if (obj.getData() instanceof Value) {
	    				Value prValue = ((Value) obj.getData());

		   				tableLines.add(new ValuePair(Activator.ICON_AUTHOR, "Author", prValue.getAuthor().getUser().getDisplayName(), tableLines.size() + 1));
		   				tableLines.add(new ValuePair(Activator.ICON_SOURCE,"Title", prValue.getTitle(), tableLines.size() + 1));
		   				tableLines.add(new ValuePair(Activator.ICON_BRANCHES,"Branch", prValue.getFromRef().getId(), tableLines.size() + 1));
		   				
		   				String state = prValue.getProperties().getMergeResult().getOutcome();
		   				if (state.equalsIgnoreCase("clean")) {
		   					tableLines.add(new ValuePair(Activator.ICON_SYMBOLS,"State", state, tableLines.size() + 1));		   					
		   				} else {
		   					tableLines.add(new ValuePair(Activator.ICON_WARNINGS,"State", state, tableLines.size() + 1));		   							   					
		   				}
		   				//tableLines.add(new ValuePair(Activator.ICON_COMMENT,"Description", "\nTEST\n" + prValue.getDescription() + "\n" + "TEST", tableLines.size() + 1));
	   					
		   				if (prValue.getDescription() != null) {
		   					descriptionText.setText(prValue.getDescription());
		   				}
		   				
		   				/*
		   				 * Get the pull-request's reviewers
		   				 * We don't increment the line to force the table to display
		   				 * all the data with the same background color
		   				 */
		   				
		   				reviewerNumber = 1;
		   				 
		   				prValue.getReviewers().forEach(reviewer -> {
		   					
		   					if (reviewer.getStatus().equalsIgnoreCase("unapproved")) {
		   						image = Activator.ICON_PERSON_WITH_CROSS;
		   					} else {
		   						image = Activator.ICON_PERSON_WITH_TICK;
		   					}
		   					tableLines.add(new ValuePair(image, "Reviewer #" + reviewerNumber, reviewer.getUser().getDisplayName() + " (" + reviewer.getStatus().toLowerCase() + ")", tableLines.size() + 1)); 
		   					reviewerNumber++;
		   				});
		   				
		   				tableViewer.setInput(tableLines);
		   				/*
		   				 * Resize the columns in regards with the content
		   				 */				
		   				
		   				
		   				tableViewer.getTable().getColumn(0).pack();
		   				tableViewer.getTable().getColumn(1).setWidth( tableViewer.getTable().getClientArea().width - tableViewer.getTable().getColumn(0).getWidth());				
					
					}
					tableViewer.refresh();
					tableViewer.getTable().redraw();
				}
			} 
		};
	}

	/*
	 * Link the Actions to the tree-view
	 */
	private void defineListeners() {

		cboRepositories.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentCboRepositoriesIndex = cboRepositories.getSelectionIndex();
				FillViewerPullRequests();				
			}
		});

			
		viewerPullRequests.addSelectionChangedListener(new ISelectionChangedListener() {
		   public void selectionChanged(SelectionChangedEvent event) {
		       if(event.getSelection() instanceof IStructuredSelection) {
		    	   PullRequestClickAction.run();
		       }
		   }
		});

		btnRefresh.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				initializeData();
			}
		});

		cboProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// When the user select another project, the repository combo is automatically set to "All"
				cboRepositories.select(0);
				
				// First combo's entry is always "All"
				if (cboProjects.getSelectionIndex() == 0) {
					cboRepositories.setEnabled(false);
				} else {
					fillCboRepositories();
					cboRepositories.setEnabled(true);					
				}
				currentCboProjectIndex = cboProjects.getSelectionIndex();
				FillViewerPullRequests();
			}
		});
		
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

	}

	
	public void setFocus() {
		if (viewerPullRequests != null) {
			viewerPullRequests.getControl().setFocus();
		}
	}
}
