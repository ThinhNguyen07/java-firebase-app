package com.doan.cookpad.Model;

public class CookingSteps {
    private String mTitle,mImage;

    public CookingSteps(String mTitle, String mImage) {
        this.mTitle = mTitle;
        this.mImage = mImage;
    }

    public CookingSteps() {
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmImage() {
        return mImage;
    }
}
