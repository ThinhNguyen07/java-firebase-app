package com.doan.cookpad.View;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Lib.PhotoView.PhotoView;
import com.doan.cookpad.R;

import java.util.ArrayList;

// Class này có chức năng xem hình ảnh ở chế độ toàn màn hình và có thể thu phóng hình ảnh
public class ImageViewerActivity extends AppCompatActivity {

    private PhotoView mImageView;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        initView();
        initDataIntent();
        initUI();
    }

    private void initDataIntent() {
        if (getIntent()!=null){
            mPath = getIntent().getStringExtra(Constant.KEY_IMAGE);
        }
    }

    private void initUI() {
        Glide.with(getApplicationContext()).load(mPath).into(mImageView);
    }

    private void initView() {
        mImageView = findViewById(R.id.mImageView);
    }
}
