package com.doan.cookpad.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Adapter.EditResourcesFoodAdapter;
import com.doan.cookpad.Adapter.EditStepsCookingAdapter;
import com.doan.cookpad.CustomView.PickImageFromStorage;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

/// Class này có chức năng tạo công thức món ăn mới
public class CreateFood extends AppCompatActivity {

    private ImageView img_Food;
    private EditText edt_NameFood,edt_WhenFood,edt_PeopleEating,edt_TimeCooking;
    private RecyclerView mRecyclerView_Resources,mRecyclerView_StepsCooking;
    private SwitchCompat mSwitch_AllowComment;
    private LinearLayout layout_PickFood;
    private EditResourcesFoodAdapter editResourcesFoodAdapter;
    private EditStepsCookingAdapter editStepsCookingAdapter;

    private String ImageFood;

    private Account account;
    private getImageResult listener;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);

        initView();
        initData();
        initUI();
    }

    private void initData() {
        account = new Utilities().getUserFromLocal(getApplicationContext());
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_32dp);

        mRecyclerView_Resources.setHasFixedSize(true);
        mRecyclerView_Resources.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editResourcesFoodAdapter = new EditResourcesFoodAdapter(getApplicationContext());
        mRecyclerView_Resources.setAdapter(editResourcesFoodAdapter);

        mRecyclerView_StepsCooking.setHasFixedSize(true);
        mRecyclerView_StepsCooking.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editStepsCookingAdapter = new EditStepsCookingAdapter(CreateFood.this);
        mRecyclerView_StepsCooking.setAdapter(editStepsCookingAdapter);
    }

    // Lắng nghe sụ kiện người dùng nhấn vào TEXTVIEW "Xong"
    public void setup_Done(View view){
        // Tạo 1 hộp thoại thông báo
        final ProgressDialog progressDialog = new ProgressDialog(CreateFood.this);
        progressDialog.setMessage("Đang đăng bài viết...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Tạo 1 Key với giá trị là thời gian hiện tại (dạng millisecond)
        String KeyRandom = String.valueOf(System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();

        // Khởi tạo đối tượng tùy chỉnh
        Posts posts = new Posts();
        // Gán dữ liệu vào các trường của đối tượng
        posts.setIdPosts(KeyRandom);
        posts.setIdUser(account.getmID());
        posts.setImage_Dish(ImageFood);
        posts.setName_Dish(edt_NameFood.getText().toString());
        posts.setWhen_Dish(edt_WhenFood.getText().toString());
        posts.setNumber_PeopleEating(edt_PeopleEating.getText().toString());
        posts.setSteps_Cooking(editStepsCookingAdapter.getListItem());
        posts.setResources_Dish(editResourcesFoodAdapter.getListItem());
        posts.setAllow_Comment(mSwitch_AllowComment.isChecked());
        posts.setTime_Cooking(edt_TimeCooking.getText().toString());
        posts.setTimeCreate(currentTime);

        // Gửi bài viết lên Firebase Database
        FirebaseDatabase.getInstance().getReference().child("Posts").child("TimeLine").child(KeyRandom).setValue(posts, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    progressDialog.cancel();
                    Toast.makeText(CreateFood.this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    progressDialog.cancel();
                    Toast.makeText(CreateFood.this, "Đăng bài thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Lắng nghe sự kiện người dùng Click vào LAYOUT "Thêm nguyên liệu"
    public void addResources(View view){
        editResourcesFoodAdapter.addListItem();
    }

    // Lắng nghe sự kiện người dùng Click vào LAYOUT "Bước tiếp theo"
    public void addSteps(View view){
        editStepsCookingAdapter.addItem();
    }
    public interface getImageResult{
        void onComplate(String mImageResult,int position,String title);
    }
    public void setImageResultListener(getImageResult listener){
        this.listener = listener;
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img_Food = findViewById(R.id.img_Food);
        edt_NameFood = findViewById(R.id.edt_NameFood);
        edt_WhenFood = findViewById(R.id.edt_WhenFood);
        edt_PeopleEating = findViewById(R.id.edt_PeopleEating);
        edt_TimeCooking = findViewById(R.id.edt_TimeCooking);
        mRecyclerView_StepsCooking = findViewById(R.id.mRecyclerView_StepsCooking);
        mRecyclerView_Resources = findViewById(R.id.mRecyclerView_Resources);
        mSwitch_AllowComment = findViewById(R.id.mSwitch_AllowComment);
        layout_PickFood = findViewById(R.id.layout_PickFood);
    }

    // Lắng nghe sự kiện người dùng Click vào LAYOUT "Up ảnh thành phẩm"
    public void PickFood(View view){
        startActivityForResult(new Intent(getApplicationContext(),PickImageFromStorage.class),123);
    }

    // Lắng nghe sự kiện trả về dữ liệu của activity khác trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        switch (requestCode){
            case 123:
                if (resultCode==RESULT_OK&&data!=null){
                    // Hiển thị hộp thoại thông báo
                    final ProgressDialog dialog = new ProgressDialog(CreateFood.this);
                    dialog.setMessage("Đang tải lên hình ảnh...");
                    dialog.setCancelable(false);
                    dialog.show();

                    // Đọc đường dẫn file thành InputStream và tải lên FirebaseStorage
                    InputStream stream;
                    try {
                        stream = new FileInputStream(new File(data.getStringExtra("image")));
                        FirebaseStorage.getInstance().getReference().child("Image").child(String.valueOf(System.currentTimeMillis())+".png").putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ImageFood = taskSnapshot.getDownloadUrl().toString();
                                dialog.cancel();
                                layout_PickFood.setVisibility(View.GONE);
                                Glide.with(getApplicationContext()).load(ImageFood).into(img_Food);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateFood.this, "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Toast.makeText(CreateFood.this, "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
                break;
            case 321:
                if (resultCode==RESULT_OK&&data!=null){

                    final ProgressDialog dialog = new ProgressDialog(CreateFood.this);
                    dialog.setMessage("Đang tải lên hình ảnh...");
                    dialog.setCancelable(false);
                    dialog.show();

                    final int position = data.getIntExtra("position",0);
                    final String title = data.getStringExtra("title");

                    InputStream stream;
                    try {
                        stream = new FileInputStream(new File(data.getStringExtra("image")));
                        FirebaseStorage.getInstance().getReference().child("Image").child(String.valueOf(System.currentTimeMillis())+".png").putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.cancel();
                                listener.onComplate(taskSnapshot.getDownloadUrl().toString(),position,title);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateFood.this, "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Toast.makeText(CreateFood.this, "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
