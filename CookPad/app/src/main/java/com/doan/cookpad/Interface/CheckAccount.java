package com.doan.cookpad.Interface;

import com.doan.cookpad.Model.Account;

public interface CheckAccount {
    void NotExist();
    void Exist(Account account);
}
