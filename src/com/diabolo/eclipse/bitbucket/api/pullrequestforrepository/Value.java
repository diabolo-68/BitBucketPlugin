
package com.diabolo.eclipse.bitbucket.api.pullrequestforrepository;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Value {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("open")
    @Expose
    private Boolean open;
    @SerializedName("closed")
    @Expose
    private Boolean closed;
    @SerializedName("createdDate")
    @Expose
    private Long createdDate;
    @SerializedName("updatedDate")
    @Expose
    private Long updatedDate;
    @SerializedName("fromRef")
    @Expose
    private FromRef fromRef;
    @SerializedName("toRef")
    @Expose
    private ToRef toRef;
    @SerializedName("locked")
    @Expose
    private Boolean locked;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("reviewers")
    @Expose
    private List<Reviewer> reviewers;
    @SerializedName("participants")
    @Expose
    private List<Object> participants;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("links")
    @Expose
    private Links links;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public FromRef getFromRef() {
        return fromRef;
    }

    public void setFromRef(FromRef fromRef) {
        this.fromRef = fromRef;
    }

    public ToRef getToRef() {
        return toRef;
    }

    public void setToRef(ToRef toRef) {
        this.toRef = toRef;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public List<Object> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Object> participants) {
        this.participants = participants;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
