package com.doan.cookpad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Interface.DetailsPosts;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Bookmark;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.doan.cookpad.View.DetailsPostsActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<Posts> mListItem;
    private Context mContext;
    private Utilities utilities;

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
        mListItem = new ArrayList<>();
        utilities = new Utilities();
    }

    public void setNewList(ArrayList<Posts> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }
    public void setClearList(){
        mListItem.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_search,null);

        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ViewHolder viewHolder, final int position) {
        utilities.getUserFromID(mListItem.get(position).getIdUser(), new GetAccount() {
            @Override
            public void Success(Account account) {
                if (account!=null){
                    viewHolder.txt_NameUser.setText("của "+account.getmName());
                    Glide.with(mContext).load(account.getmPicture()).override(120,120).into(viewHolder.img_PictureUser);
                }
            }
            @Override
            public void Failure() {
            }
        });
        Glide.with(mContext).load(mListItem.get(position).getImage_Dish()).into(viewHolder.img_Image);
        viewHolder.txt_name_Dish.setText(mListItem.get(position).getName_Dish());
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsPostsActivity.class);
                intent.putExtra("posts",mListItem.get(position).getIdPosts());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_Image;
        TextView txt_name_Dish,txt_NameUser;
        CircleImageView img_PictureUser;
        CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_Image = itemView.findViewById(R.id.img_Image);
            txt_name_Dish = itemView.findViewById(R.id.txt_name_Dish);
            img_PictureUser = itemView.findViewById(R.id.img_PictureUser);
            txt_NameUser = itemView.findViewById(R.id.txt_NameUser);
            container = itemView.findViewById(R.id.container);
        }
    }
}

