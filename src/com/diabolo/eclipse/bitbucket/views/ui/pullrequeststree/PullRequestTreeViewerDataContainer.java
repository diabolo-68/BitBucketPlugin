package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.core.runtime.IAdaptable;

public class PullRequestTreeViewerDataContainer implements IAdaptable {
	
	private String name;
	private PullRequestTreeViewerTreeParent parent;
	private Object data;

	public PullRequestTreeViewerDataContainer(String name) {
		this.name = name;
	}

	public PullRequestTreeViewerDataContainer(String name, Object data) {
		this.name = name;
		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	public String getName() {
		return name;
	}

	public void setParent(PullRequestTreeViewerTreeParent parent) {
		this.parent = parent;
	}

	public PullRequestTreeViewerTreeParent getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public <T> T getAdapter(Class<T> key) {
		return null;
	}
}
