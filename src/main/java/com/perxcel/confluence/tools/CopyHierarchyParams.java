package com.perxcel.confluence.tools;

public class CopyHierarchyParams {
    private boolean copyAttachments;

    private boolean copyPermissions;

    private boolean copyProperties;

    private boolean copyLabels;

    private String originalPageId;

    private String destinationPageId;

    private TitleOptions titleOptions;

    public CopyHierarchyParams() {
        // TODO Auto-generated constructor stub
    }

    public static CopyHierarchyParams newInstanceWithSourceId(String sourceId) {
        CopyHierarchyParams request = new CopyHierarchyParams();
        request.setCopyAttachments(true);
        request.setCopyPermissions(true);
        request.setCopyLabels(true);
        request.setCopyProperties(true);
        request.setTitleOptions(new TitleOptions());
        request.setOriginalPageId(sourceId);
        return request;
    }


    public CopyHierarchyParams addDestinationId(String destinationId) {
        this.setDestinationPageId(destinationId);
        return this;
    }

    public CopyHierarchyParams addSearchString(String search) {
        this.getTitleOptions().setSearch(search);
        return this;
    }

    public CopyHierarchyParams addReplaceWith(String replaceWith) {
        this.getTitleOptions().setReplace(replaceWith);
        return this;
    }

    public boolean isCopyAttachments() {
        return copyAttachments;
    }

    public void setCopyAttachments(boolean copyAttachments) {
        this.copyAttachments = copyAttachments;
    }

    public boolean isCopyPermissions() {
        return copyPermissions;
    }

    public void setCopyPermissions(boolean copyPermissions) {
        this.copyPermissions = copyPermissions;
    }

    public boolean isCopyProperties() {
        return copyProperties;
    }

    public void setCopyProperties(boolean copyProperties) {
        this.copyProperties = copyProperties;
    }

    public boolean isCopyLabels() {
        return copyLabels;
    }

    public void setCopyLabels(boolean copyLabels) {
        this.copyLabels = copyLabels;
    }

    public String getOriginalPageId() {
        return originalPageId;
    }

    public void setOriginalPageId(String originalPageId) {
        this.originalPageId = originalPageId;
    }

    public String getDestinationPageId() {
        return destinationPageId;
    }

    public void setDestinationPageId(String destinationPageId) {
        this.destinationPageId = destinationPageId;
    }

    public TitleOptions getTitleOptions() {
        return titleOptions;
    }

    public void setTitleOptions(TitleOptions titleOptions) {
        this.titleOptions = titleOptions;
    }

    public static class TitleOptions {
        private String prefix;

        private String replace;

        private String search;

        public TitleOptions() {
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getReplace() {
            return replace;
        }

        public void setReplace(String replace) {
            this.replace = replace;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }
    }
}
