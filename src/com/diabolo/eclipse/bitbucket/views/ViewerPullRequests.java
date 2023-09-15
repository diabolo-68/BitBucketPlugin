package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class ViewerPullRequests extends TreeViewer{
	/**
	 * @return 
	 * @wbp.parser.entryPoint
	 */
	public ViewerPullRequests(Composite parent, int style) {
		super(parent, style);
		setExpandPreCheckFilters(true);
		setAutoExpandLevel(10);
		// Decorate the TreeView nodes
		setLabelProvider(new ViewLabelProvider());
	}

	public ViewerPullRequests(Composite parent) {
		super(parent);
	}

	public ViewerPullRequests(Tree tree) {
		super(tree);
	}

}
