package com.doan.cookpad.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.CustomView.PickImageFromStorage;
import com.doan.cookpad.Model.CookingSteps;
import com.doan.cookpad.R;
import com.doan.cookpad.View.CreateFood;

import java.util.ArrayList;

public class EditStepsCookingAdapter extends RecyclerView.Adapter<EditStepsCookingAdapter.ViewHolder> implements CreateFood.getImageResult{

    private ArrayList<CookingSteps> mListItem;
    private Context mContext;

    public EditStepsCookingAdapter(Context mContext) {
        this.mContext = mContext;

        mListItem = new ArrayList<>();
        mListItem.add(null);

        ((CreateFood)mContext).setImageResultListener(this);
    }
    public void addItem(){
        mListItem.add(null);
        notifyDataSetChanged();
    }

    public void setNewList(ArrayList<CookingSteps> mListItem){
        this.mListItem = mListItem;
        notifyDataSetChanged();
    }

    public ArrayList<CookingSteps> getListItem(){
        return mListItem;
    }

    @NonNull
    @Override
    public EditStepsCookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_steps_cooking,null);
        return new EditStepsCookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EditStepsCookingAdapter.ViewHolder viewHolder, final int position) {
        try {
            if (!TextUtils.isEmpty(mListItem.get(position).getmImage())){
                Glide.with(mContext).load(mListItem.get(position).getmImage()).into(viewHolder.img_Steps);
            }
        }catch (NullPointerException e){

        }

        viewHolder.txt_Numerical.setText((position+1)+"");

        viewHolder.img_Steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PickImageFromStorage.class);
                intent.putExtra("position", position);
                intent.putExtra("title", viewHolder.edt_Content.getText().toString());
                ((Activity)mContext).startActivityForResult(intent, 321);
            }
        });
        viewHolder.img_DeleteSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListItem.remove(position);
                setNewList(mListItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    @Override
    public void onComplate(String mImageResult, int position, String title) {
        mListItem.set(position,new CookingSteps(title, mImageResult));
        setNewList(mListItem);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_Numerical;
        EditText edt_Content;
        ImageView img_Steps,img_DeleteSteps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Numerical = itemView.findViewById(R.id.txt_Numerical);
            edt_Content = itemView.findViewById(R.id.edt_Content);
            img_Steps = itemView.findViewById(R.id.img_Steps);
            img_DeleteSteps = itemView.findViewById(R.id.img_DeleteSteps);
        }
    }
}
