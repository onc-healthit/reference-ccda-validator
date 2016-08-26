package org.sitenv.referenceccda.controllers.wrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Brian on 8/24/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubResponseWrapper {
    @JsonProperty("sha")
    private String sha;
    @JsonProperty("url")
    private String url;
    @JsonProperty("tree")
    private List<TestDataTreeWrapper> testDataTrees;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TestDataTreeWrapper> getTestDataTrees() {
        return testDataTrees;
    }

    public void setTestDataTrees(List<TestDataTreeWrapper> testDataTrees) {
        this.testDataTrees = testDataTrees;
    }


}
