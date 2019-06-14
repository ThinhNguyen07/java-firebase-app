package com.doan.cookpad.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Channel;
import com.doan.cookpad.Model.Conversation;
import com.doan.cookpad.Module.ConversationManager.ConversationManager;
import com.doan.cookpad.Module.ConversationManager.GetMessageEndList;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {

    private ArrayList<Channel> mListItem;
    private Context mContext;

    private Utilities utilities;
    private ConversationManager conversationManager;
    private Account account;

    public ConversationListAdapter( Context mContext) {
        this.mContext = mContext;

        mListItem = new ArrayList<>();

        utilities = new Utilities();
        conversationManager = new ConversationManager();
        account = utilities.getUserFromLocal(mContext);
    }

    public void setNewList(ArrayList<Channel> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_conversation_list,null);

        return new ConversationListAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ConversationListAdapter.ViewHolder viewHolder, final int position) {
        final Channel channel = mListItem.get(position);
        viewHolder.mName.setText(channel.getmName());
        Glide.with(mContext).load(channel.getmPictureUser()).override(250,250).into(viewHolder.mAvatar);

        conversationManager.setChannelID(channel.getmChannelID());
        conversationManager.getMessageEndList(new GetMessageEndList() {
            @Override
            public void onComplate(Conversation conversation) {
                if (conversation.getID().equals(account.getmID())){
                    viewHolder.mMessage.setText("Bạn: "+conversation.getMESSAGE());
                }else {
                    viewHolder.mMessage.setText(conversation.getMESSAGE());
                }
                viewHolder.mTime.setText(utilities.formatMilisecodToDate(conversation.getSEND_TIME()));
            }
            @Override
            public void onError(String error) {

            }
        });
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, com.doan.cookpad.View.Conversation.class);
                intent.putExtra(Constant.KEY_ID,channel.getmUserID());
                intent.putExtra(Constant.KEY_NAME,channel.getmName());
                intent.putExtra(Constant.KEY_PICTURE,channel.getmPictureUser());
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

        FrameLayout container;
        TextView mMessage,mName,mTime;
        CircleImageView mAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            mMessage = itemView.findViewById(R.id.mMessage);
            mName = itemView.findViewById(R.id.mName);
            mTime = itemView.findViewById(R.id.mTime);
            mAvatar = itemView.findViewById(R.id.mAvatar);
        }
    }
}
