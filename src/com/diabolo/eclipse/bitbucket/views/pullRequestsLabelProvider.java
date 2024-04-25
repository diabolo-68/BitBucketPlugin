package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class pullRequestsLabelProvider extends StyledCellLabelProvider {
    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        if (element instanceof PullRequestsTreeObject) {
        	PullRequestsTreeObject node = (PullRequestsTreeObject) element;
            // Check if the node has a particular value
        	// TODO: Add the condition that displays the node in RED 
            if (true) {
                // Set the background color of the text
                cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                cell.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            } else {
                // Default background color
                cell.setBackground(null);
            }
            // Set the text and styling
            StyledString styledString = new StyledString(node.getName());
            cell.setText(styledString.getString());
            cell.setStyleRanges(styledString.getStyleRanges());
        } else {
            super.update(cell);
        }
    }
}
