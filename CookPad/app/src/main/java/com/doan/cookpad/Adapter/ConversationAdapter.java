package com.doan.cookpad.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Conversation;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private ArrayList<Conversation> mListItem;
    private Context mContext;
    private Utilities utilities;
    private Account account;
    private String ID_Current;
    private final int CODE_SEND_MESSAGE = 1;
    private final int CODE_RECEIVE_MESSAGE = 2;

    public ConversationAdapter(ArrayList<Conversation> mListItem,Context mContext) {
        this.mContext = mContext;
        this.mListItem = mListItem;
        utilities = new Utilities();
        account = utilities.getUserFromLocal(mContext);
        ID_Current = account.getmID();
    }

    @NonNull
    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        if (viewType==CODE_SEND_MESSAGE){
            view = LayoutInflater.from(mContext).inflate(R.layout.row_send_message,viewGroup,false);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_receive_message,viewGroup,false);
        }
        return new ConversationAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (mListItem.get(position).getID().equals(ID_Current)){
            return CODE_SEND_MESSAGE;
        }else {
            return CODE_RECEIVE_MESSAGE;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ConversationAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mMessage.setText(mListItem.get(position).getMESSAGE());
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.mMessage);
        }
    }

}
