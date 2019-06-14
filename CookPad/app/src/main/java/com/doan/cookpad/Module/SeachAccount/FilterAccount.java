package com.doan.cookpad.Module.SeachAccount;

import com.doan.cookpad.Model.Account;

import java.util.ArrayList;

public interface FilterAccount {
    void onComplate(ArrayList<Account> mListAccount);
    void onSearchComplate(ArrayList<Account> mListAccount);
}
