package com.example.myapplication.Retrofit;

public class File {
    private String title;
    private String description;
    private String orgFileName;
    private String saveFileName;
    private String size;

    public File (String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getSaveFileName(){
        return saveFileName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
