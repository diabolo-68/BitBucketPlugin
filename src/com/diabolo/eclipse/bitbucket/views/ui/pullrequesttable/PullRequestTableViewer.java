package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class PullRequestTableViewer extends org.eclipse.jface.viewers.TableViewer{


	public PullRequestTableViewer(Composite parent, int style) {
		super(parent, style);
	
		setLayout();
		setListeners();
		createColumns();
	}
	
	private void setLayout() {
		Table table = this.getTable();
		table.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		getControl().setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));
		
	}

	private void setListeners() {
		setContentProvider(ArrayContentProvider.getInstance());
		PullRequestTableViewerLabelProvider labelProvider = new PullRequestTableViewerLabelProvider();
		setLabelProvider(labelProvider);
	}
	
	/**
	 * Create the columns to be used.
	 */	
	private void createColumns() {
		TableLayout layout = new TableLayout();
		getTable().setLayout(layout);
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);

		
		String[] columns = new String[] { "Element", "Value" };
		for (String element : columns) {
			TableColumn col = new TableColumn(getTable(), SWT.NONE, 0);
			col.setText(element);
		}
	}
	
	public void fillTable(ArrayList<PullRequestTableViewerDataContainer> tableLines) {
		
		setInput(null);
		getTable().clearAll();
		update(getTable().getItems(), null);
		setInput(tableLines);   				
		getTable().getColumn(0).pack();
		getTable().getColumn(1).setWidth( getTable().getClientArea().width - getTable().getColumn(0).getWidth());
		refresh();
		getTable().redraw();
	}

}
