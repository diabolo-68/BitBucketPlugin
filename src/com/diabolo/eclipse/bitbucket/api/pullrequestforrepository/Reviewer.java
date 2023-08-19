
package com.diabolo.eclipse.bitbucket.api.pullrequestforrepository;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Reviewer {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("approved")
    @Expose
    private Boolean approved;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lastReviewedCommit")
    @Expose
    private String lastReviewedCommit;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastReviewedCommit() {
        return lastReviewedCommit;
    }

    public void setLastReviewedCommit(String lastReviewedCommit) {
        this.lastReviewedCommit = lastReviewedCommit;
    }

}
