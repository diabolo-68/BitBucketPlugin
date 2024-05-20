package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import java.util.ArrayList;
import java.util.List;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.PullRequestForRepository;

public class PullRequestTreeViewerTreeParent extends PullRequestTreeViewerDataContainer {
	
	private ArrayList<PullRequestTreeViewerDataContainer> children = new ArrayList<PullRequestTreeViewerDataContainer>();

	public PullRequestTreeViewerTreeParent(String name) {
		super(name);
	}
	
	public PullRequestTreeViewerTreeParent (String name, com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue, com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue, String textFilter, int indexFilter) {
		
		super(name);
		
		final String lowerTextFilter = textFilter.toLowerCase();
		
		/*
		 * Parse all repositories, 
		 * parse all projects of repositories,
		 * get all open pull-requests of projects
		 * 
		 */
		if (Activator.getServices().repositoriesValues != null) {
			
			if (Activator.getServices().repositoriesValues.size() > 0) {
				
				Activator.getServices().repositoriesValues.forEach(repository -> {
					
					PullRequestForRepository pullRequests;
					
					List<com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value> pullRequestValues;
					
					String repositoryTreeValue = repository.getName();
					
					/*
					 * Filter the repositories with the repository-combo's value
					 */
					if (currentRepositoryValue == null || currentRepositoryValue.getId() == repository.getId()) {
						
						/*
						 * Filter the projects with the project-combo's value
						 */
						if (currentProjectValue == null	|| repository.getProject().getId().compareTo(currentProjectValue.getId()) == 0) {
							
							pullRequests = Activator.getServices().GetPullRequestsForRepo(repository.getProject().getKey(), repository.getName());
							
							if (pullRequests != null) {
								
								pullRequestValues = pullRequests.getValues();
								
								if (pullRequestValues.size() > 0) {
									
									PullRequestTreeViewerTreeParent repositoryTree = new PullRequestTreeViewerTreeParent(repositoryTreeValue);
									
									pullRequestValues.forEach(prValue -> {
										
										/*
										 * For each pull-request, fill the tree-view
										 */
										String treeName = String.format("%s - %s", prValue.getTitle(),
												prValue.getAuthor().getUser().getDisplayName());
										
										/*
										 * Apply the filters
										 */
										if (!lowerTextFilter.isBlank()) {
											
											switch (indexFilter) {
											case 0:
												// Filter on Pull Request's title
												if (prValue.getTitle().toLowerCase().contains(lowerTextFilter)) {
													PullRequestTreeViewerDataContainer pullRequest = new PullRequestTreeViewerDataContainer(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											case 1:
												// Filter on Pull Request's source branch name
												if (prValue.getFromRef().getDisplayId().toLowerCase().contains(lowerTextFilter)) {
													PullRequestTreeViewerDataContainer pullRequest = new PullRequestTreeViewerDataContainer(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											case 2:
												// Filter on Pull Request's target branch name
												if (prValue.getToRef().getDisplayId().toLowerCase().contains(lowerTextFilter)) {
													PullRequestTreeViewerDataContainer pullRequest = new PullRequestTreeViewerDataContainer(treeName, prValue);
													repositoryTree.addChild(pullRequest);
												}
												break;
											}
										} else {
											PullRequestTreeViewerDataContainer pullRequest = new PullRequestTreeViewerDataContainer(treeName, prValue);
											repositoryTree.addChild(pullRequest);
										}
										
									});
									
									this.addChild(repositoryTree);
								}
							}
						}
					}
				});
			}		
		}
	}
	
	public void addChild(PullRequestTreeViewerDataContainer child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(PullRequestTreeViewerDataContainer child) {
		children.remove(child);
		child.setParent(null);
	}

	public PullRequestTreeViewerDataContainer[] getChildren() {
		return (PullRequestTreeViewerDataContainer[]) children.toArray(new PullRequestTreeViewerDataContainer[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
}
