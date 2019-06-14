package com.doan.cookpad.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Adapter.ConversationAdapter;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Module.ConversationManager.ChannelID;
import com.doan.cookpad.Module.ConversationManager.ConversationListener;
import com.doan.cookpad.Module.ConversationManager.ConversationManager;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// Class này có chức năng xem thông tin chi tiết về cuộc trò chuyện bao gồm xem và trả lời tin nhắn
public class Conversation extends AppCompatActivity {

    private CircleImageView img_PictureUser;
    private TextView txt_NameUser;
    private RecyclerView mRecyclerView_Conversation;
    private ConversationAdapter conversationAdapter;
    private EditText mMessage;
    private ImageView mSendMessage;
    private Account account;
    private ConversationManager conversationManager;
    private String receive_ID,receive_Name,receive_Picture;
    private ArrayList<com.doan.cookpad.Model.Conversation> mListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Ánh xạ VIEW
        initView();
        // Nhận dữ liệu từ Intent
        initIntentData();
        // Thay đổi giao diện người dùng
        initUI();
        // Kiểm tra xem ID người dùng trong cuộc trò chuyện có trùng với ID người dùng hiện tại hay ko. Nếu trùng sẽ hiển thị hộp thoại và trở lại
        if (!receive_ID.equals(account.getmID())) {
            // Nếu trùng thì sẽ cho phép lắng nghe các sự kiện trong cuộc trò chuyện
            initEvent();
        }else {
            // Hiển thị hộp thoại thông báo
            AlertDialog alertDialog = new AlertDialog.Builder(Conversation.this)
                    .setMessage("Bạn không thể nhắn tin với bản thân bạn. Vui lòng thử lại với một ai khác")
                    .setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            alertDialog.show();
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
        }
    }

    private void initIntentData() {
        if (getIntent()!=null){
            receive_ID = getIntent().getStringExtra(Constant.KEY_ID);
            receive_Name = getIntent().getStringExtra(Constant.KEY_NAME);
            receive_Picture = getIntent().getStringExtra(Constant.KEY_PICTURE);
        }
    }

    private void initEvent() {
        // Lấy ID cuộc trò chuyện
        conversationManager.getChannelIDListener(new ChannelID() {
            @Override
            public void onComplate(final String channel_ID) {
                if (channel_ID!=null){
                    // Gán ID cuộc trò chuyện vào đối tượng tùy chỉnh "Conversation Manager"
                    conversationManager.setChannelID(channel_ID);
                    // Lấy về toàn bộ tin nhắn trong cuộc trò chuyện và hiển thị vào danh sách
                    conversationManager.getAllConversation(new ConversationListener() {
                        @Override
                        public void onSuccess(com.doan.cookpad.Model.Conversation Message) {
                            mListItem.add(Message);
                            mRecyclerView_Conversation.scrollToPosition(mListItem.size()-1);
                        }
                    });
                    // Lắng nghe sự kiện người dùng Click vào button gửi tin nhắn
                    mSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Kiểm tra xem Edittext có đc nhập text ko ?
                            if (!TextUtils.isEmpty(mMessage.getText())){
                                // Khởi tạo đối tượng Conversation
                                com.doan.cookpad.Model.Conversation conversation = new com.doan.cookpad.Model.Conversation();
                                // Gán dữ liệu cho các thuộc tính của Conversation
                                conversation.setID(account.getmID());
                                conversation.setSEND_TIME(System.currentTimeMillis());
                                conversation.setMESSAGE(mMessage.getText().toString());

                                // Gửi tin nhắn lên Firebase Database
                                FirebaseDatabase.getInstance().getReference().child("Conversation").child("Message").child(channel_ID).push().setValue(conversation, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError==null){
                                            mMessage.setText("");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void initUI() {
        mRecyclerView_Conversation.setHasFixedSize(true);
        mRecyclerView_Conversation.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView_Conversation.setAdapter(conversationAdapter);

        Utilities utilities = new Utilities();
        account = utilities.getUserFromLocal(getApplicationContext());
        conversationManager = new ConversationManager();

        Glide.with(getApplicationContext()).load(receive_Picture).into(img_PictureUser);
        txt_NameUser.setText(receive_Name);

        conversationManager.setUserID_1(account.getmID());
        conversationManager.setUserName_1(account.getmName());
        conversationManager.setUserPicture_1(account.getmPicture());

        conversationManager.setUserID_2(receive_ID);
        conversationManager.setUserName_2(receive_Name);
        conversationManager.setUserPicture_2(receive_Picture);
    }

    private void initView() {
        mListItem = new ArrayList<>();
        img_PictureUser = findViewById(R.id.img_PictureUser);
        txt_NameUser = findViewById(R.id.txt_NameUser);
        mMessage = findViewById(R.id.mMessage);
        mSendMessage = findViewById(R.id.mSendMessage);
        mRecyclerView_Conversation = findViewById(R.id.mRecyclerView_Conversation);
        conversationAdapter = new ConversationAdapter(mListItem,getApplicationContext());
    }

    public void onBack(View view){
        finish();
    }
}
