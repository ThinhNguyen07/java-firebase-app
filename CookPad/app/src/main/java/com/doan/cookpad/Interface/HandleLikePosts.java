package com.doan.cookpad.Interface;

public interface HandleLikePosts {
    void onLikeChangeListener(int like);
    void onFailure(String error);
}
