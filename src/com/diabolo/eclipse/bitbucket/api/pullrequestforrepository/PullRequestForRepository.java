
package com.diabolo.eclipse.bitbucket.api.pullrequestforrepository;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class PullRequestForRepository {

    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("isLastPage")
    @Expose
    private Boolean isLastPage;
    @SerializedName("values")
    @Expose
    private List<Value> values;
    @SerializedName("start")
    @Expose
    private Integer start;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(Boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

}
