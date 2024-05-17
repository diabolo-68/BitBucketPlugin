package com.diabolo.eclipse.bitbucket.views;

import java.util.ArrayList;
import java.util.List;

import com.diabolo.eclipse.bitbucket.BitBucketServices;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.PullRequestForRepository;

public class PullRequestsTreeParent extends PullRequestsTreeObject {
	
	private ArrayList<PullRequestsTreeObject> children;

	public PullRequestsTreeParent(String name) {
		super(name);
		children = new ArrayList<PullRequestsTreeObject>();
	}

	// txtFilter.getText() = textFilter
	// cboFilterOn.getSelectionIndex() = indexFilter
	// final com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue = (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getData(cboProjects.getItem(idxProject)); 
	// final com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue = (com.diabolo.eclipse.bitbucket.api.Repositories.Value) cboRepositories.getData(cboRepositories.getItem(idxRepositories));	
	
	public PullRequestsTreeParent (String name, com.diabolo.eclipse.bitbucket.api.Projects.Value currentProjectValue, com.diabolo.eclipse.bitbucket.api.Repositories.Value currentRepositoryValue , BitBucketServices services, String textFilter, int indexFilter) {
		
		super(name);
		
		children = new ArrayList<PullRequestsTreeObject>();
		
		final String lowerTextFilter = textFilter.toLowerCase();
		
		/*
		 * Parse all repositories, 
		 * parse all projects of repositories,
		 * get all open pull-requests of projects
		 * 
		 */
		if (services.repositoriesValues.size() > 0) {
			
			services.repositoriesValues.forEach(repository -> {
			
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
						
						pullRequests = services.GetPullRequestsForRepo(repository.getProject().getKey(), repository.getName());
						
						if (pullRequests != null) {
							
							pullRequestValues = pullRequests.getValues();

							if (pullRequestValues.size() > 0) {
								
								PullRequestsTreeParent repositoryTree = new PullRequestsTreeParent(repositoryTreeValue);

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
												PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
												repositoryTree.addChild(pullRequest);
											}
											break;
										case 1:
											// Filter on Pull Request's source branch name
											if (prValue.getFromRef().getDisplayId().toLowerCase().contains(lowerTextFilter)) {
												PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
												repositoryTree.addChild(pullRequest);
											}
											break;
										case 2:
											// Filter on Pull Request's target branch name
											if (prValue.getToRef().getDisplayId().toLowerCase().contains(lowerTextFilter)) {
												PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
												repositoryTree.addChild(pullRequest);
											}
											break;
										}
									} else {
										PullRequestsTreeObject pullRequest = new PullRequestsTreeObject(treeName, prValue);
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
