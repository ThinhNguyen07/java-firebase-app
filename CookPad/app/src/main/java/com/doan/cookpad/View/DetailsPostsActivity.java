package com.doan.cookpad.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Adapter.CommentAdapter;
import com.doan.cookpad.Adapter.ListResourcesAdapter;
import com.doan.cookpad.Adapter.ListStepsCookingAdapter;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Interface.BookmarkChangeListener;
import com.doan.cookpad.Interface.DetailsPosts;
import com.doan.cookpad.Interface.FilterFriend;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Interface.GetLikedListener;
import com.doan.cookpad.Interface.HandleLikePosts;
import com.doan.cookpad.Interface.NumberComment;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Comment;
import com.doan.cookpad.Model.CookingSteps;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// Class này có chức năng xem thông tin chi tiết về công thức
public class DetailsPostsActivity extends AppCompatActivity {

    private ImageView img_Image,img_Back,img_Like,img_Bookmark,mDelete;
    private TextView txt_name_Dish,txt_Message,txt_PeopleEating,txt_NameUser,txt_Feelings,txt_Number_Comment,txt_Number_Like,txt_Number_Bookmark;
    private CircleImageView img_PictureUser2,img_CurrentPictureUser;
    private RecyclerView mRecyclerView_Resources;
    private ListResourcesAdapter resourceAdapter;
    private LinearLayout mIntroComment;
    private RecyclerView mRecyclerView_Comment,mRecyclerView_StepsCooking;
    private CommentAdapter commentAdapter;
    private ListStepsCookingAdapter stepsCookingAdapter;
    private ArrayList<Comment> mListComment;
    private ArrayList<CookingSteps> mListSteps;

