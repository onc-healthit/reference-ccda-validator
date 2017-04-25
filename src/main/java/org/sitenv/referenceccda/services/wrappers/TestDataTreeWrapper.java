package org.sitenv.referenceccda.services.wrappers;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Brian on 8/26/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDataTreeWrapper {
    @JsonProperty("path")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getPathArray(){
        return StringUtils.split(path, '/');
    }

    public String getMessageType(){
        return getPathArray()[0];
    }

    public String getValidationObjective(){
        if(getPathArray().length > 1){
            return getPathArray()[1];
        }
        return null;
    }

    public String getReferenceFileName(){
        if(getPathArray().length > 2){
            return getPathArray()[2];
        }
        return null;
    }
}
