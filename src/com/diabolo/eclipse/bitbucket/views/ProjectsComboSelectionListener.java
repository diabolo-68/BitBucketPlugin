package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.diabolo.eclipse.bitbucket.Activator;

public class ProjectsComboSelectionListener extends SelectionAdapter{
	
	public ProjectsComboSelectionListener() {
		super();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Activator.getPullRequestView().refreshView(false, false, true);
	}	
}
