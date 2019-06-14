package com.doan.cookpad.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Model.CookingSteps;
import com.doan.cookpad.R;
import com.doan.cookpad.View.ImageViewerActivity;

import java.util.ArrayList;

public class ListStepsCookingAdapter extends RecyclerView.Adapter<ListStepsCookingAdapter.ViewHolder> {

    private ArrayList<CookingSteps> mListItem;
    private Context mContext;

    public ListStepsCookingAdapter(Context mContext) {
        this.mContext = mContext;
        mListItem = new ArrayList<>();
    }

    public void setNewList(ArrayList<CookingSteps> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListStepsCookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_steps_cookings,null);
        return new ListStepsCookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListStepsCookingAdapter.ViewHolder viewHolder, final int position) {
        if (!TextUtils.isEmpty(mListItem.get(position).getmImage())){
            viewHolder.img_Steps.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mListItem.get(position).getmImage()).into(viewHolder.img_Steps);
        }
        viewHolder.txt_Numerical.setText((position+1)+"");
        viewHolder.txt_Content.setText(mListItem.get(position).getmTitle());
        viewHolder.img_Steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageViewerActivity.class);
                intent.putExtra(Constant.KEY_IMAGE, mListItem.get(position).getmImage());
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
        TextView txt_Numerical;
        TextView txt_Content;
        ImageView img_Steps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Numerical = itemView.findViewById(R.id.txt_Numerical);
            txt_Content = itemView.findViewById(R.id.txt_Content);
            img_Steps = itemView.findViewById(R.id.img_Steps);
        }
    }
}
