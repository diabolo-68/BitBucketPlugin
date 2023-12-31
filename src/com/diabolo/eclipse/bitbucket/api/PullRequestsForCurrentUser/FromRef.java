
package com.diabolo.eclipse.bitbucket.api.PullRequestsForCurrentUser;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class FromRef {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayId")
    @Expose
    private String displayId;
    @SerializedName("latestCommit")
    @Expose
    private String latestCommit;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("repository")
    @Expose
    private Repository repository;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

}
