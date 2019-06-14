package com.doan.cookpad.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Class này có chức năng giúp người dùng đăng nhập bằng Email
public class LoginEmail extends AppCompatActivity {

    private EditText edt_Email,edt_Password;
    private Button btn_Signin;
    private TextView txt_resetPassword;
    private FirebaseAuth mAuth;
    private Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        utilities = new Utilities();

        initView();
        initEvent();
    }

    private void initEvent() {
        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem người dùng đã nhập text trong các edittext hay chưa
                if (TextUtils.isEmpty(edt_Email.getText())|TextUtils.isEmpty(edt_Password.getText())){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    // Lắng nghe kết quả trả về khi đăng nhập bằng Email
                    mAuth.signInWithEmailAndPassword(edt_Email.getText().toString(), edt_Password.getText().toString())
                            .addOnCompleteListener(LoginEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Đăng nhập thành công
                                    if (task.isSuccessful()) {
                                        String mID = task.getResult().getUser().getUid();

                                        // Đăng nhập thành công sẽ lấy dữ liệu của người dùng từ Database về và lưu vào SharePre
                                        utilities.getUserFromID(mID, new GetAccount() {
                                            @Override
                                            public void Success(Account account) {
                                                if (account!=null){
                                                    utilities.saveUserInfor(account,getApplicationContext());
                                                    startActivity(new Intent(getApplicationContext(),HouseActivity.class));
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void Failure() {
                                                Toast.makeText(LoginEmail.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        // Khi lỗi thì task sẽ trả về 2 lỗi thường gặp là "Log" và "Log1"
                                        String Log = "The password is invalid or the user does not have a password.";
                                        String Log1 = "There is no user record corresponding to this identifier. The user may have been deleted.";
                                        // So sánh lỗi ở task trả về trùng với lỗi ở trên và hiện thị hộp thoại phù hợp
                                        if (Log.equals(task.getException().getMessage())){
                                            Toast.makeText(getApplicationContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                        }else if (Log1.equals(task.getException().getMessage())){
                                            new AlertDialog.Builder(LoginEmail.this)
                                                    .setTitle("Không Thể Tìm Thấy Tài Khoản")
                                                    .setMessage("Tài khoản này không tồn tại. Vui lòng đăng ký để sử dụng dịch vụ")
                                                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("Đăng ký", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            startActivity(new Intent(getApplicationContext(),SignupEmail.class));
                                                        }
                                                    }).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        btn_Signin = findViewById(R.id.btn_Signin);
        txt_resetPassword = findViewById(R.id.txt_resetPassword);
    }
}
