package com.doan.cookpad.Module.ConversationManager;

import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Channel;
import com.doan.cookpad.Model.Conversation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Random;

public class ConversationManager {
    private String UserID_1,UserName_1,UserPicture_1;
    private String UserID_2,UserName_2,UserPicture_2;
    private String ChannelID;

    public void setChannelID(String channelID) {
        ChannelID = channelID;
    }

    public void setUserID_1(String userID_1) {
        UserID_1 = userID_1;
    }

    public void setUserName_1(String userName_1) {
        UserName_1 = userName_1;
    }

    public void setUserPicture_1(String userPicture_1) {
        UserPicture_1 = userPicture_1;
    }

    public void setUserID_2(String userID_2) {
        UserID_2 = userID_2;
    }

    public void setUserName_2(String userName_2) {
        UserName_2 = userName_2;
    }

    public void setUserPicture_2(String userPicture_2) {
        UserPicture_2 = userPicture_2;
    }

    public void getChannelIDListener(final ChannelID channelID){
        FirebaseDatabase.getInstance().getReference().child("Conversation").child("ListChannel").child(UserID_1).child(UserID_2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    channelID.onComplate(dataSnapshot.getValue(Channel.class).getmChannelID());
                }else {
                    setChannelIDListener();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                channelID.onError(databaseError.toString());
            }
        });
    }
    public void setChannelIDListener(){
        String Key = String.valueOf(new Random().nextInt(999999999)) + System.currentTimeMillis();

        // Set Channel ID into List
        Channel channel = new Channel();
        channel.setmChannelID(Key);
        channel.setmName(UserName_1);
        channel.setmUserID(UserID_1);
        channel.setmPictureUser(UserPicture_1);

        Channel channel1 = new Channel();
        channel1.setmChannelID(Key);
        channel1.setmName(UserName_2);
        channel1.setmUserID(UserID_2);
        channel1.setmPictureUser(UserPicture_2);

        FirebaseDatabase.getInstance().getReference().child("Conversation").child("ListChannel").child(UserID_1).child(UserID_2).setValue(channel1);
        FirebaseDatabase.getInstance().getReference().child("Conversation").child("ListChannel").child(UserID_2).child(UserID_1).setValue(channel);
    }

    public void getMessageEndList(final GetMessageEndList messageEndList){
        final ArrayList<Conversation> mListConversation = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Conversation").child("Message").child(ChannelID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    mListConversation.add(dataSnapshot.getValue(Conversation.class));
                    messageEndList.onComplate(mListConversation.get(mListConversation.size()-1));
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
    public void getAllConversation(final ConversationListener listener){
        final ArrayList<Conversation> mListConversation = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Conversation").child("Message").child(ChannelID).orderByChild("send_TIME").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    mListConversation.add(dataSnapshot.getValue(Conversation.class));
                    listener.onSuccess(dataSnapshot.getValue(Conversation.class));
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
}
