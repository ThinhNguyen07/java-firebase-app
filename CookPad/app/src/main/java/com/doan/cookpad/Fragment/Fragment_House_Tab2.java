package com.doan.cookpad.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doan.cookpad.Adapter.BookmarkAdapter;
import com.doan.cookpad.Model.Bookmark;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Fragment_House_Tab2 extends Fragment {

    private RecyclerView mRecyclerView;
    private View view;
    private BookmarkAdapter bookmarkAdapter;
    private ArrayList<Bookmark> mList;
    private Account currentAccount;
    private Utilities utilities;

    public Fragment_House_Tab2() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_tab2, container, false);

        initView();
        initUI();
        initEvent();

        return view;
    }

    private void initEvent() {
        FirebaseDatabase.getInstance().getReference().child("Bookmark").child(currentAccount.getmID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    mList.add(dataSnapshot.getValue(Bookmark.class));
                    bookmarkAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initUI() {
        utilities = new Utilities();
        currentAccount = utilities.getUserFromLocal(getActivity());
        mList = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkAdapter = new BookmarkAdapter(mList,getActivity());
        mRecyclerView.setAdapter(bookmarkAdapter);
    }

    private void initView() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
    }

}
