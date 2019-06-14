package com.doan.cookpad.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doan.cookpad.R;

import java.util.ArrayList;

public class ListResourcesAdapter extends RecyclerView.Adapter<ListResourcesAdapter.ViewHolder> {

    private ArrayList<String> mListItem;
    private Context mContext;

    public ListResourcesAdapter(Context mContext) {
        mListItem = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setNewList(ArrayList<String> mListItem){
        this.mListItem = mListItem;
    }
    @NonNull
    @Override
    public ListResourcesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_resource,null);

        return new ListResourcesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListResourcesAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mResource.setText((position+1) + ": "+mListItem.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mResource;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mResource = itemView.findViewById(R.id.mResource);
        }
    }
}