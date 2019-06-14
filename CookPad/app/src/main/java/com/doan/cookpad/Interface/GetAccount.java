package com.doan.cookpad.Interface;

import com.doan.cookpad.Model.Account;

public interface GetAccount {
    void Success(Account account);
    void Failure();
}
