package com.doan.cookpad.Module.SeachAccount;

import android.support.annotation.NonNull;
import android.util.Log;

import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.Module.SearchFilter.FilterSearchs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountFilter {
    private ArrayList<Account> mListAccount;
    private FilterAccount filterAccount;

    public AccountFilter() {
        mListAccount = new ArrayList<>();
    }

    public void setFilterAccount(final FilterAccount filterAccount) {
        this.filterAccount = filterAccount;
        FirebaseDatabase.getInstance().getReference().child("Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mListAccount.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        mListAccount.add(data.getValue(Account.class));
                    }
                    filterAccount.onComplate(mListAccount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void onFilter(String filter){
        Log.d("lllllllllll",filter);
        Log.d("lllllllllll",mListAccount.size()+"");
        ArrayList<Account> mListFilter = new ArrayList<>();
        for (Account account : mListAccount){
            if (account.getmName()!=null) {
                if ((account.getmName().toLowerCase().contains(filter.toLowerCase()))) {
                    mListFilter.add(account);
                }
            }
        }
        filterAccount.onSearchComplate(mListFilter);
    }
}
