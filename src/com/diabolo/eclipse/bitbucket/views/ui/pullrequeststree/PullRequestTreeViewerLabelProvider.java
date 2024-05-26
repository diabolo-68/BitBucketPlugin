package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

public class PullRequestTreeViewerLabelProvider extends StyledCellLabelProvider {
    
    Display display = Display.getDefault();

	@Override
    public void update(ViewerCell cell) {

        Object element = cell.getElement();
        StyledString styledString = new StyledString();
        
        if (element instanceof PullRequestTreeViewerTreeParent) {
        	
        	PullRequestTreeViewerTreeParent parent = (PullRequestTreeViewerTreeParent) element;
            styledString.append(parent.getName());
			cell.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			
			
        	cell.setImage(Activator.getImage(Activator.ICON_PROJECT));
        	
        	
        } else if (element instanceof PullRequestTreeViewerDataContainer) {
        	/*
        	 * Get the pull-request's data stored in the node's object
        	 */
        	PullRequestTreeViewerDataContainer node = (PullRequestTreeViewerDataContainer) element;
        	styledString.append(node.getName());

        	Value prValue = ((Value) node.getData());
        	
        	if (prValue != null) {
        		String pullRequestState = prValue.getProperties().getMergeResult().getOutcome();
        		
        		/*
        		 * If the pull-request's status is not clean, add a red icon
        		 */
        		if (!pullRequestState.contentEquals("CLEAN")) {
        			
    				cell.setImage(Activator.getImage(Activator.ICON_ERRORSTATE));        			
        		}
        	}
		}
        
        cell.setText(styledString.toString());
    }  
		
}
