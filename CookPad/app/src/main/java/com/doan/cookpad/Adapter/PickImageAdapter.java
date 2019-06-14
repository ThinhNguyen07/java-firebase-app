package com.doan.cookpad.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.doan.cookpad.CustomView.PickImageFromStorage;
import com.doan.cookpad.R;

import java.util.ArrayList;

public class PickImageAdapter extends RecyclerView.Adapter<PickImageAdapter.ViewHolder>  {

    private ArrayList<String> mListItem;
    private Context mContext;
    private int Measuredwidth = 0;
    private int Measuredheight = 0;
    private PickImageFromStorage.resultImage listener;

    public PickImageAdapter(ArrayList<String> mListItem, Context mContext,WindowManager windowManager) {
        this.mListItem = mListItem;
        this.mContext = mContext;

        Point size = new Point();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            windowManager.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = windowManager.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
    }

    public void setResultListener(PickImageFromStorage.resultImage listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_pick_image,null);
        return new PickImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        int maxWidth = Measuredwidth/3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(maxWidth, maxWidth);
        viewHolder.layout_Container.setLayoutParams(layoutParams);
        Glide.with(mContext).load(mListItem.get(i)).into(viewHolder.img_Image);
        viewHolder.layout_Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResultImage(mListItem.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_Image;
        FrameLayout layout_Container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Image = itemView.findViewById(R.id.img_Image);
            layout_Container = itemView.findViewById(R.id.layout_Container);
        }
    }
}
