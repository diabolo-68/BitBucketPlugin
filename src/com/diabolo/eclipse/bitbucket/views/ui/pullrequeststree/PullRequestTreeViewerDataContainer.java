package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IAdaptable;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable.PullRequestTableViewerDataContainer;

public class PullRequestTreeViewerDataContainer implements IAdaptable {
	
	private String name;
	private PullRequestTreeViewerTreeParent parent;
	private Object data;
	private ArrayList<PullRequestTableViewerDataContainer> tableLines;

	public PullRequestTreeViewerDataContainer(String name) {
		this.name = name;
	}

	/**
	 * @return the tableLines
	 */
	public ArrayList<PullRequestTableViewerDataContainer> getTableLines() {
		return tableLines;
	}

	public PullRequestTreeViewerDataContainer(String name, Object data) {

		this.name = name;
		this.data = data;
		tableLines = new ArrayList<>();
				
		if (this.data instanceof Value) {
			Value prValue = ((Value) this.data);

			tableLines.add(new PullRequestTableViewerDataContainer(Activator.ICON_AUTHOR, "Author", prValue.getAuthor().getUser().getDisplayName(), prValue.getAuthor().getUser().getLinks().getSelf().get(0).getHref(), tableLines.size() + 1));
			tableLines.add(new PullRequestTableViewerDataContainer(Activator.ICON_SOURCE,"Title", prValue.getTitle(), tableLines.size() + 1));
			tableLines.add(new PullRequestTableViewerDataContainer(Activator.ICON_BRANCHES,"Branch", prValue.getFromRef().getId(), tableLines.size() + 1));
			
			String state = prValue.getProperties().getMergeResult().getOutcome();
			
			String stateIcon;
			
			if (state.equalsIgnoreCase("clean")) {
				stateIcon = Activator.ICON_SYMBOLS;		   					
			} else {
				stateIcon = Activator.ICON_WARNINGS;		   							   					
			}
			
			tableLines.add(new PullRequestTableViewerDataContainer(stateIcon, "State", state, tableLines.size() + 1));		   							   					
			
			/*
			 * Get the pull-request's reviewers
			 * We don't increment the line to force the table to display
			 * all the data with the same background color
			 */
			
			AtomicInteger reviewerNumber = new AtomicInteger(0);
			 
			prValue.getReviewers().forEach(reviewer -> {
	
				final String reviewState;
				
				if (reviewer.getStatus().equalsIgnoreCase("unapproved")) {
					reviewState = Activator.ICON_PERSON_WITH_CROSS;
				} else {
					reviewState = Activator.ICON_PERSON_WITH_TICK;
				}
				
				tableLines.add(new PullRequestTableViewerDataContainer(reviewState,
																	   "Reviewer #" + reviewerNumber.incrementAndGet(),
																	   reviewer.getUser().getDisplayName() + " (" + reviewer.getStatus().toLowerCase() + ")",
																	   reviewer.getUser().getLinks().getSelf().get(0).getHref(),
																	   tableLines.size() + 1));
			});
		}
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
