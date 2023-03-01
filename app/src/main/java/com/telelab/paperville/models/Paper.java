package com.telelab.paperville.models;

public class Paper {
    String name;
    String type;
    boolean isValid;
    String downloadPath;

    public Paper(String name, String type, boolean isValid){
        this.name = name;
        this.isValid = isValid;
        this.type = type;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isValid() {
        return isValid;
    }
}
