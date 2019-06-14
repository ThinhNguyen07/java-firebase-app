package com.doan.cookpad.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Adapter.CommentAdapter;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Comment;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

// Class này có chức năng xem thông tin chi tiết các comment về công thức món ăn
public class CommentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CircleImageView img_PictureUser,img_CurrentPictureUser;
    private TextView txt_name_Dish,txt_NameUser;
    private ImageView img_Send;
    private EditText edt_Message;
    private String iD_User,iD_Posts;
    private Utilities utilities;
    private Account currentAccount;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> mList;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Ánh xạ các View
        initView();
        // Nhận dữ liệu từ Intent
        initDataIntent();
        // Thay đổi giao diện người dùng
        initUI();
        // Lắng nghe các sự kiện thay đổi
        initEvent();
    }

    private void initEvent() {
        // Lắng nghe người dùng thêm,sửa,xóa,... comment
        FirebaseDatabase.getInstance().getReference().child("Posts").child("Comment").child(iD_Posts).addChildEventListener(new ChildEventListener() {
            // Phương thức này chạy khi người dùng Thêm dữ liệu vào
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    // Thêm dữ liệu vào list
                    mList.add(dataSnapshot.getValue(Comment.class));
                    // Load lại adapter
                    commentAdapter.notifyDataSetChanged();
                }
            }
            // Phương thức này chạy khi người dùng Thay đổi dữ liệu vào
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            // Phương thức này chạy khi người dùng Xóa dữ liệu vào
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            // Phương thức này chạy khi người dùng Di chuyển dữ liệu vào
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            // Phương thức này chạy khi sự kiện lắng nghe bị lỗi
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // Lắng nghe sự kiện Click vào button gửi
        img_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra có dữ liệu trong editext hay ko
                if (!TextUtils.isEmpty(edt_Message.getText())){
                    // Lấy thời gian hiện tại
                    long currentMilisecond = System.currentTimeMillis();
                    String Key_Comment = String.valueOf(new Random().nextInt(999999999)) + String.valueOf(currentMilisecond);

                    // Tạo đối tượng Comment
                    Comment comment = new Comment();
                    // Gán dữ liệu vào các thuộc tính của đối tượng
                    comment.setIdUser(currentAccount.getmID());
                    comment.setIdComment(Key_Comment);
                    comment.setCreateDate(currentMilisecond);
                    comment.setMessage(edt_Message.getText().toString());

                    // Lưu dữ liệu comment lên database
                    FirebaseDatabase.getInstance().getReference().child("Posts").child("Comment").child(iD_Posts).child(Key_Comment).setValue(comment, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError==null){
                                edt_Message.setText("");
                            }else {
                                Toast.makeText(CommentActivity.this, "Bình luận thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_32dp);

        utilities = new Utilities();

        currentAccount = utilities.getUserFromLocal(getApplicationContext());
        Glide.with(getApplicationContext()).load(currentAccount.getmPicture()).into(img_CurrentPictureUser);

        utilities.getUserFromID(iD_User, new GetAccount() {
            @Override
            public void Success(Account account) {
                if (account!=null){
                    Glide.with(getApplicationContext()).load(account.getmPicture()).into(img_PictureUser);
                    txt_NameUser.setText(account.getmName());
                }
            }
            @Override
            public void Failure() {

            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentAdapter = new CommentAdapter(mList,getApplicationContext());
        mRecyclerView.setAdapter(commentAdapter);
    }

    private void initDataIntent() {
        mList = new ArrayList<>();
        iD_User = getIntent().getStringExtra("user");
        iD_Posts = getIntent().getStringExtra("posts");
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        img_PictureUser = findViewById(R.id.img_PictureUser);
        img_CurrentPictureUser = findViewById(R.id.img_CurrentPictureUser);
        txt_name_Dish = findViewById(R.id.txt_name_Dish);
        txt_NameUser = findViewById(R.id.txt_NameUser);
        img_Send = findViewById(R.id.img_Send);
        edt_Message = findViewById(R.id.edt_Message);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
