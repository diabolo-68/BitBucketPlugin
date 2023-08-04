
package com.diabolo.eclipse.bitbucket.api.pullquests;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Links {

    @SerializedName("self")
    @Expose
    private List<Self> self = new ArrayList<Self>();

    public List<Self> getSelf() {
        return self;
    }

    public void setSelf(List<Self> self) {
        this.self = self;
    }

}
