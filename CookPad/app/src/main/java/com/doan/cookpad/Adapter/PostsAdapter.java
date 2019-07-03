package com.doan.cookpad.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Interface.BookmarkChangeListener;
import com.doan.cookpad.Interface.FilterFriend;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Interface.GetLikedListener;
import com.doan.cookpad.Interface.HandleLikePosts;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.doan.cookpad.View.CommentActivity;
import com.doan.cookpad.View.DetailsPostsActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private ArrayList<Posts> mListItem;
    private Context mContext;
    private Utilities utilities;
    private Account currentAccount;
    private boolean VISIBLE_REMOVE,BOOKMARK;

    public PostsAdapter( Context mContext) {
        mListItem = new ArrayList<>();
        this.mContext = mContext;

        utilities = new Utilities();
        currentAccount = utilities.getUserFromLocal(mContext);
    }

    public void setNewList(ArrayList<Posts> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_posts,null);

        return new PostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        Glide.with(mContext).load(mListItem.get(position).getImage_Dish()).into(viewHolder.img_Image);
        viewHolder.txt_CreateTime.setText(utilities.formatMilisecodToDate(mListItem.get(position).getTimeCreate()));
        viewHolder.txt_name_Dish.setText(mListItem.get(position).getName_Dish());
        viewHolder.txt_Message.setText(mListItem.get(position).getWhen_Dish());

        utilities.getUserFromID(mListItem.get(position).getIdUser(), new GetAccount() {
            @Override
            public void Success(Account account) {
                if (account!=null){
                    Glide.with(mContext).load(account.getmPicture()).into(viewHolder.img_PictureUser);
                    viewHolder.txt_NameUser.setText(account.getmName());
                }
            }
            @Override
            public void Failure() {
            }
        });
        utilities.getLikeChangeListener(mListItem.get(position).getIdPosts(), new HandleLikePosts() {
            @Override
            public void onLikeChangeListener(int like) {
                viewHolder.txt_Number_Like.setText(like + " lượt thích");
            }
            @Override
            public void onFailure(String error) {
            }
        });
        utilities.getLikedListener(currentAccount.getmID(), mListItem.get(position).getIdPosts(), new GetLikedListener() {
            @Override
            public void Success(final boolean liked) {
                if (liked){
                    Glide.with(mContext).load(R.drawable.ic_favorite_red_64dp).into(viewHolder.img_Like);
                }else {
                    Glide.with(mContext).load(R.drawable.ic_favorite_border_white_24dp).into(viewHolder.img_Like);
                }
                viewHolder.img_Like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (liked){
                            utilities.setLikedListener(currentAccount.getmID(),mListItem.get(position).getIdPosts(),false);
                        }else {
                            utilities.setLikedListener(currentAccount.getmID(),mListItem.get(position).getIdPosts(),true);
                        }
                    }
                });
            }
            @Override
            public void Failure(String error) {

            }
        });
        utilities.getBookmarkChangeListener(currentAccount.getmID(), mListItem.get(position).getIdPosts(), new BookmarkChangeListener() {
            @Override
            public void Result(final int number) {
                if (number>0){
                    viewHolder.txt_Number_Bookmark.setText(number + " đã lưu");
                }
                viewHolder.img_Bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BOOKMARK){
                            utilities.setBookmark(currentAccount.getmID(), mListItem.get(position).getIdPosts(),mListItem.get(position).getIdUser(),false,number);
                        }else {
                            utilities.setBookmark(currentAccount.getmID(), mListItem.get(position).getIdPosts(),mListItem.get(position).getIdUser(),true,number);
                        }
                    }
                });
            }
            @Override
            public void BookmarkExits(boolean exits) {
                BOOKMARK = exits;
                if (exits){
                    Glide.with(mContext).load(R.drawable.ic_bookmark_red_64dp).into(viewHolder.img_Bookmark);
                }else {
                    Glide.with(mContext).load(R.drawable.ic_bookmark_border_white_24dp).into(viewHolder.img_Bookmark);
                }
            }
            @Override
            public void Failure(String error) {

            }
        });
        // Xóa bài viết
        if (mListItem.get(position).getIdUser().equals(currentAccount.getmID())|currentAccount.getmID().equals(Constant.ADMIN_ID)){
            viewHolder.img_Options.setVisibility(View.VISIBLE);
            viewHolder.img_Options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!VISIBLE_REMOVE){
                        viewHolder.mRemoveLayout.setVisibility(View.VISIBLE);
                        VISIBLE_REMOVE = true;
                        viewHolder.mRemoveLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Xóa Bài Viết")
                                        .setMessage("Bạn có muốn xóa công thức "+ mListItem.get(position).getName_Dish() +" này không ?")
                                        .setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("Xóa bài viết", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseDatabase.getInstance().getReference().child("Posts").child("TimeLine").child(mListItem.get(position).getIdPosts()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                        if (databaseError==null){
                                                            mListItem.remove(position);
                                                            notifyDataSetChanged();
                                                            Toast.makeText(mContext, "Xóa bài viết thành công", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }).show();
                            }
                        });
                    }else {
                        viewHolder.mRemoveLayout.setVisibility(View.GONE);
                        VISIBLE_REMOVE = false;
                    }
                }
            });
        }
        viewHolder.img_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CommentActivity.class);
                intent.putExtra("user",mListItem.get(position).getIdUser());
                intent.putExtra("posts",mListItem.get(position).getIdPosts());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsPostsActivity.class);
                intent.putExtra("posts",mListItem.get(position).getIdPosts());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        if (mListItem.get(position).isAllow_Comment()){
            Glide.with(mContext).load(currentAccount.getmPicture()).into(viewHolder.img_CurrentPictureUser);
            viewHolder.mCommentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,CommentActivity.class);
                    intent.putExtra("user",mListItem.get(position).getIdUser());
                    intent.putExtra("posts",mListItem.get(position).getIdPosts());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }else {
            viewHolder.mCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_PictureUser,img_CurrentPictureUser;
        TextView txt_NameUser,txt_Message,txt_Number_Like,txt_CreateTime,txt_name_Dish,txt_Number_Bookmark;
        ImageView img_Options,img_Like,img_Comment,img_Bookmark,img_Image;
        LinearLayout mCommentLayout,container;
        CardView mRemoveLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_PictureUser = itemView.findViewById(R.id.img_PictureUser);
            txt_NameUser = itemView.findViewById(R.id.txt_NameUser);
            img_Options = itemView.findViewById(R.id.img_Options);
            img_Image = itemView.findViewById(R.id.img_Image);
            img_Like = itemView.findViewById(R.id.img_Like);
            img_Comment = itemView.findViewById(R.id.img_Comment);
            img_Bookmark = itemView.findViewById(R.id.img_Bookmark);
            mCommentLayout = itemView.findViewById(R.id.mCommentLayout);
            txt_Message = itemView.findViewById(R.id.txt_Message);
            mRemoveLayout = itemView.findViewById(R.id.mRemoveLayout);
            txt_Number_Like = itemView.findViewById(R.id.txt_Number_Like);
            img_CurrentPictureUser = itemView.findViewById(R.id.img_CurrentPictureUser);
            txt_CreateTime = itemView.findViewById(R.id.txt_CreateTime);
            txt_name_Dish = itemView.findViewById(R.id.txt_name_Dish);
            container = itemView.findViewById(R.id.container);
            txt_Number_Bookmark = itemView.findViewById(R.id.txt_Number_Bookmark);
        }
    }
}
