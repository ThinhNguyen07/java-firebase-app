package com.doan.cookpad.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.doan.cookpad.R;

import java.util.ArrayList;

public class EditResourcesFoodAdapter extends RecyclerView.Adapter<EditResourcesFoodAdapter.ViewHolder> {

    private ArrayList<String> mListItem;
    private Context mContext;

    public ArrayList<String> getListItem(){
        ArrayList<String> mList = new ArrayList<>();
        for (String s : mListItem){
            if (!TextUtils.isEmpty(s)){
                mList.add(s);
            }
        }
        return mList;
    }

    public void addListItem(){
        mListItem.add(null);
        notifyDataSetChanged();
    }

    public EditResourcesFoodAdapter( Context mContext) {
        this.mContext = mContext;
        mListItem = new ArrayList<>();
        mListItem.add(null);
    }

    @NonNull
    @Override
    public EditResourcesFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_create_resources,null);
        return new EditResourcesFoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditResourcesFoodAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.edt_Resources.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListItem.set(position,s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        viewHolder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListItem.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText edt_Resources;
        ImageView mRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_Resources = itemView.findViewById(R.id.edt_Resources);
            mRemove = itemView.findViewById(R.id.mRemove);
        }
    }
}