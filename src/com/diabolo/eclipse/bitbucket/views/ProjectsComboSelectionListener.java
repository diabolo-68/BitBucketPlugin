package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ProjectsComboSelectionListener extends SelectionAdapter{
	
	private PullRequestsView parent;
	
	public ProjectsComboSelectionListener(PullRequestsView parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		parent.refreshView(false, false, true);
	}	
}
