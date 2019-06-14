package com.doan.cookpad.Module.ConversationManager;

import com.doan.cookpad.Model.Channel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListConversationFilter {
    private ArrayList<Channel> mListChannel;
    private FilterConversation filterChannel;
    private ChannelList channelList;

    public ListConversationFilter(){
        mListChannel = new ArrayList<>();
    }
    public void setOnFilterListener(FilterConversation filterChannel){
        this.filterChannel = filterChannel;
    }

    public void setChannelListener(ChannelList channelList){
        this.channelList = channelList;
    }

    public void onFilter(String filter){
        ArrayList<Channel> mListFilter = new ArrayList<>();
        for (Channel channel : mListChannel){
            if (channel.getmName().toLowerCase().contains(filter)){
                mListFilter.add(channel);
            }
        }
        this.filterChannel.onSuccess(mListFilter);
    }
    public void getAllConversation(String IDUser){
        FirebaseDatabase.getInstance().getReference().child("Conversation").child("ListChannel").child(IDUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        mListChannel.add(data.getValue(Channel.class));
                    }
                    channelList.onSuccess(mListChannel);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
