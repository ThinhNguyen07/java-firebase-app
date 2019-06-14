package com.doan.cookpad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterConversationAdapter extends RecyclerView.Adapter<FilterConversationAdapter.ViewHolder> {

    private ArrayList<Account> mListItem;
    private Context mContext;

    public FilterConversationAdapter( Context mContext) {
        mListItem = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setNewArrayList(ArrayList<Account> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilterConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_filter_conversation,null);

        return new FilterConversationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterConversationAdapter.ViewHolder viewHolder, final int position) {
        final Account account = mListItem.get(position);

        viewHolder.txt_NameUser.setText(account.getmName());
        Glide.with(mContext).load(account.getmPicture()).into(viewHolder.img_PictureUser);
        if (!TextUtils.isEmpty(account.getmFeelings())){
            viewHolder.txt_Feelings.setText(account.getmFeelings());
        }else {
            viewHolder.txt_Feelings.setVisibility(View.GONE);
        }
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, com.doan.cookpad.View.Conversation.class);
                intent.putExtra(Constant.KEY_ID,account.getmID());
                intent.putExtra(Constant.KEY_NAME,account.getmName());
                intent.putExtra(Constant.KEY_PICTURE,account.getmPicture());
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

        TextView txt_NameUser,txt_Feelings;
        CircleImageView img_PictureUser;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_PictureUser = itemView.findViewById(R.id.img_PictureUser);
            txt_NameUser = itemView.findViewById(R.id.txt_NameUser);
            container = itemView.findViewById(R.id.container);
            txt_Feelings = itemView.findViewById(R.id.txt_Feelings);
        }
    }
}
