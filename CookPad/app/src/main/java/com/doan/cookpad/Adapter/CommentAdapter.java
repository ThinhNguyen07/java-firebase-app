package com.doan.cookpad.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Model.Comment;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> mListItem;
    private Context mContext;
    private Utilities utilities;
    private Account currentAccount;
    private boolean REMOVE_COMMENT;

    public CommentAdapter(ArrayList<Comment> mListItem, Context mContext) {
        this.mListItem = mListItem;
        this.mContext = mContext;

        utilities = new Utilities();
        currentAccount = utilities.getUserFromLocal(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_comment,null);

        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        utilities.getUserFromID(mListItem.get(position).getIdUser(), new GetAccount() {
            @Override
            public void Success(Account account) {
                if (account!=null){
                    Glide.with(mContext).load(account.getmPicture()).override(60,60).into(viewHolder.img_PictureUser);
                    viewHolder.txt_NameUser.setText(account.getmName());
                }
            }
            @Override
            public void Failure() {

            }
        });

        viewHolder.txt_Message.setText(mListItem.get(position).getMessage());
        viewHolder.txt_CreateTime.setText(utilities.formatMilisecodToDate(mListItem.get(position).getCreateDate()));

        REMOVE_COMMENT = false;
        if (currentAccount.getmID().equals(mListItem.get(position).getIdUser())){
            viewHolder.img_Options.setVisibility(View.VISIBLE);
            viewHolder.img_Options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!REMOVE_COMMENT){
                        viewHolder.mReportLayout.setVisibility(View.VISIBLE);
                        REMOVE_COMMENT = true;
                    }else {
                        viewHolder.mReportLayout.setVisibility(View.GONE);
                        REMOVE_COMMENT = false;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_PictureUser;
        TextView txt_NameUser,txt_Message,txt_CreateTime;
        ImageView img_Options;
        CardView mReportLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_PictureUser = itemView.findViewById(R.id.img_PictureUser);
            txt_NameUser = itemView.findViewById(R.id.txt_NameUser);
            img_Options = itemView.findViewById(R.id.img_Options);
            txt_Message = itemView.findViewById(R.id.txt_Message);
            txt_CreateTime = itemView.findViewById(R.id.txt_CreateTime);
            mReportLayout = itemView.findViewById(R.id.mReportLayout);
        }
    }
}
