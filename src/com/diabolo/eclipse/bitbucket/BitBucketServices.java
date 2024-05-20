package com.diabolo.eclipse.bitbucket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.diabolo.eclipse.bitbucket.api.Projects.Projects;
import com.diabolo.eclipse.bitbucket.api.PullRequestsForCurrentUser.PullRequestsForCurrentUser;
import com.diabolo.eclipse.bitbucket.api.Repositories.Repositories;
import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.PullRequestForRepository;
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BitBucketServices {

	private URL url;
	private String host;
	private String basePath;
	private String auth;

	public Projects projects;
	public Repositories repositories;
	
	public List<com.diabolo.eclipse.bitbucket.api.Projects.Value> projectsValues;
	public List<com.diabolo.eclipse.bitbucket.api.Repositories.Value> repositoriesValues;
	
	
	public BitBucketServices() {
		super();
        ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.diabolo.eclipse.bitbucket");
    	auth = "Basic " + Base64.getEncoder().encodeToString((store.getString(PreferenceConstants.P_BBUSER) + ":"
                + store.getString(PreferenceConstants.P_BBPASSWORD)).getBytes());;
        host = store.getString(PreferenceConstants.P_HOST);
        basePath = store.getString(PreferenceConstants.P_BASEPATH);
        Update();
	}

	public void Update() {
		System.out.println("Update Data");		
		projects = GetProjects();
		repositories = GetRepositories();
		if (projects != null && repositories != null) {
			repositoriesValues = repositories.getValues();
			projectsValues = projects.getValues();					
		}
	}
	
	/**
	 * @param url the url to set
	 * @throws MalformedURLException 
	 */
	private void SetUrl(Api apiName, UrlProtocol protocol, String host, String basePath) throws MalformedURLException {
		setUrl (apiName, protocol, host, basePath, "", "");
	}
		
	private void setUrl(Api apiName, UrlProtocol protocol, String host, String basePath, String projectKey, String RepositorySlug) throws MalformedURLException {
		SetUrl (apiName, protocol, host, basePath, projectKey, RepositorySlug, "");
	}

	/**
	 * @param url the url to set
	 * @throws MalformedURLException 
	 */
	private void SetUrl(Api apiName, UrlProtocol protocol, String host, String basePath, String projectKey, String repositorySlug, String filter) throws MalformedURLException {
		
		switch (apiName) {
			case GET_PULLREQUESTS_FOR_CURRENT_USER:
				this.url = new URL(String.format("%s://%s/%s/api/latest/dashboard/pull-requests?limit=1000&state=%s",protocol,host, basePath, filter)); 
				//{{protocol}}://{{host}}/{{basePath}}api/latest/dashboard/pull-requests
				break;
			case GET_PROJECTS:
				this.url = new URL(String.format("%s://%s/%s/api/latest/projects?limit=1000",protocol,host,basePath)); 
				//{{protocol}}://{{host}}/{{basePath}}api/latest/projects
				break;
			case GET_REPOSITORIES:
				this.url = new URL(String.format("%s://%s/%s/api/latest/repos?limit=1000",protocol,host,basePath)); 
				//{{protocol}}://{{host}}/{{basePath}}api/latest/repos
				break;
			case GET_PULLREQUESTS_FOR_REPO:
				this.url = new URL(String.format("%s://%s/%s/api/latest/projects/%s/repos/%s/pull-requests/?limit=1000",protocol,host,basePath, projectKey, repositorySlug)); 
				//{{protocol}}://{{host}}/{{basePath}}api/latest/projects/:projectKey/repos/:repositorySlug/pull-requests
				break;
			default:
				break;
		};
	}

	private HttpURLConnection setBaseConnection(httpMethod method) {
		
        HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) this.url.openConnection();
			connection.setRequestMethod(method.toString());
			connection.setDoOutput(true);
			connection.setRequestProperty ("Authorization", this.auth);
			connection.setRequestProperty ("Accept", "application/json");
			connection.setRequestProperty ("Content-Type", "application/json");
			return connection;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private StringBuffer getHttpResponse(HttpURLConnection connection) {
        BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String s = "";
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}
			
			return sb;
		} catch (IOException e) {
			return null;
		}

	}
	
	public PullRequestsForCurrentUser GetPullRequests(PullRequestState state) {
				
		try {
			SetUrl(Api.GET_PULLREQUESTS_FOR_CURRENT_USER, UrlProtocol.https , host, basePath, "", "", state.toString());
			HttpURLConnection connection = setBaseConnection(httpMethod.GET);
			
			if (connection.getResponseCode() == 200) {
				StringBuffer response = getHttpResponse(connection);
				
				Gson pullRequestsResponse = new Gson();
				
				PullRequestsForCurrentUser pullRequests = pullRequestsResponse.fromJson(response.toString(), PullRequestsForCurrentUser.class);
				
				return pullRequests;	    	
			}
			return null;
			
		} catch (IOException | JsonSyntaxException e) {
			return null;
		}
		
        
	}
	
	public PullRequestForRepository GetPullRequestsForRepo(String projectKey, String repositorySlug) {
		
		try {
			setUrl(Api.GET_PULLREQUESTS_FOR_REPO, UrlProtocol.https , host, basePath, projectKey, repositorySlug);
			HttpURLConnection connection = setBaseConnection(httpMethod.GET);
			
			if (connection.getResponseCode() == 200) {
				StringBuffer response = getHttpResponse(connection);
				
				Gson pullRequestsResponse = new Gson();
				
				PullRequestForRepository pullRequests = pullRequestsResponse.fromJson(response.toString(), PullRequestForRepository.class);
				
				return pullRequests;
			}    	        
			return null;
		} catch (IOException | JsonSyntaxException e) {
			return null;
		}
		
	}

	public Projects GetProjects() {
		
		try {
			SetUrl(Api.GET_PROJECTS, UrlProtocol.https , host, basePath);
			HttpURLConnection connection = setBaseConnection(httpMethod.GET);
			
			if (connection.getResponseCode() == 200) {
				StringBuffer response = getHttpResponse(connection);
				
				Gson projectsResponse = new Gson();
				
				Projects projects = projectsResponse.fromJson(response.toString(), Projects.class);
				
				return projects;
			}
			
			return null;
		} catch (IOException | JsonSyntaxException e) {
			return null;
		}
		
	}
		
	public Repositories GetRepositories() {
		
		try {
			SetUrl(Api.GET_REPOSITORIES, UrlProtocol.https , host, basePath);
			HttpURLConnection connection = setBaseConnection(httpMethod.GET);
			
			if (connection.getResponseCode() == 200) {
				StringBuffer response = getHttpResponse(connection);
				
				Gson repositoriesResponse = new Gson();
				
				Repositories repositories = repositoriesResponse.fromJson(response.toString(), Repositories.class);
				
				return repositories;
			}		
			
			return null;
		} catch (IOException | JsonSyntaxException e) {
			return null;
		}
	}
}
