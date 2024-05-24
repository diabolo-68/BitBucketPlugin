package com.diabolo.eclipse.bitbucket.views;

import java.io.IOException;

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
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewer;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerClickAction;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerTreeParent;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable.PullRequestTableViewer;

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

	/*
	 * public properties used by sub-classes 
	 */
	private PullRequestTreeViewerTreeParent treeParent;
	private PullRequestTreeViewer pullRequestsTreeViewer;
	private PullRequestTableViewer tableViewer;
	
	private RepositoriesCombo cboRepositories;
	private ProjectsCombo cboProjects;
	private Combo cboFilterOn;
	
	private Button btnApplyFilter;
	private Button btnUpdateData;

	private Text descriptionText;
	private Text txtFilter;

	@Inject
	IWorkbench workbench;
	private Button btnSetDefault;
	
	public void createPartControl(Composite parent) {
		
		Activator.setPullRequestView(this);

		parent.setLayout(new GridLayout(5, false));

		cboProjects = new ProjectsCombo(parent, SWT.NONE);

	
		Label lblFilter = new Label(parent, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter:");
		
		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnApplyFilter = new Button(parent, SWT.NONE);
		btnApplyFilter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		btnApplyFilter.setText("Apply Filter");
		
		cboRepositories = new RepositoriesCombo(parent, SWT.NONE);
		cboRepositories.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		btnSetDefault = new Button(parent, SWT.NONE);
		btnSetDefault.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnSetDefault.setText("Set Default");
		
		
		Label lblFilterOn = new Label(parent, SWT.NONE);
		lblFilterOn.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblFilterOn.setText("Filter on:");

		ComboViewer comboViewer = new ComboViewer(parent, SWT.NONE);
		cboFilterOn = comboViewer.getCombo();
		cboFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		cboFilterOn.setItems(new String[] { "Pull Request Title", "Source Branch", "Target Branch", "Author", "Reviewer" });
		cboFilterOn.setToolTipText("Select on which element the filter must apply to");
		cboFilterOn.select(0);
		
		btnUpdateData = new Button(parent, SWT.NONE);
		btnUpdateData.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnUpdateData.setText("Update Data");
		
		Composite compositePanel = new Composite(parent,SWT.BORDER | SWT.EMBEDDED);
		GridData gd_compositePanel = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
		compositePanel.setLayoutData(gd_compositePanel);
		compositePanel.setBackground(Display.getCurrent().getSystemColor(SWT.BACKGROUND));
		compositePanel.setLayout(new FillLayout(SWT.HORIZONTAL));
		pullRequestsTreeViewer = new PullRequestTreeViewer(compositePanel, SWT.FULL_SELECTION);
		
		Composite detailPanelComposite = new Composite(compositePanel, SWT.NONE);
		detailPanelComposite.setLayout(new GridLayout(1, false));
		tableViewer = new PullRequestTableViewer(detailPanelComposite, SWT.FULL_SELECTION);
		
		descriptionText = new Text(detailPanelComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData gd_descriptionText = new GridData(SWT.FILL , SWT.FILL, true, true, 1, 1);
		descriptionText.setLayoutData(gd_descriptionText);
		pullRequestsTreeViewer.PullRequestClickAction = new PullRequestTreeViewerClickAction(tableViewer, pullRequestsTreeViewer, descriptionText);

		defineListeners();

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

		btnSetDefault.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeDefaults();
			}
		});

	}
	
	public void storeDefaults() {
		try {
			System.out.println("Store defaults");
			Activator.getStore().setValue(PreferenceConstants.P_DEFAULT_PROJECT, cboProjects.getSelectionIndex());
			Activator.getStore().setValue(PreferenceConstants.P_DEFAULT_REPOSITORY, cboRepositories.getSelectionIndex());
			Activator.getStore().save();
		} catch (IOException e) {
		}
		System.out.println("Stored Project default: " + Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_PROJECT));
		System.out.println("Stored Repo default: " + Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_REPOSITORY));

	}
	
	public void setFocus() {

	}
}
