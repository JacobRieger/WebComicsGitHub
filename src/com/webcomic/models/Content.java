package com.webcomic.models;

import android.graphics.Bitmap;

/**
 * Created by Jacob on 9/7/13.
 */
public class Content {

    private String ImageUrl;
    private Bitmap Image;
    private String AltText;

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getAltText() {
        return altText;
    }
    public void setAltText(String altText) {
        this.altText = altText;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Content(String imageUrl, Bitmap image, String altText)
    {
        ImageUrl = imageUrl;
        Image = image;
        AltText = altText;
    }

}
