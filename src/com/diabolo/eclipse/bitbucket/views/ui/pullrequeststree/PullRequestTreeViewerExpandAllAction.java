package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Text;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTreeViewerExpandAllAction extends Action{

	private PullRequestTreeViewer viewer;
	
	public PullRequestTreeViewerExpandAllAction(PullRequestTreeViewer viewer) {
		super();
		
		this.viewer = viewer;
		
		setText("Expand All");
		setToolTipText("Expand Pull Requests List");
		
		setImageDescriptor(Activator.getImageDescriptor(Activator.ICON_EXPANDALL));
		
	}
	
	public void run() {
		viewer.expandAll();
	}
}
