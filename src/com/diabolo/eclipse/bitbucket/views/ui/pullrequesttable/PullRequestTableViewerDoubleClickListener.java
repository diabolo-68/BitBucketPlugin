package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;

public class PullRequestTableViewerDoubleClickListener implements IDoubleClickListener {
	
	@Override
	public void doubleClick(DoubleClickEvent event) {
		
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        
        if (selection.getFirstElement() instanceof PullRequestTableViewerDataContainer) {
        
        	PullRequestTableViewerDataContainer firstElement = (PullRequestTableViewerDataContainer) selection.getFirstElement();
        	
        	if (firstElement != null) {
        		if (firstElement.getUrl() != null) {
        			Program.launch(firstElement.getUrl().toString());
        		}	
        	}
        }
	}
}
