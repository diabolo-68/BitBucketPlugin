package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTableViewerLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider {

    Display display = Display.getDefault();
 
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof PullRequestTableViewerDataContainer) {
            PullRequestTableViewerDataContainer pair = (PullRequestTableViewerDataContainer) element;
            if (columnIndex == 0) {
                return pair.getKey();
            } else if (columnIndex == 1) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public Image getColumnImage(Object element, int colmnIndex) {
        if (element instanceof PullRequestTableViewerDataContainer && colmnIndex == 0) {
            return Activator.getImage(((PullRequestTableViewerDataContainer) element).getImageKey());
        }
        return null;
    }

    @Override
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof PullRequestTableViewerDataContainer) {
            if (((PullRequestTableViewerDataContainer) element).getIndex() % 2 == 0) {
                return display.getSystemColor(SWT.COLOR_DARK_GRAY);
            }
        }
        return null;
    }

    @Override
    public Color getForeground(Object element, int columnIndex) {
        if (element instanceof PullRequestTableViewerDataContainer) {
            if (((PullRequestTableViewerDataContainer) element).getIndex() % 2 == 0) {
                return display.getSystemColor(SWT.COLOR_WHITE);
            }
        }
        return null;
    }

    @Override
    public Font getFont(Object element, int columnIndex) {
        if (columnIndex == 0) {
            return display.getSystemFont(); // You can set custom font here if needed
        }
        return null;
    }


	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}
 }
