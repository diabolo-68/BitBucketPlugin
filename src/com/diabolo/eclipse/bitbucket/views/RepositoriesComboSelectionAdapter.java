package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RepositoriesComboSelectionAdapter extends SelectionAdapter{
	
	PullRequestsView parent;
	
	public RepositoriesComboSelectionAdapter(PullRequestsView parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		parent.refreshView(false, false, false);
	}
}
