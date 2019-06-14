package com.doan.cookpad.Module.SearchFilter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.doan.cookpad.Interface.LoadSuccess;
import com.doan.cookpad.Model.Posts;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFilter {
    private ArrayList<Posts> mListPosts;
    private FilterSearchs filterSearchs;

    public SearchFilter(){
        mListPosts = new ArrayList<>();
    }
    public void getAllPosts(final LoadSuccess loadSuccess){
        // Lấy toàn bộ bài viết về
        FirebaseDatabase.getInstance().getReference().child("Posts").child("TimeLine").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    mListPosts.add(dataSnapshot.getValue(Posts.class));
                    loadSuccess.onComplate(mListPosts);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void setOnFilterListener(FilterSearchs filterSearchs){
        this.filterSearchs = filterSearchs;
    }

    // Lọc các đối tượng theo từ khóa người dùng nhập vào
    public void onFilterResource(String filter){

        ArrayList<Posts> mListFilter = new ArrayList<>();
        for (Posts posts : mListPosts){
            if (posts.getResources_Dish()!=null){
                for (int i=0;i<posts.getResources_Dish().size();i++){
                    if (posts.getResources_Dish().get(i).toLowerCase().contains(filter.toLowerCase())){
                        mListFilter.add(posts);
                    }
                }
            }
        }
        this.filterSearchs.onComplate(mListFilter);
    }
}
