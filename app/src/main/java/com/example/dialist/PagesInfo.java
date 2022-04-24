package com.example.dialist;

import android.graphics.Bitmap;

public class PagesInfo {
    private String pageTitle;
    private Bitmap pageImage;

    public PagesInfo(String pageTitle, Bitmap pageImage){
        this.pageTitle = pageTitle;
        this.pageImage = pageImage;
    }

    public void setPageTitle(String pageTitle){
        this.pageTitle = pageTitle;
    }

    public void setPageImage(Bitmap pageImage){
        this.pageImage = pageImage;
    }

    public String getPageTitle(){
        return pageTitle;
    }

    public Bitmap getPageImage(){
        return pageImage;
    }
}