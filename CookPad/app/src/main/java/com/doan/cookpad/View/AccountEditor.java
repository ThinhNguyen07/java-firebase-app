package com.doan.cookpad.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doan.cookpad.CustomView.PickImageFromStorage;
import com.doan.cookpad.Model.Account;
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

import de.hdodenhof.circleimageview.CircleImageView;

// Class này có chức năng thay đổi thông tin người dùng
public class AccountEditor extends AppCompatActivity {

    private CircleImageView img_CurrentPictureUser;
    private EditText edt_Name,edt_Email,edt_Position,edt_Feelings;
    private Utilities utilities;
    private Account currentAccount;
    private boolean editAccount = false;
    private String path_Image;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);

        // Ánh xạ view
        initView();
        // Thay đổi các thuộc tính của giao diện
        initUI();
        // Lắng nghe các sự kiện
        initEvent();
    }

    private void initEvent() {
        // Lắng nghe các sự kiện người dùng thay đổi dữ liệu ở edittext (Nếu thay đổi dữ liệu thì sẽ có thể lưu lại dữ liệu ở Button "Lưu").
        edt_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editAccount){
                    editAccount = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // Lắng nghe các sự kiện người dùng thay đổi dữ liệu ở edittext (Nếu thay đổi dữ liệu thì sẽ có thể lưu lại dữ liệu ở Button "Lưu").
        edt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editAccount){
                    editAccount = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // Lắng nghe các sự kiện người dùng thay đổi dữ liệu ở edittext (Nếu thay đổi dữ liệu thì sẽ có thể lưu lại dữ liệu ở Button "Lưu").
        edt_Feelings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editAccount){
                    editAccount = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // Lắng nghe các sự kiện người dùng thay đổi dữ liệu ở edittext (Nếu thay đổi dữ liệu thì sẽ có thể lưu lại dữ liệu ở Button "Lưu").
        edt_Position.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editAccount){
                    editAccount = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initUI() {
        // Tạo một button "Home" ở góc trái toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Gán hình ảnh tùy chỉnh vào button "Home".
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_32dp);

        // Khởi tạo đối tượng Utilities
        utilities = new Utilities();
        // Gán dữ liệu cho đối tượng currentAccount bằng cách dùng phương thức getUserFromLocal để lấy dữ liệu từ SharedPreferences
        currentAccount = utilities.getUserFromLocal(getApplicationContext());

        // Gán đường dẫn mặc định lấy từ SharedPreferences cho path_Image với mục đích khi người dùng cập nhật lại tài khoản mà ko thay đổi ảnh đại diện thì sẽ gán lại ảnh đại diện cũ
        path_Image = currentAccount.getmPicture();

        // Gán các dữ liệu người dùng vào Edittext
        edt_Name.setText(currentAccount.getmName());
        edt_Email.setText(currentAccount.getmEmail());
        edt_Feelings.setText(currentAccount.getmFeelings());
        edt_Position.setText(currentAccount.getmPosition());
        if (currentAccount.getmPicture()!=null) {
            Glide.with(getApplicationContext()).load(currentAccount.getmPicture()).into(img_CurrentPictureUser);
        }else {
            Glide.with(getApplicationContext()).load(R.drawable.ic_logos).into(img_CurrentPictureUser);
        }
    }
    // Ánh xạ các view
    private void initView() {
        img_CurrentPictureUser = findViewById(R.id.img_CurrentPictureUser);
        edt_Name = findViewById(R.id.edt_Name);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Position = findViewById(R.id.edt_Position);
        edt_Feelings = findViewById(R.id.edt_Feelings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    // Sự kiện lắng nghe người dùng Click vào View "Thay đổi ảnh đại diện" sẽ mở một activity mới để chọn 1 hình ảnh từ Album ảnh
    public void UpdateAvatarLayout(View view){
        startActivityForResult(new Intent(getApplicationContext(),PickImageFromStorage.class),123);
    }
    // Sự kiên lắng nghe người dùng Click vào View "Lưu"
    public void UpdateAccount(View view){
        if (editAccount){
            // Khởi tạo đối tượng Account
            Account account = new Account();
            // Gán các dữ liệu được nhập vào edittext vào các thuộc tính của đối tượng Account
            account.setmEmail(edt_Email.getText().toString());
            account.setmFeelings(edt_Feelings.getText().toString());
            account.setmName(edt_Name.getText().toString());
            account.setmPicture(path_Image);
            account.setmPosition(edt_Position.getText().toString());
            account.setmID(currentAccount.getmID());

            // Lưu lại thông tin người dùng
            FirebaseDatabase.getInstance().getReference().child("Account").child(currentAccount.getmID()).setValue(account, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError==null){
                        Toast.makeText(AccountEditor.this, "Thay đổi thông tin thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AccountEditor.this, "Thay đổi thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            utilities.saveUserInfor(account,getApplicationContext());
        }
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

    // Lắng nghe sự kiện trả về hình ảnh khi người dùng chạy phương thức "UpdateAvatarLayout"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        switch (requestCode){
            case 123:
                if (resultCode==RESULT_OK&&data!=null){
                    // Tạo 1 dialog thông báo
                    final ProgressDialog dialog = new ProgressDialog(AccountEditor.this);
                    dialog.setMessage("Đang tải lên hình ảnh...");
                    dialog.setCancelable(false);
                    dialog.show();

                    // Upload hình ảnh lên Firebase Storage
                    InputStream stream;
                    try {
                        stream = new FileInputStream(new File(data.getStringExtra("image")));
                        FirebaseStorage.getInstance().getReference().child("Image").child(String.valueOf(System.currentTimeMillis())+".png").putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                path_Image = taskSnapshot.getDownloadUrl().toString();
                                dialog.cancel();
                                Glide.with(getApplicationContext()).load(path_Image).into(img_CurrentPictureUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi: "+e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
