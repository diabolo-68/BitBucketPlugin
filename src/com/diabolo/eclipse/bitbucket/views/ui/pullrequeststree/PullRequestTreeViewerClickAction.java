package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Text;

import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable.PullRequestTableViewer;

public class PullRequestTreeViewerClickAction extends Action{

	private PullRequestTableViewer tableViewer;
	private PullRequestTreeViewer treeViewer;
	private Text descriptionText;
	
	
	public PullRequestTreeViewerClickAction(PullRequestTableViewer tableViewer, PullRequestTreeViewer treeViewer, Text descriptionText) {
		super();
		this.tableViewer = tableViewer;
		this.treeViewer = treeViewer;
		this.descriptionText = descriptionText;
	}

	public void run() {
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		
		PullRequestTreeViewerDataContainer prValueData = (PullRequestTreeViewerDataContainer) selection.getFirstElement();
		
		descriptionText.setText("");
		
		if (prValueData != null) {
			tableViewer.fillTable(prValueData.getTableLines());
			
			if (prValueData.getData() instanceof Value) {
				Value prValue = ((Value) prValueData.getData());
			
				if (prValue.getDescription() != null) {
					descriptionText.setText(prValue.getDescription());
				}
			}			
		}
	}
}
