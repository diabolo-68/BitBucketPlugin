package com.diabolo.eclipse.bitbucket.views;

import java.util.ArrayList;

class PullRequestsTreeParent extends PullRequestsTreeObject {
	private ArrayList children;

	public PullRequestsTreeParent(String name) {
		super(name);
		children = new ArrayList();
	}

	public void addChild(PullRequestsTreeObject child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(PullRequestsTreeObject child) {
		children.remove(child);
		child.setParent(null);
	}

	public PullRequestsTreeObject[] getChildren() {
		return (PullRequestsTreeObject[]) children.toArray(new PullRequestsTreeObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
}
