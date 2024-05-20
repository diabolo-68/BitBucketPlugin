package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Text;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTreeViewerCollapseAllAction extends Action{

	private PullRequestTreeViewer viewer;
	
	public PullRequestTreeViewerCollapseAllAction(PullRequestTreeViewer viewer) {
		super();
		
		this.viewer = viewer;
		
		setText("Collapse All");
		setToolTipText("Collapse Pull Requests List");
		
		setImageDescriptor(Activator.getImageDescriptor(Activator.ICON_COLLAPSEALL));
		
	}
	
	public void run() {
		viewer.collapseAll();
	}
}
