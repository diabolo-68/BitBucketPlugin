package com.diabolo.eclipse.bitbucket.views;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.diabolo.eclipse.bitbucket.Services;
import com.diabolo.eclipse.bitbucket.valuePair;
import com.diabolo.eclipse.bitbucket.api.Projects.Projects;
import com.diabolo.eclipse.bitbucket.api.Repositories.Repositories;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.PullRequestForRepository;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

public class PullRequestsView extends ViewPart {

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
	private Action PullRequestClickAction;
	private Text txtFilter;
	private List<com.diabolo.eclipse.bitbucket.api.Projects.Value> projectsValues;
	private List<com.diabolo.eclipse.bitbucket.api.Repositories.Value> repositoriesValues;
	private Services services = new Services();
	private Combo cboRepositories;
	private Combo cboProjects;
	private Combo cboFilterOn;
	private TableViewer tableViewer;
	private PullRequestsTreeParent invisibleRoot = new PullRequestsTreeParent("Pull Request is empty");
	private Button btnRefresh;
	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(4, false));

		cboProjects = new Combo(parent, SWT.NONE);
		cboProjects.add("All");
		cboProjects.setText("Project");
		cboProjects.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		cboProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				invisibleRoot = null;
				initializeTreeView();
				
				// When the user select another project, the repository combo is automatically set to "All*
				cboRepositories.select(0);
				
				// First combo's entry is always "All"
				if (cboProjects.getSelectionIndex() == 0) {
					cboRepositories.setEnabled(false);
				} else {
					fillCboRepositories();
					cboRepositories.setEnabled(true);					
				}
				
				btnRefresh.notifyListeners(SWT.Selection, new Event());
				
			}
		});
	
	
		Label lblFilter = new Label(parent, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter:");

		txtFilter = new Text(parent, SWT.BORDER);
		txtFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnRefresh = new Button(parent, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

		btnRefresh.setText("Refresh");

		cboRepositories = new Combo(parent, SWT.NONE);
		cboRepositories.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		cboRepositories.setText("Repository");
		cboRepositories.setEnabled(false);

		cboRepositories.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btnRefresh.notifyListeners(SWT.Selection, new Event());				
			}
		});
		
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

		scPullRequestsViewer.setLayout(new FillLayout(SWT.HORIZONTAL));
		scPullRequestsViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		viewerPullRequests = new ViewerPullRequests(scPullRequestsViewer,SWT.NONE);
		
		drillDownAdapter = new DrillDownAdapter(viewerPullRequests);
		
		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewerPullRequests.getControl(), "com.diabolo.eclipse.bitbucket.viewer");
		getSite().setSelectionProvider(viewerPullRequests);

		tableViewer = new TableViewer(scPullRequestsViewer);

		// Define the layout of the table*


		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(20, true));
		tlayout.addColumnData(new ColumnWeightData(80, 400, false));

		tableViewer.setContentProvider(new ArrayContentProvider());

		tableViewer.getTable().setLayout(tlayout);

		// For very element to display, we will select what to display
		// in function of the column index
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// nothing
			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			@Override
			public void dispose() {
				// nothing
			}

			@Override
			public void addListener(ILabelProviderListener arg0) {
				// nothing
			}

			@Override
			public String getColumnText(Object element, int colmnIndex) {

				String result = null;
				switch (colmnIndex) {
				case 0:
					result = ((valuePair) element).getKey();
					break;

				case 1:
					result = ((valuePair) element).getValue();
					break;
				}
				return result;
			}

			@Override
			public Image getColumnImage(Object element, int colmnIndex) {
				return null;
			}
		});

		String[] COLUMNS = new String[] { "Element", "Value" };

		for (String element : COLUMNS) {
			TableColumn col = new TableColumn(tableViewer.getTable(), SWT.FILL);
			col.setText(element);
		}

		btnRefresh.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				try {
					
					invisibleRoot = null;
					if (cboProjects.getItemCount() <= 1) {
						initializeData();
					}
					
					initializeTreeView();
					viewerPullRequests.setContentProvider(new ViewPullRequestsContentProvider(getViewSite(), invisibleRoot));
					viewerPullRequests.expandAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		initializeData();
		makeActions();
		hookContextMenu();
		hookPullRequestAction();
		contributeToActionBars();
	}

	private void initializeData() {
		Projects projects = services.GetProjects();
		
		Repositories repositories = services.GetRepositories();

		if (projects != null && repositories != null) {
			repositoriesValues = repositories.getValues();

			projectsValues = projects.getValues();					

			// TODO: Check if PR can be merged, highlight the PR if conflict

			projectsValues.forEach(projectValue -> {
				cboProjects.add(projectValue.getName());
				cboProjects.setData(projectValue.getName(), projectValue);
			});

			cboProjects.select(0);

			fillCboRepositories();

			initializeTreeView();
			
			viewerPullRequests.setContentProvider(new ViewPullRequestsContentProvider(getViewSite(), invisibleRoot));
			viewerPullRequests.setInput(getViewSite());


		} else {
			showMessage("BitBucket repositories and/or projects not found.\nCheck you settings and your network connectivity.");
		}
		
	}

	/*
	 * We will set up a dummy model to initialize tree hierarchy. In a real code,
	 * you will connect to a real model and expose its hierarchy.
	 */
	private void initializeTreeView() {
		invisibleRoot = null;
		
		PullRequestsTreeParent root = new PullRequestsTreeParent("");

		int idxProject = cboProjects.getSelectionIndex();
		int idxRepositories = cboRepositories.getSelectionIndex();

		com.diabolo.eclipse.bitbucket.api.Repositories.Value repositoryValue = new com.diabolo.eclipse.bitbucket.api.Repositories.Value();
		
		if (cboRepositories.getData(cboRepositories.getItem(idxRepositories).toString()) instanceof com.diabolo.eclipse.bitbucket.api.Repositories.Value) {
			repositoryValue = (com.diabolo.eclipse.bitbucket.api.Repositories.Value) cboRepositories.getData(cboRepositories.getItem(idxRepositories).toString()); 
		} 

		com.diabolo.eclipse.bitbucket.api.Projects.Value projectValue = new com.diabolo.eclipse.bitbucket.api.Projects.Value();
		
		if (cboProjects.getData(cboProjects.getItem(idxProject).toString()) instanceof com.diabolo.eclipse.bitbucket.api.Projects.Value) {
			projectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(idxProject).toString()); 
		} 

		final com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue = projectValue;
		final com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue = repositoryValue;
		
		if (repositoriesValues.size() > 0) {
			repositoriesValues.forEach(repository -> {
				PullRequestForRepository pullRequests;
				
				
				List<com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value> pullRequestValues;

				if (cboProjects.getItemCount() > 0 && cboRepositories.getItemCount() > 0) {
					String repositoryTreeValue = repository.getName();
					
					if (idxRepositories == 0 || currentRepositoryValue.getId() == repository.getId()) {
						
						if (idxProject == 0	|| repository.getProject().getId().compareTo(currentProjectValue.getId()) == 0) {
							pullRequests = services.GetPullRequestsForRepo(repository.getProject().getKey(), repository.getName());
							
							if (pullRequests != null) {
								pullRequestValues = pullRequests.getValues();
	
								if (pullRequestValues.size() > 0) {
									
									PullRequestsTreeParent repositoryTree = new PullRequestsTreeParent(repositoryTreeValue);

									pullRequestValues.forEach(prValue -> {

										String treeName = String.format("%s - %s", prValue.getTitle(),
												prValue.getAuthor().getUser().getDisplayName());
									
										if (!txtFilter.getText().isBlank()) {

											switch (cboFilterOn.getSelectionIndex()) {
											case 0:
												// Filter on Pull Request's title
												if (prValue.getTitle().toLowerCase().contains(txtFilter.getText().toLowerCase())) {
													PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											case 1:
												// Filter on Pull Request's source branch name
												if (prValue.getFromRef().getDisplayId().toLowerCase().contains(txtFilter.getText().toLowerCase())) {
													PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											case 2:
												// Filter on Pull Request's target branch name
												if (prValue.getToRef().getDisplayId().toLowerCase().contains(txtFilter.getText().toLowerCase())) {
													PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											}
										} else {
											PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
											repositoryTree.addChild(pullRequest);
										}
										
									});

									root.addChild(repositoryTree);
								}
							}
						}
					}
				}
			});

			invisibleRoot = root;
		} else {
			showMessage("No BitBucket repository found. Nothing to display\n. Check your settings and your network connectivity.");
		}

	}

	
	private void fillCboRepositories() {

		cboRepositories.removeAll();

		cboRepositories.add("All");

		com.diabolo.eclipse.bitbucket.api.Projects.Value projectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(cboProjects.getSelectionIndex())); 
		
		if (repositoriesValues.size() > 1) {
			repositoriesValues.forEach(repository -> {

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
		}

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
		URL imageUrl;
		
		try {
			imageUrl = new URL("platform:/plugin/org.eclipse.xtext.ui/icons/elcl16/expandall.gif");
			expandAllAction.setImageDescriptor(ImageDescriptor.createFromURL(imageUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
		PullRequestClickAction = new Action() {
			public void run() {
				
				IStructuredSelection selection = viewerPullRequests.getStructuredSelection();
				PullRequestsTreeObject obj = (PullRequestsTreeObject) selection.getFirstElement();

				valuePair[] currentLine = new valuePair[20];

				AtomicInteger reviewerCounter = new AtomicInteger();
				
				reviewerCounter.set(5);

				/*
				 * Delete the current table content	
				 */

				for (int i = 0; i < 20; i++) {
					currentLine[i] = new valuePair("","");
				}

				tableViewer.setInput(currentLine);
			    	
				if (obj != null) {
		    		   if (obj.getData() instanceof Value) {
		    			   Value prValue = ((Value) obj.getData());
		    			   
		    			   String pullRequestState = prValue.getProperties().getMergeResult().getOutcome();
		    			   currentLine[0] = new valuePair("Author", prValue.getAuthor().getUser().getDisplayName());
		    			   currentLine[1] = new valuePair("Title", prValue.getTitle());
		    			   currentLine[2] = new valuePair("Branch", prValue.getFromRef().getId());
		    			   currentLine[3] = new valuePair("State", pullRequestState);
		    			   currentLine[4] = new valuePair("Description", prValue.getDescription());

		    			   prValue.getReviewers().forEach(reviewer -> {
		    				   
		    				   if (reviewerCounter.get() < 20) {
		    					   String reviewerStatus = reviewer.getStatus().toString();
		    					   
		    					   if (!reviewerStatus.equalsIgnoreCase("UNAPPROVED")) {
		    						   currentLine[reviewerCounter.get()] = new valuePair(reviewer.getUser().getDisplayName(), reviewerStatus);
		    						   reviewerCounter.getAndIncrement();
		    					   }
		    				   }
		    				   
		    			   });
		    			   
		    			   tableViewer.setInput(currentLine);
		    			   
		    			   /*
		    			    * Resize the columns in regards with the content
		    			    */
		    			   for (int i = 0, n = tableViewer.getTable().getColumnCount(); i < n; i++) {
		    				   tableViewer.getTable().getColumn(i).pack();
		    			   }
		    			   
		    			   
		    			   for (int i = 0, n = tableViewer.getTable().getItemCount(); i < n; i++) {
		    				   tableViewer.getTable().getItem(i).setBackground(0,Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		    				   tableViewer.getTable().getItem(i).setBackground(1,Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));	            		
		    				   
		    				   if ((i == 3 && !pullRequestState.contentEquals("CLEAN")) || i == 5) {
		    					   tableViewer.getTable().getItem(i).setForeground(0,Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		    					   tableViewer.getTable().getItem(i).setForeground(1,Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));	            		
		    				   } else {
		    					   tableViewer.getTable().getItem(i).setForeground(0,Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		    					   tableViewer.getTable().getItem(i).setForeground(1,Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));	            		
		    				   }
		    			   }
		    		   }
				}
	            
	            tableViewer.refresh();
			}
		};
	}

	private void hookPullRequestAction() {
		if (viewerPullRequests != null) {

			viewerPullRequests.addSelectionChangedListener(new ISelectionChangedListener() {
			   public void selectionChanged(SelectionChangedEvent event) {
			       if(event.getSelection() instanceof IStructuredSelection) {
			    	   IStructuredSelection selection = viewerPullRequests.getStructuredSelection();						
			    	   PullRequestClickAction.run();
			       }
			   }
			});
		}
	}

	private void showMessage(String message) {
		/*
		 * MyTitleAreaDialog dialog = new
		 * MyTitleAreaDialog(viewerPullRequests.getControl().getShell());
		 * dialog.create(); dialog.open();
		 */
		MessageDialog.openInformation(viewerPullRequests.getControl().getShell(), "Pull Requests", message);
	}

	@Override
	public void setFocus() {
		if (viewerPullRequests != null) {
			viewerPullRequests.getControl().setFocus();
		}
	}

}
