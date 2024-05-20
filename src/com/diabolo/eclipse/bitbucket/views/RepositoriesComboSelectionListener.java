package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.diabolo.eclipse.bitbucket.Activator;

public class RepositoriesComboSelectionListener extends SelectionAdapter{
	
	public RepositoriesComboSelectionListener() {
		super();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Activator.getPullRequestView().refreshView(false, false, false);
	}
}
