package com.doan.cookpad.Model;

public class Account {
    private String mID,mName,mEmail,mPicture,mPosition,mFeelings;

    public Account(String mID, String mName, String mEmail, String mPicture, String mPosition, String mFeelings) {
        this.mID = mID;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPicture = mPicture;
        this.mPosition = mPosition;
        this.mFeelings = mFeelings;
    }

    public Account() {
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPicture() {
        return mPicture;
    }

    public void setmPicture(String mPicture) {
        this.mPicture = mPicture;
    }

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }

    public String getmFeelings() {
        return mFeelings;
    }

    public void setmFeelings(String mFeelings) {
        this.mFeelings = mFeelings;
    }
}
