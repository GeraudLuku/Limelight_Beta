package com.krystofrapp.limelightbeauty;


public class ModelTopOrdered {

    private int image;
    private String title;

    public ModelTopOrdered() {
    }

    public ModelTopOrdered(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

