package com.diabolo.eclipse.bitbucket.views;

import java.util.ArrayList;

public class PullRequestsTreeParent extends PullRequestsTreeObject {
	private ArrayList<PullRequestsTreeObject> children;

	public PullRequestsTreeParent(String name) {
		super(name);
		children = new ArrayList<PullRequestsTreeObject>();
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
