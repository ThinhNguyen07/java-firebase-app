package com.doan.cookpad.Interface;

public interface GetLikedListener {
    void Success(boolean liked);
    void Failure(String error);
}
