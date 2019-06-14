package com.doan.cookpad.Interface;

public interface BookmarkChangeListener {
    void Result(int number);
    void BookmarkExits(boolean exits);
    void Failure(String error);
}
