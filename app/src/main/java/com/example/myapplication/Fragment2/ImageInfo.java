package com.example.myapplication.Fragment2;

import android.graphics.Bitmap;

public class ImageInfo {
    Bitmap image;
    String imageTitle;
    String imageMenu;

    public ImageInfo(Bitmap image, String imageTitle, String imageMenu) {
        this.image = image;
        this.imageTitle = imageTitle;
        this.imageMenu = imageMenu;
    }
    public String getImageMenu() {
        return imageMenu;
    }
    public Bitmap getImage(){
        return image;
    }
    public String getImageTitle(){
        return imageTitle;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }
    public void setImageTitle(String imageTitle){
        this.imageTitle = imageTitle;
    }
    public void setImageMenu(String imageMenu){
        this.imageMenu = imageMenu;
    }
}
