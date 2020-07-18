package com.example.myapplication.Fragment2;

public class ImageInfo {
    int image;
    String imageTitle;
    String imageMenu;

    public ImageInfo(int image, String imageTitle, String imageMenu) {
        this.image = image;
        this.imageTitle = imageTitle;
        this.imageMenu = imageMenu;
    }
    public String getImageMenu() {
        return imageMenu;
    }
    public int getImage(){
        return image;
    }
    public String getImageTitle(){
        return imageTitle;
    }

    public void setImage(int image){
        this.image = image;
    }
    public void setImageTitle(String imageTitle){
        this.imageTitle = imageTitle;
    }
    public void setImageMenu(String imageMenu){
        this.imageMenu = imageMenu;
    }
}
