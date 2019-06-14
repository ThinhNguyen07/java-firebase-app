package com.doan.cookpad.Model;

public class Channel {
    private String mChannelID,
                    mUserID,
                    mName,
                    mPictureUser;

    public Channel(String mChannelID, String mUserID, String mName, String mPictureUser) {
        this.mChannelID = mChannelID;
        this.mUserID = mUserID;
        this.mName = mName;
        this.mPictureUser = mPictureUser;
    }

    public Channel() {
    }

    public String getmChannelID() {
        return mChannelID;
    }

    public void setmChannelID(String mChannelID) {
        this.mChannelID = mChannelID;
    }

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPictureUser() {
        return mPictureUser;
    }

    public void setmPictureUser(String mPictureUser) {
        this.mPictureUser = mPictureUser;
    }
}
