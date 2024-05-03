package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.core.runtime.IAdaptable;

class PullRequestsTreeObject implements IAdaptable {
	
	private String name;
	private PullRequestsTreeParent parent;
	private Object data;

	public PullRequestsTreeObject(String name) {
		this.name = name;
	}

	public PullRequestsTreeObject(String name, Object data) {
		this.name = name;
		this.data = data;
	}

	public Object getData() {
		return this.data;
	}

	public String getName() {
		return name;
	}

	public void setParent(PullRequestsTreeParent parent) {
		this.parent = parent;
	}

	public PullRequestsTreeParent getParent() {
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
