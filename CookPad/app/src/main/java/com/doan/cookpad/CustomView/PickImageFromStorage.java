package com.doan.cookpad.CustomView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.doan.cookpad.Adapter.PickImageAdapter;
import com.doan.cookpad.R;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class PickImageFromStorage extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private PickImageAdapter adapter;
    private int position;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image_from_storage);

        initView();
        initData();
        initUI();
    }

    private void initData() {
        position = getIntent().getIntExtra("position",0);
        title = getIntent().getStringExtra("title");
    }

    private void initUI() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_32dp);

        WindowManager windowManager = getWindowManager();
        // Tạo ra 2 loại Pick hình ảnh. 2 Loại 1 sẽ pick duy nhất 1 ảnh, loại 2 sẽ pick đc nhiều ảnh.
        adapter = new PickImageAdapter(getFilePaths(),PickImageFromStorage.this,windowManager);
        adapter.setResultListener(new resultImage() {
            @Override
            public void onResultImage(String result) {
                Intent intent = new Intent();
                intent.putExtra("image", result);
                intent.putExtra("position", position);
                intent.putExtra("title", title);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        toolbar = findViewById(R.id.toolbar);
    }

    public interface resultImage{
        void onResultImage(String result);
    }

    private ArrayList<String> getFilePaths()
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {



                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
