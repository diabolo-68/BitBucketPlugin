package com.diabolo.eclipse.bitbucket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Base64;

import org.apache.commons.httpclient.HttpConstants;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.diabolo.eclipse.bitbucket.api.objects.Projects;
import com.diabolo.eclipse.bitbucket.api.objects.Pullrequests;
import com.diabolo.eclipse.bitbucket.api.objects.Repositories;
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;
import com.google.gson.Gson;

public class Services {

	private URL url;
	public Services() {
		super();
        ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE,
                "com.diabolo.eclipse.bitbucket");
    	auth = "Basic " + Base64.getEncoder().encodeToString((store.getString(PreferenceConstants.P_BBUSER) + ":"
                + store.getString(PreferenceConstants.P_BBPASSWORD)).getBytes());;
        host = store.getString(PreferenceConstants.P_HOST);
        basePath = store.getString(PreferenceConstants.P_BASEPATH);
        protocol = store.getString(PreferenceConstants.P_PROTOCOL);
    	//services.setAuth(PreferenceConstants.P_BBUSER,PreferenceConstants.P_BBPASSWORD);

	}

	private String protocol;
	private String host;
	private String basePath;
	private String projectKey;
	private String RepositorySlug;
	private String auth;

	/**
	 * @param url the url to set
	 * @throws MalformedURLException 
	 */
	private void setUrl(Api apiName, UrlProtocol protocol, String host, String basePath) throws MalformedURLException {
		setUrl (apiName, protocol, host, basePath, "", "");
	}
		
	private void setUrl(Api apiName, UrlProtocol protocol, String host, String basePath, String projectKey, String RepositorySlug) throws MalformedURLException {
		setUrl (apiName, protocol, host, basePath, projectKey, RepositorySlug, "");
	}

	/**
	 * @param url the url to set
	 * @throws MalformedURLException 
	 */
	private void setUrl(Api apiName, UrlProtocol protocol, String host, String basePath, String projectKey, String repositorySlug, String filter) throws MalformedURLException {
		this.protocol = protocol.toString();
		this.host = host;
		this.basePath = basePath;
		this.projectKey = projectKey;
		this.RepositorySlug = repositorySlug;
		
		switch (apiName) {
			case GET_PULLREQUESTS:
				//System.out.printf("%s://%s/%s/api/latest/dashboard/pull-requests?limit=1000",protocol,host, basePath);
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

	private HttpURLConnection setBaseConnection(httpMethod method) throws IOException {
		
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod(method.toString());
        connection.setDoOutput(true);
        connection.setRequestProperty ("Authorization", this.auth);
        connection.setRequestProperty ("Accept", "application/json");
        connection.setRequestProperty ("Content-Type", "application/json");
        return connection;
	}

	private StringBuffer getHttpResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuffer sb = new StringBuffer();
        String s = "";
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        
        return sb;
	}
	
	public Pullrequests GetPullRequests(pullRequestState state) throws IOException {
				
		setUrl(Api.GET_PULLREQUESTS, UrlProtocol.https , host, basePath, "", "", state.toString());
		
	    HttpURLConnection connection = setBaseConnection(httpMethod.GET);
	        
	    StringBuffer response = getHttpResponse(connection);
			
	    Gson pullRequestsResponse = new Gson();

	    Pullrequests pullRequests = pullRequestsResponse.fromJson(response.toString(), Pullrequests.class);
		
		return pullRequests;
        
	}
	
	public Pullrequests GetPullRequestsForRepo(String projectKey, String repositorySlug) throws IOException {
		
		setUrl(Api.GET_PULLREQUESTS_FOR_REPO, UrlProtocol.https , host, basePath, projectKey, repositorySlug);
		
	    HttpURLConnection connection = setBaseConnection(httpMethod.GET);
	        
	    StringBuffer response = getHttpResponse(connection);
			
	    Gson pullRequestsResponse = new Gson();

	    Pullrequests pullRequests = pullRequestsResponse.fromJson(response.toString(), Pullrequests.class);
		
		return pullRequests;
        
	}

	public Projects GetProjects() throws IOException {
		
		setUrl(Api.GET_PROJECTS, UrlProtocol.https , host, basePath);
		
		HttpURLConnection connection = setBaseConnection(httpMethod.GET);
	        
	    StringBuffer response = getHttpResponse(connection);
			
	    Gson projectsResponse = new Gson();

	    Projects projects = projectsResponse.fromJson(response.toString(), Projects.class);
		
		return projects;
        
	}
		
	public Repositories GetRepositories() throws IOException {
		
		setUrl(Api.GET_REPOSITORIES, UrlProtocol.https , host, basePath);
		
		HttpURLConnection connection = setBaseConnection(httpMethod.GET);
	        
	    StringBuffer response = getHttpResponse(connection);
			
	    Gson repositoriesResponse = new Gson();

	    Repositories repositories = repositoriesResponse.fromJson(response.toString(), Repositories.class);
		
		return repositories;
        
	}
}
