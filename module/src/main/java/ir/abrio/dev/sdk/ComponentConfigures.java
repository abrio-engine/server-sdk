package ir.abrio.dev.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * component settings including database collections
 * and the component id
 */
class ComponentConfigures {

    @JsonProperty("componentId")
    public int componentId;
    @JsonProperty("collections")
    public List<String> collections;
    @JsonProperty("projectId")
    public String projectId;

}
