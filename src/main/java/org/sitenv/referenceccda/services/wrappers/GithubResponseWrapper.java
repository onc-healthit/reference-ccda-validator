package org.sitenv.referenceccda.services.wrappers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private List<TestDataTreeWrapper> tree;

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

    public List<TestDataTreeWrapper> getTree() {
        return tree;
    }

    public void setTree(List<TestDataTreeWrapper> tree) {
        this.tree = tree;
    }
}
