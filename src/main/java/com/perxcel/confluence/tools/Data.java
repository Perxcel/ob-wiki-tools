package com.perxcel.confluence.tools;

import java.io.Serializable;

public class Data implements Serializable {

    private String pageId;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
