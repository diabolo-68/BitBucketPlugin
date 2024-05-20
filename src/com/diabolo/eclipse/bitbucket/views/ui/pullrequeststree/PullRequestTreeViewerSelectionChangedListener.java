package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class PullRequestTreeViewerSelectionChangedListener implements ISelectionChangedListener {

	private PullRequestTreeViewer parent;
	
	
	public PullRequestTreeViewerSelectionChangedListener(PullRequestTreeViewer parent) {
		super();
		this.parent = parent;
	}


	@Override
	   public void selectionChanged(SelectionChangedEvent event) {
	       if(event.getSelection() instanceof IStructuredSelection) {
	    	   System.out.println("TreeView Selection");
	    	   parent.PullRequestClickAction.run();
	       };
	}

}