    private String idPosts,idUser;
    private ArrayList<String> mListResource;
    private Utilities utilities;
    private Account currentAccount;
    private boolean BOOKMARK;
    private Posts mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_posts);

        initView();
        initDataIntent();
        initUI();
        initEvent();
    }

    private void initEvent() {
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIntroComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
                intent.putExtra("posts",idPosts);
                intent.putExtra("user",idUser);
                startActivity(intent);
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailsPostsActivity.this)
                        .setTitle("Xóa Bài Viết")
                        .setMessage("Bạn có muốn xóa công thức "+ mPost.getName_Dish() +" này không ?")
                        .setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Xóa bài viết", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child("TimeLine").child(mPost.getIdPosts()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError==null){
                                            Toast.makeText(getApplicationContext(), "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                            }
                        }).show();
            }
        });
        // Lắng nghe sự kiện người dùng thêm comment mới vào bài viết và hiển thị lên RecyclerView
        FirebaseDatabase.getInstance().getReference().child("Posts").child("Comment").child(idPosts).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    mListComment.add(dataSnapshot.getValue(Comment.class));
                    commentAdapter.notifyDataSetChanged();
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
        currentAccount= utilities.getUserFromLocal(getApplicationContext());

        Glide.with(getApplicationContext()).load(currentAccount.getmPicture()).override(200,200).into(img_CurrentPictureUser);

        mListResource = new ArrayList<>();
        mRecyclerView_Resources.setHasFixedSize(true);
        mRecyclerView_Resources.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        resourceAdapter = new ListResourcesAdapter(getApplicationContext());
        mRecyclerView_Resources.setAdapter(resourceAdapter);

        mListComment = new ArrayList<>();
        mRecyclerView_Comment.setHasFixedSize(true);
        mRecyclerView_Comment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentAdapter = new CommentAdapter(mListComment,getApplicationContext());
        mRecyclerView_Comment.setAdapter(commentAdapter);

        mListSteps = new ArrayList<>();
        mRecyclerView_StepsCooking.setHasFixedSize(true);
        mRecyclerView_StepsCooking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stepsCookingAdapter = new ListStepsCookingAdapter(getApplicationContext());
        mRecyclerView_StepsCooking.setAdapter(stepsCookingAdapter);

        utilities.getPostListener(idPosts, new DetailsPosts() {
            @Override
            public void Success(final Posts posts) {
                if (posts!=null){
                    mPost = posts;
                    idUser = posts.getIdUser();
                    Glide.with(getApplicationContext()).load(posts.getImage_Dish()).into(img_Image);
                    txt_name_Dish.setText(posts.getName_Dish());
                    txt_Message.setText(posts.getWhen_Dish());
                    txt_PeopleEating.setText(posts.getNumber_PeopleEating());

                    utilities.getUserFromID(posts.getIdUser(), new GetAccount() {
                        @Override
                        public void Success(Account account) {
                            if (account!=null){
                                Glide.with(getApplicationContext()).load(account.getmPicture()).into(img_PictureUser2);
                                txt_NameUser.setText(account.getmName());
                                txt_Feelings.setText(account.getmFeelings());
                            }
                        }
                        @Override
                        public void Failure() {

                        }
                    });

                    if (posts.getSteps_Cooking()!=null)
                    stepsCookingAdapter.setNewList(posts.getSteps_Cooking());
                    if (posts.getResources_Dish()!=null)
                    resourceAdapter.setNewList(posts.getResources_Dish());

                    img_Image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),ImageViewerActivity.class);
                            intent.putExtra(Constant.KEY_IMAGE,posts.getImage_Dish());
                            startActivity(intent);
                        }
                    });
                    if (currentAccount.getmID().equals(Constant.ADMIN_ID)|mPost.getIdUser().equals(currentAccount.getmID())){
                        mDelete.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void Failure(String error) {
            }
        });
        utilities.getNumberComment(idPosts, new NumberComment() {
            @Override
            public void Success(int comment) {
                txt_Number_Comment.setText(comment+"");
                if (comment==0){
                    mIntroComment.setVisibility(View.VISIBLE);
                }else {
                    mIntroComment.setVisibility(View.GONE);
                }
            }
            @Override
            public void Failure(String error) {

            }
        });
        utilities.getLikeChangeListener(idPosts, new HandleLikePosts() {
            @Override
            public void onLikeChangeListener(int like) {
                txt_Number_Like.setText(like+"");
            }
            @Override
            public void onFailure(String error) {

            }
        });
        utilities.getLikedListener(currentAccount.getmID(), idPosts, new GetLikedListener() {
            @Override
            public void Success(final boolean liked) {
                if (liked){
                    Glide.with(getApplicationContext()).load(R.drawable.ic_favorite_red_64dp).into(img_Like);
                }else {
                    Glide.with(getApplicationContext()).load(R.drawable.ic_favorite_border_white_24dp).into(img_Like);
                }
                img_Like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liked){
                            utilities.setLikedListener(currentAccount.getmID(),idPosts,false);
                        }else {
                            utilities.setLikedListener(currentAccount.getmID(),idPosts,true);
                        }
                    }
                });
            }
            @Override
            public void Failure(String error) {

            }
        });
        utilities.getBookmarkChangeListener(currentAccount.getmID(), idPosts, new BookmarkChangeListener() {
            @Override
            public void Result(final int number) {
                txt_Number_Bookmark.setText(number + "");
                img_Bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BOOKMARK){
                            utilities.setBookmark(currentAccount.getmID(),idPosts,idUser,false,number);
                        }else {
                            utilities.setBookmark(currentAccount.getmID(),idPosts,idUser,true,number);
                        }
                    }
                });
            }
            @Override
            public void BookmarkExits(boolean exits) {
                BOOKMARK = exits;
                if (exits){
                    Glide.with(getApplicationContext()).load(R.drawable.ic_bookmark_red_64dp).into(img_Bookmark);
                }else {
                    Glide.with(getApplicationContext()).load(R.drawable.ic_bookmark_border_white_24dp).into(img_Bookmark);
                }
            }
            @Override
            public void Failure(String error) {

            }
        });
    }

    private void initDataIntent() {
        idPosts = getIntent().getStringExtra("posts");
    }
    public void Comment(View view){
        Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
        intent.putExtra("posts",idPosts);
        intent.putExtra("user",idUser);
        startActivity(intent);
    }

    private void initView() {
        img_Image = findViewById(R.id.img_Image);
        mDelete = findViewById(R.id.mDelete);
        img_Like = findViewById(R.id.img_Like);
        img_Back = findViewById(R.id.img_Back);
        txt_name_Dish = findViewById(R.id.txt_name_Dish);
        txt_Message = findViewById(R.id.txt_Message);
        txt_PeopleEating = findViewById(R.id.txt_PeopleEating);
        img_PictureUser2 = findViewById(R.id.img_PictureUser2);
        txt_NameUser = findViewById(R.id.txt_NameUser);
        txt_Feelings = findViewById(R.id.txt_Feelings);
        txt_Number_Comment = findViewById(R.id.txt_Number_Comment);
        txt_Number_Like = findViewById(R.id.txt_Number_Like);
        mRecyclerView_Resources = findViewById(R.id.mRecyclerView_Resources);
        mIntroComment = findViewById(R.id.mIntroComment);
        mRecyclerView_Comment = findViewById(R.id.mRecyclerView_Comment);
        img_CurrentPictureUser = findViewById(R.id.img_CurrentPictureUser);
        mRecyclerView_StepsCooking = findViewById(R.id.mRecyclerView_StepsCooking);
        txt_Number_Bookmark = findViewById(R.id.txt_Number_Bookmark);
        img_Bookmark = findViewById(R.id.img_Bookmark);
    }
}
