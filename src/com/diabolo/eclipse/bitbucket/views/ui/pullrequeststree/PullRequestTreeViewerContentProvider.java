package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IViewSite;

public class PullRequestTreeViewerContentProvider implements ITreeContentProvider {

	private IViewSite iViewSite;
	private PullRequestTreeViewerTreeParent invisibleRoot;
	
	public Object[] getElements(Object parent) {
		if (parent.equals(iViewSite)) {
			
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public PullRequestTreeViewerContentProvider(IViewSite iViewSite, PullRequestTreeViewerTreeParent invisibleRoot) {
		super();
		this.iViewSite = iViewSite;
		this.invisibleRoot = invisibleRoot;
	}

	public Object getParent(Object child) {
		if (child instanceof PullRequestTreeViewerDataContainer) {
			return ((PullRequestTreeViewerDataContainer) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof PullRequestTreeViewerTreeParent) {
			return ((PullRequestTreeViewerTreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof PullRequestTreeViewerTreeParent)
			return ((PullRequestTreeViewerTreeParent) parent).hasChildren();
		return false;
	}
}
