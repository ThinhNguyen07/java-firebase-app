package com.doan.cookpad.Interface;

import com.doan.cookpad.Model.Posts;

public interface DetailsPosts {
    void Success(Posts posts);
    void Failure(String error);
}
