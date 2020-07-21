package com.example.myapplication.Fragment2;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageInfo {
    String image; // saveFileName
    String imageTitle; // Title
    String imageMenu; // Description

    public ImageInfo(String image, String imageTitle, String imageMenu) {
        this.image = image;
        this.imageTitle = imageTitle;
        this.imageMenu = imageMenu;
    }
    public String getImageMenu() {
        return imageMenu;
    }
    public String getImage(){
        return image;
    }
    public String getImageTitle(){
        return imageTitle;
    }

    public void setImage(String image){
        this.image = image;
    }
    public void setImageTitle(String imageTitle){
        this.imageTitle = imageTitle;
    }
    public void setImageMenu(String imageMenu){
        this.imageMenu = imageMenu;
    }
}
