package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IViewSite;

public class ViewPullRequestsContentProvider implements ITreeContentProvider {

	private IViewSite iViewSite;
	private PullRequestsTreeParent invisibleRoot;
	
	public Object[] getElements(Object parent) {
		if (parent.equals(iViewSite)) {
			
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public ViewPullRequestsContentProvider(IViewSite iViewSite, PullRequestsTreeParent invisibleRoot) {
		super();
		this.iViewSite = iViewSite;
		this.invisibleRoot = invisibleRoot;
	}

	public Object getParent(Object child) {
		if (child instanceof PullRequestsTreeObject) {
			return ((PullRequestsTreeObject) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof PullRequestsTreeParent) {
			return ((PullRequestsTreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof PullRequestsTreeParent)
			return ((PullRequestsTreeParent) parent).hasChildren();
		return false;
	}
}
