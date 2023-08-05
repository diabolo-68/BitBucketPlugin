package com.diabolo.eclipse.bitbucket.views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.diabolo.eclipse.bitbucket.MyTitleAreaDialog;
import com.diabolo.eclipse.bitbucket.Services;
import com.diabolo.eclipse.bitbucket.api.objects.Projects;
import com.diabolo.eclipse.bitbucket.api.objects.Pullrequests;
import com.diabolo.eclipse.bitbucket.api.objects.Repositories;

import org.eclipse.jface.viewers.ComboViewer;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class PullRequestsView extends ViewPart {
	public PullRequestsView() {
		Projects projects;
		try {
			projects = services.GetProjects();
			Repositories repositories = services.GetRepositories();

			projectsValues = projects.getValues();

			repositoriesValues = repositories.getValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.diabolo.eclipse.bitbucket.views.PullRequestsView";

	@Inject
	IWorkbench workbench;

	private TreeViewer viewerPullRequests;
	private DrillDownAdapter drillDownAdapter;
	private Action collapseAllAction;
	private Action expandAllAction;
	private Action PullRequestdoubleClickAction;
	private TreeParent invisibleRoot = null;
	private Text txtFilter;
	private List<com.diabolo.eclipse.bitbucket.api.objects.ProjectValue> projectsValues;
	private List<com.diabolo.eclipse.bitbucket.api.objects.RepositoryValue> repositoriesValues;
	private Services services = new Services();
	private Combo cboRepositories;
	private Combo cboProjects;
	private Combo cboFilterOn;

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		@Override
		public String toString() {
			return getName();
		}

		@Override
		public <T> T getAdapter(Class<T> key) {
			return null;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ViewContentProvider implements ITreeContentProvider {

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					try {
						initialize();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		/*
		 * We will set up a dummy model to initialize tree hierarchy. In a real code,
		 * you will connect to a real model and expose its hierarchy.
		 */
		private void initialize() throws Exception {
			TreeParent root = new TreeParent("");

			repositoriesValues.forEach(repository -> {

				Pullrequests pullRequests;
				List<com.diabolo.eclipse.bitbucket.api.objects.PullrequestValue> pullRequestValues;

				try {

					int idxProject = cboProjects.getSelectionIndex();
					int idxRepositories = cboRepositories.getSelectionIndex();

					String cboValue = repository.getProject().getName() + " / " + repository.getName();

					if (cboRepositories.getItem(idxRepositories).compareTo("All") == 0
							|| ((String) cboRepositories.getData(cboValue))
									.compareTo(repository.getId().toString()) == 0) {

						if (cboProjects.getItem(idxProject).compareTo("All") == 0
								|| cboProjects.getItem(idxProject).compareTo(repository.getProject().getName()) == 0) {

							pullRequests = services.GetPullRequestsForRepo(repository.getProject().getKey(),
									repository.getName());

							pullRequestValues = pullRequests.getValues();

							if (pullRequestValues.size() > 0) {

								TreeParent repositoryTree = new TreeParent(
										repository.getProject().getName() + " / " + repository.getName());

								pullRequestValues.forEach(prValue -> {
									if (!txtFilter.getText().isBlank()) {
										
										switch(cboFilterOn.getSelectionIndex()) {
										case 0:
											if (prValue.getTitle().contains(txtFilter.getText())) {
												TreeObject pullRequest = new TreeObject(
														String.format("%s - %s", prValue.getTitle(),
																prValue.getAuthor().getUser().getDisplayName()));
												repositoryTree.addChild(pullRequest);
											}
											break;
										case 1:
											if (prValue.getFromRef().getDisplayId().contains(txtFilter.getText())) {
												TreeObject pullRequest = new TreeObject(
														String.format("%s - %s", prValue.getTitle(),
																prValue.getAuthor().getUser().getDisplayName()));
												repositoryTree.addChild(pullRequest);
											}
											break;
										case 2:
											if (prValue.getToRef().getDisplayId().contains(txtFilter.getText())) {
												TreeObject pullRequest = new TreeObject(
														String.format("%s - %s", prValue.getTitle(),
																prValue.getAuthor().getUser().getDisplayName()));
												repositoryTree.addChild(pullRequest);
											}
											break;
										}
									} else {
										TreeObject pullRequest = new TreeObject(String.format("%s - %s",
												prValue.getTitle(), prValue.getAuthor().getUser().getDisplayName()));
										repositoryTree.addChild(pullRequest);
									}
								});

								root.addChild(repositoryTree);
							}
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			invisibleRoot = root;

		}
	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return workbench.getSharedImages().getImage(imageKey);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(4, false));

		// TODO: Check if PR can be merged, highlight the PR if conflict

		cboProjects = new Combo(parent, SWT.NONE);
		cboProjects.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				invisibleRoot = null;
				// TODO: Restart Initialize instead of re-defining the content provider
				fillCboRepositories(cboProjects.getItem(cboProjects.getSelectionIndex()).toString());

				// TODO: move Refresh Viewer to cbRepo update
				viewerPullRequests.setContentProvider(new ViewContentProvider());
			}
		});
		cboProjects.add("All");
		cboProjects.setText("Project");
		projectsValues.forEach(project -> {
			cboProjects.add(project.getName());
		});

		cboProjects.select(0);

		Label lblFilterOn = new Label(parent, SWT.NONE);
		lblFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilterOn.setText("Filter:");

		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnRefresh = new Button(parent, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

		btnRefresh.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				// TODO: move Refresh Viewer to cbRepo update
				invisibleRoot = null;
				viewerPullRequests.setContentProvider(new ViewContentProvider());

			}
		});

		btnRefresh.setText("Refresh list");

		cboRepositories = new Combo(parent, SWT.NONE);
		cboRepositories.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		cboRepositories.setText("Repository");

		fillCboRepositories(cboProjects.getItem(cboProjects.getSelectionIndex()).toString());
		
		Label lblFilterOn_2 = new Label(parent, SWT.NONE);
		lblFilterOn_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilterOn_2.setText("Filter on:");
		
		ComboViewer comboViewer = new ComboViewer(parent, SWT.NONE);
		cboFilterOn = comboViewer.getCombo();
		cboFilterOn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboFilterOn.setItems(new String[] {"Pull Request Title", "Source Branch", "Target Branch"});
		cboFilterOn.setToolTipText("Select on which element the filter must apply to");
		cboFilterOn.select(0);
		
		new Label(parent, SWT.NONE);

		ScrolledComposite scPullRequestsViewer = new ScrolledComposite(parent,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scPullRequestsViewer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 4, 1));
		scPullRequestsViewer.setExpandHorizontal(true);
		scPullRequestsViewer.setExpandVertical(true);
		viewerPullRequests = new TreeViewer(scPullRequestsViewer, SWT.NONE);
		viewerPullRequests.setExpandPreCheckFilters(true);
		viewerPullRequests.setAutoExpandLevel(10);

		Tree treePullRequests = viewerPullRequests.getTree();
		drillDownAdapter = new DrillDownAdapter(viewerPullRequests);

		viewerPullRequests.setContentProvider(new ViewContentProvider());
		viewerPullRequests.setInput(getViewSite());
		viewerPullRequests.setLabelProvider(new ViewLabelProvider());

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewerPullRequests.getControl(), "com.diabolo.eclipse.bitbucket.viewer");
		getSite().setSelectionProvider(viewerPullRequests);
		scPullRequestsViewer.setContent(treePullRequests);
		makeActions();
		hookContextMenu();
		hookPullRequestdoubleClickAction();
		contributeToActionBars();
	}

	private void fillCboRepositories(String project) {

		cboRepositories.removeAll();

		cboRepositories.add("All");

		repositoriesValues.forEach(repository -> {
			if (repository.getProject().getName().compareTo(project) == 0 || project == "All") {

				if (repository.getProject().getType().compareTo("NORMAL") == 0) {
					String cboValue = repository.getProject().getName() + " / " + repository.getName();
					cboRepositories.add(cboValue);
					cboRepositories.setData(cboValue, repository.getId());
				}
			}
		});

		cboRepositories.update();
		cboRepositories.select(0);

	}

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

	private void makeActions() {
		collapseAllAction = new Action() {
			public void run() {
				viewerPullRequests.collapseAll();
			}
		};
		collapseAllAction.setText("Collapse All");
		collapseAllAction.setToolTipText("Collapse Pull Requests List");
		collapseAllAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));

		expandAllAction = new Action() {
			public void run() {
				viewerPullRequests.expandAll();
			}
		};
		expandAllAction.setText("Expand All");
		expandAllAction.setToolTipText("Expand Pull Requests List");
		// expandAllAction.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_STOP));
		URL imageUrl;
		try {

			imageUrl = new URL("platform:/plugin/org.eclipse.xtext.ui/icons/elcl16/expandall.gif");
			expandAllAction.setImageDescriptor(ImageDescriptor.createFromURL(imageUrl));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PullRequestdoubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewerPullRequests.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookPullRequestdoubleClickAction() {
		viewerPullRequests.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				PullRequestdoubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MyTitleAreaDialog dialog = new MyTitleAreaDialog(viewerPullRequests.getControl().getShell());
		dialog.create();
		dialog.open();
		MessageDialog.openInformation(viewerPullRequests.getControl().getShell(), "Pull Requests", message);
	}

	@Override
	public void setFocus() {
		viewerPullRequests.getControl().setFocus();
	}
}
