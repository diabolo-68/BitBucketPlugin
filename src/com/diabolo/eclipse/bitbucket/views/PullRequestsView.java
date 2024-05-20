package com.diabolo.eclipse.bitbucket.views;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewer;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerClickAction;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerTreeParent;

/*
 * A JFace view that displays all Pull-Requests extracted from BitBucket
 * using the BitBucket APIs.
 * When a pull-request is selected in the tree-view, some data are displayed
 * in a table.
 */
public class PullRequestsView extends ViewPart {
	public PullRequestsView() {
		Activator.setPullRequestView(this);
	}
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.diabolo.eclipse.bitbucket.views.PullRequestsView";

	/*
	 * public properties used by sub-classes 
	 */
	private PullRequestTreeViewerTreeParent treeParent;
	private PullRequestTreeViewer pullRequestsTreeViewer;
	private Text txtFilter;
	private RepositoriesCombo cboRepositories;
	private ProjectsCombo cboProjects;
	private Combo cboFilterOn;
	private com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable.PullRequestTableViewer tableViewer;
	private Composite composite;
	private Button btnApplyFilter;
	private Button btnUpdateData;
	private Text descriptionText;

	@Inject
	IWorkbench workbench;
	
	public void createPartControl(Composite parent) {
		
		parent.setLayout(new GridLayout(4, false));

		cboProjects = new ProjectsCombo(parent, this, SWT.NONE);
	
		Label lblFilter = new Label(parent, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter:");

		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		btnApplyFilter = new Button(parent, SWT.NONE);
		btnApplyFilter.setText("Apply Filter");
		
		cboRepositories = new RepositoriesCombo(parent, this, SWT.NONE);

		Label lblFilterOn = new Label(parent, SWT.NONE);
		lblFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilterOn.setText("Filter on:");
		
		ComboViewer comboViewer = new ComboViewer(parent, SWT.NONE);
		cboFilterOn = comboViewer.getCombo();
		cboFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboFilterOn.setItems(new String[] { "Pull Request Title", "Source Branch", "Target Branch" });
		cboFilterOn.setToolTipText("Select on which element the filter must apply to");
		cboFilterOn.select(0);
		
		btnUpdateData = new Button(parent, SWT.NONE);
		btnUpdateData.setText("Update Data");
		
		Composite scPullRequestsViewer = new Composite(parent,SWT.BORDER | SWT.EMBEDDED);
		scPullRequestsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		scPullRequestsViewer.setBackground(Display.getCurrent().getSystemColor(SWT.BACKGROUND));

		scPullRequestsViewer.setLayout(new FillLayout(SWT.HORIZONTAL));

		pullRequestsTreeViewer = new PullRequestTreeViewer(scPullRequestsViewer,this, SWT.BORDER);
		pullRequestsTreeViewer.setViewpart(this);

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(pullRequestsTreeViewer.getControl(), "com.diabolo.eclipse.bitbucket.viewer");
		
		composite = new Composite(scPullRequestsViewer, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		tableViewer = new com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable.PullRequestTableViewer(composite, SWT.FULL_SELECTION);
		
		descriptionText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData gd_descriptionText = new GridData(SWT.FILL , SWT.FILL, true, true, 1, 1);
		gd_descriptionText.widthHint = 279;
		descriptionText.setLayoutData(gd_descriptionText);
		

		defineListeners();
		pullRequestsTreeViewer.PullRequestClickAction = new PullRequestTreeViewerClickAction(tableViewer, pullRequestsTreeViewer, descriptionText);
		pullRequestsTreeViewer.PullRequestClickAction.run();
		
		initView();
	}

	
	private void initView() {
		btnUpdateData.notifyListeners(SWT.Selection, new Event());
	}
	
	/*
	 * Initialize view's default values by
	 * using the BitBucket APIs
	 */
	public void refreshView(boolean refreshData, boolean refreshProjectsList, boolean refreshRepositoryList) {
		System.out.println("refreshView");
		
		if (refreshData) {
			Activator.getServices().Update();
		}
		
		if (refreshProjectsList) {
			cboProjects.fillCboProjects();
			cboRepositories.select(0);
		}

		if (refreshRepositoryList) {
			cboRepositories.fill(cboProjects);
		}
		
		com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(cboProjects.getSelectionIndex())); 
		com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue = (com.diabolo.eclipse.bitbucket.api.Repositories.Value) cboRepositories.getData(cboRepositories.getItem(cboRepositories.getSelectionIndex()));	
		
		System.out.println("cboProjects.getItem(cboProjects.getSelectionIndex()) " + cboProjects.getItem(cboProjects.getSelectionIndex()));

		treeParent = new PullRequestTreeViewerTreeParent("Pull Requests", currentProjectValue, currentRepositoryValue, txtFilter.getText(), cboFilterOn.getSelectionIndex());			
		
		pullRequestsTreeViewer.fill(treeParent);
	}
	
	/*
	 * Link the Actions to the tree-view
	 */
	private void defineListeners() {

		btnApplyFilter.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				refreshView(false, false, false);
			}
		});

		
		btnUpdateData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshView(true, true, true);
			}
		});

	}
	
	public void setFocus() {

	}
}
