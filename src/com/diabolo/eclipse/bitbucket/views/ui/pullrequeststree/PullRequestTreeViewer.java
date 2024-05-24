package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTreeViewer extends org.eclipse.jface.viewers.TreeViewer{

	private Cursor waitCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
	
	public PullRequestTreeViewerClickAction PullRequestClickAction;
	
	public PullRequestTreeViewer() {
		super(null);
	}
	
	public PullRequestTreeViewer(Composite parent, int style) {
		super(parent, style);
		setExpandPreCheckFilters(true);
		setAutoExpandLevel(10);
		addSelectionChangedListener(((ISelectionChangedListener) new PullRequestTreeViewerSelectionChangedListener(this)));	
		hookContextMenu();
	}

	public void fill(PullRequestTreeViewerTreeParent treeParent) {
		
		if (Display.getCurrent().getActiveShell() != null) {
			Display.getCurrent().getActiveShell().setCursor(waitCursor);
		}
		
		if (Activator.getServices().projects != null && Activator.getServices().repositories != null) {
			
			setContentProvider(new PullRequestTreeViewerContentProvider(treeParent));
			setLabelProvider(new PullRequestTreeViewerLabelProvider());
			setInput(Activator.getPullRequestView().getViewSite());
			expandAll();					
		}

		if (Display.getCurrent().getActiveShell() != null) {
			Display.getCurrent().getActiveShell().setCursor(null);
		}
        
	}	
	/*
	 * Define a context-menu on the tree-view
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");

		menuMgr.setRemoveAllWhenShown(true);

		menuMgr.addMenuListener(new PullRequestTreeViewerMenuListener(this));

		Menu menu = menuMgr.createContextMenu(getControl());
		getControl().setMenu(menu);
		Activator.getPullRequestView().getSite().registerContextMenu(menuMgr, this);
	}
}
