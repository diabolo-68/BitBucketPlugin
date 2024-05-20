package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.ValuePair;

public class PullRequestTableViewerLabelProvider extends OwnerDrawLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider {

    Display display = Display.getDefault();
	private TableViewer viewer;
	
	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

 
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof ValuePair) {
            ValuePair pair = (ValuePair) element;
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
        if (element instanceof ValuePair && colmnIndex == 0) {
            return Activator.getImage(((ValuePair) element).getImageKey());
        }
        return null;
    }

    @Override
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof ValuePair) {
            if (((ValuePair) element).getIndex() % 2 == 0) {
                return display.getSystemColor(SWT.COLOR_DARK_GRAY);
            }
        }
        return null;
    }

    @Override
    public Color getForeground(Object element, int columnIndex) {
        if (element instanceof ValuePair) {
            if (((ValuePair) element).getIndex() % 2 == 0) {
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
	protected void measure(Event event, Object element) {
		if (element instanceof ValuePair) {
			System.out.println("1");
			if (event.index == 1) {
				event.width = viewer.getTable().getColumn(event.index).getWidth();
				System.out.println("2 " + event.width);
				if (event.width == 0)
					return;
				ValuePair line = (ValuePair) element;
				Point size = event.gc.textExtent(line.getValue());
				int lines = size.x / event.width + 1;
				event.height = size.y * lines;
				System.out.println("3 " + event.height);
			}
		}
	}

	
	@Override
	protected void paint(Event event, Object element) {
		if (element instanceof ValuePair) {
			System.out.println("4");
			if (event.index == 1) {
				System.out.println("5");
				ValuePair entry = (ValuePair) element;
				event.gc.drawText(entry.getValue(), event.x, event.y, true);			
			}
		}
	}
    
    @Override
    public void dispose() {
    }

	@Override
	public void addListener(ILabelProviderListener arg0) {
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
