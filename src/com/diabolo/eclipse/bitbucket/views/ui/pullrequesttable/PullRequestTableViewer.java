package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.ValuePair;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewer;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerDataContainer;

public class PullRequestTableViewer extends org.eclipse.jface.viewers.TableViewer{


	private int reviewerNumber;
	private String image;
	
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
		labelProvider.setViewer(this);
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
	
	public void fillTable(PullRequestTreeViewerDataContainer obj, Text descriptionText) {
		
		ArrayList<ValuePair> tableLines = new ArrayList<>();
		setInput(null);
		getTable().clearAll();
		update(getTable().getItems(), null);
			
		if (obj != null) {
			
			descriptionText.setText("");
			
			if (obj.getData() instanceof Value) {
				Value prValue = ((Value) obj.getData());

   				tableLines.add(new ValuePair(Activator.ICON_AUTHOR, "Author", prValue.getAuthor().getUser().getDisplayName(), tableLines.size() + 1));
   				tableLines.add(new ValuePair(Activator.ICON_SOURCE,"Title", prValue.getTitle(), tableLines.size() + 1));
   				tableLines.add(new ValuePair(Activator.ICON_BRANCHES,"Branch", prValue.getFromRef().getId(), tableLines.size() + 1));
   				
   				String state = prValue.getProperties().getMergeResult().getOutcome();
   				if (state.equalsIgnoreCase("clean")) {
   					tableLines.add(new ValuePair(Activator.ICON_SYMBOLS,"State", state, tableLines.size() + 1));		   					
   				} else {
   					tableLines.add(new ValuePair(Activator.ICON_WARNINGS,"State", state, tableLines.size() + 1));		   							   					
   				}
   				//tableLines.add(new ValuePair(Activator.ICON_COMMENT,"Description", "\nTEST\n" + prValue.getDescription() + "\n" + "TEST", tableLines.size() + 1));
					
   				if (prValue.getDescription() != null) {
   					descriptionText.setText(prValue.getDescription());
   				}
   				
   				/*
   				 * Get the pull-request's reviewers
   				 * We don't increment the line to force the table to display
   				 * all the data with the same background color
   				 */
   				
   				reviewerNumber = 1;
   				 
   				prValue.getReviewers().forEach(reviewer -> {
   					
   					if (reviewer.getStatus().equalsIgnoreCase("unapproved")) {
   						image = Activator.ICON_PERSON_WITH_CROSS;
   					} else {
   						image = Activator.ICON_PERSON_WITH_TICK;
   					}
   					tableLines.add(new ValuePair(image, "Reviewer #" + reviewerNumber, reviewer.getUser().getDisplayName() + " (" + reviewer.getStatus().toLowerCase() + ")", tableLines.size() + 1)); 
   					reviewerNumber++;
   				});
   				
   				setInput(tableLines);
   				/*
   				 * Resize the columns in regards with the content
   				 */				
   				
   				
   				getTable().getColumn(0).pack();
   				getTable().getColumn(1).setWidth( getTable().getClientArea().width - getTable().getColumn(0).getWidth());				
			
			}
			refresh();
			getTable().redraw();
		}
		
	}

}
