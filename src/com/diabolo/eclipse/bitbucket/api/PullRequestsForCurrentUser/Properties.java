
package com.diabolo.eclipse.bitbucket.api.PullRequestsForCurrentUser;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Properties {

    @SerializedName("mergeResult")
    @Expose
    private MergeResult mergeResult;
    @SerializedName("resolvedTaskCount")
    @Expose
    private Integer resolvedTaskCount;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("openTaskCount")
    @Expose
    private Integer openTaskCount;

    public MergeResult getMergeResult() {
        return mergeResult;
    }

    public void setMergeResult(MergeResult mergeResult) {
        this.mergeResult = mergeResult;
    }

    public Integer getResolvedTaskCount() {
        return resolvedTaskCount;
    }

    public void setResolvedTaskCount(Integer resolvedTaskCount) {
        this.resolvedTaskCount = resolvedTaskCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getOpenTaskCount() {
        return openTaskCount;
    }

    public void setOpenTaskCount(Integer openTaskCount) {
        this.openTaskCount = openTaskCount;
    }

}
