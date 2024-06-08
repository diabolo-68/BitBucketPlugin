package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import com.diabolo.eclipse.bitbucket.Activator;

public class PullRequestTreeViewer extends org.eclipse.jface.viewers.TreeViewer {

	public PullRequestTreeViewerClickAction PullRequestClickAction;

    public PullRequestTreeViewer() {
        super(null);
    }

    public PullRequestTreeViewer(Composite parent, int style) {
        super(parent, style);
        setExpandPreCheckFilters(true);
        setAutoExpandLevel(10);
		addSelectionChangedListener(((ISelectionChangedListener) new PullRequestTreeViewerSelectionChangedListener(this)));	
        addDoubleClickListener(new PullRequestTreeViewerDoubleClickListener());
        hookContextMenu();
       
    }
    
    public void fill(PullRequestTreeViewerTreeParent treeParent) {
        if (Activator.getServices().projects != null && Activator.getServices().repositories != null) {
            setContentProvider(new PullRequestTreeViewerContentProvider(treeParent));
            setLabelProvider(new PullRequestTreeViewerLabelProvider());
            setInput(Activator.getPullRequestView().getViewSite());
            expandAll();                    
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
