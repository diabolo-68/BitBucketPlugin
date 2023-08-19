
package com.diabolo.eclipse.bitbucket.api.pullrequestforrepository;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MergeResult {

    @SerializedName("outcome")
    @Expose
    private String outcome;
    @SerializedName("current")
    @Expose
    private Boolean current;

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

}
