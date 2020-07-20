package com.example.myapplication.Retrofit;

public class MyImage {

    private String title;
    private String orgFileName;
    private String saveFileName;
    private String description;
    private String size;

    public MyImage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MyImage(String title, String orgFileName, String saveFileName, String description, String size) {
        this.title = title;
        this.orgFileName = orgFileName;
        this.saveFileName = saveFileName;
        this.description = description;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public String getSize() {
        return size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
