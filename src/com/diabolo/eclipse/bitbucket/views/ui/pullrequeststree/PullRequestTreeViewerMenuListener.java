package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTreeViewerMenuListener implements IMenuListener {

	private Action collapseAllAction;
	private Action expandAllAction;
	private DrillDownAdapter drillDownAdapter;
	
	public PullRequestTreeViewerMenuListener(PullRequestTreeViewer parent) {
		super();
		drillDownAdapter = new DrillDownAdapter(parent);
		collapseAllAction = new PullRequestTreeViewerCollapseAllAction(parent);
		expandAllAction = new PullRequestTreeViewerExpandAllAction(parent);
		setActionBars();
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		fillContextMenu(manager);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(collapseAllAction);
		manager.add(new Separator());
		manager.add(expandAllAction);
	}


	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(collapseAllAction);
		manager.add(expandAllAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}
	
	/*
	 * Add a toolbar in the view
	 */
	private void setActionBars() {
		IActionBars bars = Activator.getPullRequestView().getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}	
}
