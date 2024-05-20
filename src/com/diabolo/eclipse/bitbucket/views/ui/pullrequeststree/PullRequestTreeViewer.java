package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.views.PullRequestsView;


public class PullRequestTreeViewer extends org.eclipse.jface.viewers.TreeViewer{
	/**
	 * @return 
	 * @wbp.parser.entryPoint
	 */
	
	public IViewPart viewpart; 
	private PullRequestsView view;
	private Cursor waitCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
	
	public PullRequestTreeViewerClickAction PullRequestClickAction;

	
	public PullRequestTreeViewer(Composite parent, PullRequestsView view, int style) {
		super(parent, style);
		this.view = view;
		
		setExpandPreCheckFilters(true);
		setAutoExpandLevel(10);
		addSelectionChangedListener(((ISelectionChangedListener) new PullRequestTreeViewerSelectionChangedListener(this)));	
	}


	/**
	 * @param viewpart the viewpart to set()
	 */
	public void setViewpart(IViewPart viewpart) {
		this.viewpart = viewpart;
		hookContextMenu();
	}

	public void fill(PullRequestTreeViewerTreeParent treeParent) {
		
		if (Display.getCurrent().getActiveShell() != null) {
			Display.getCurrent().getActiveShell().setCursor(waitCursor);
		}
		
System.out.println("FillViewerPullRequests");

		if (Activator.getServices().projects != null && Activator.getServices().repositories != null) {
			
			
			setContentProvider(new PullRequestTreeViewerContentProvider(view.getViewSite(),treeParent));
			setLabelProvider(new com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewerLabelProvider());
			setInput(view.getViewSite());
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
		viewpart.getSite().registerContextMenu(menuMgr, this);
	}
}
