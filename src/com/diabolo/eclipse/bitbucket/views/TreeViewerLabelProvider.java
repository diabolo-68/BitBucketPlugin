package com.diabolo.eclipse.bitbucket.views;

import java.net.URL;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

public class TreeViewerLabelProvider extends StyledCellLabelProvider {
    
    Display display = Display.getDefault();

	@Override
    public void update(ViewerCell cell) {

        Object element = cell.getElement();
        StyledString styledString = new StyledString();
        URL imageUrl;
        
        if (element instanceof PullRequestsTreeParent) {
        	
        	PullRequestsTreeParent parent = (PullRequestsTreeParent) element;
            styledString.append(parent.getName());
			cell.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			
			
        	cell.setImage(Activator.getImage(Activator.ICON_PROJECT));
        	
        	
        } else if (element instanceof PullRequestsTreeObject) {
        	/*
        	 * Get the pull-request's data stored in the node's object
        	 */
        	PullRequestsTreeObject node = (PullRequestsTreeObject) element;
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
