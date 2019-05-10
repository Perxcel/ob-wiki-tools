package com.perxcel.confluence.tools;

import java.util.HashMap;
import java.util.Map;

public class CopyPageResult {

    private String id;

    private Map<String, String> links = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "CopyPageResult{" + "id='" + id + '\'' + ", links=" + links + '}';
    }
}
