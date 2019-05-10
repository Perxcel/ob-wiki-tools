package com.perxcel.confluence.tools;

public class PageBody {
    private Storage storage;

    private View view;

    Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "PageBody{" + "storage=" + storage + ", view=" + view + '}';
    }

    public static class Storage {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Storage{" + "value='" + value + '\'' + '}';
        }
    }

    public static class View {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Storage{" + "value='" + value + '\'' + '}';
        }
    }
}
