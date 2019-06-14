package com.doan.cookpad.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.doan.cookpad.Constant;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.SQLite.SQLite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Class này có chức năng là màn hình giới thiệu và xin cấp quyền truy cập bộ nhớ
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_main);

        // Kiểm tra xem người dùng đã cho phép đã cấp phép truy cập vào bộ nhớ hay chưa
        if (hasStoragePermission(123)){
            // Nếu đã cho phép thì sẽ hẹn giờ sau 1 giây sẽ chuyển sang màn hình đăng nhập
            new CountDownTimer(1000,1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    // Mở màn hình mới
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    // Đóng màn hình cũ
                    finish();
                }
            }.start();
        }
    }

    private boolean hasStoragePermission(int requestCode) {
        // Kiểm tra xem version Android trên thiết bị hiện tại có từ Android 6.0 trở lên hay không
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Nếu từ Android 6.0 trở lên thì kiểm tra xem người dùng đã cấp quyền truy cập Đọc/Ghi bộ nhớ chưa
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa sẽ hiển thị hộp thoại xin cấp phép ngay
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    // Lắng nghe kết quả trả về người dùng có cấp phép hay không
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 123){
                // Nếu người dùng cấp phép sẽ chuyển sang màn hình mới sau 1 giây
                new CountDownTimer(1000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }.start();
            }
        }
    }
}
