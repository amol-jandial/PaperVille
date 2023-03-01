package com.telelab.admin_app;

public class Paper {

    boolean isValid;
    String uri;
    String name;
    String type;

    public Paper(boolean isValid, String uri, String name, String type) {
        this.isValid = isValid;
        this.uri = uri;
        this.name = name;
        this.type = type;
    }

    public Paper(){

    }

    public boolean isValid() {
        return isValid;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return
                "isValid=" + isValid +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
