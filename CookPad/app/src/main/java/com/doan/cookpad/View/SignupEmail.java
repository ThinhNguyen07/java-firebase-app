package com.doan.cookpad.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doan.cookpad.Interface.CheckAccount;
import com.doan.cookpad.Interface.CreateAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Class này có chức năng giúp người dùng đăng ký tài khoản mới bằng Email
public class SignupEmail extends AppCompatActivity {

    private EditText edt_Name,edt_Email,edt_Password;
    private SwitchCompat mSwitch;
    private Button btn_Signup;
    private FirebaseAuth mAuth;
    private Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        initView();
        initEvent();
    }

    private void initEvent() {
        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_Name.getText())|TextUtils.isEmpty(edt_Email.getText())|TextUtils.isEmpty(edt_Password.getText())){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(edt_Email.getText().toString(), edt_Password.getText().toString())
                            .addOnCompleteListener(SignupEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        utilities.checkAccount(user.getUid(), new CheckAccount() {
                                            @Override
                                            public void NotExist() {
                                                // Nếu chưa tồn tại sẽ đăng ký
                                                String mID = user.getUid();
                                                String mName = user.getDisplayName();
                                                String mEmail = user.getEmail();
                                                String mPicture = String.valueOf(user.getPhotoUrl());

                                                final Account account1 = new Account();
                                                account1.setmID(mID);
                                                account1.setmName(mName);
                                                account1.setmEmail(mEmail);
                                                account1.setmPicture(mPicture);
                                                account1.setmFeelings(null);
                                                account1.setmPosition(null);
                                                // Đăng ký tài khoản mới
                                                utilities.CreateNewAccount(mID, account1, new CreateAccount() {
                                                    @Override
                                                    public void Success() {
                                                        // Nếu đăng ký thành công thì sẽ lưu các thông tin vào bộ nhớ và chuyển tới màn hình chính
                                                        utilities.saveUserInfor(account1,getApplicationContext());
                                                        startActivity(new Intent(getApplicationContext(),HouseActivity.class));
                                                        finish();
                                                    }
                                                    @Override
                                                    public void Failure() {
                                                        // Đăng ký thất bại
                                                        Toast.makeText(getApplicationContext(), "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void Exist(Account account) {
                                            }
                                        });
                                    } else {
                                        Log.d("pppppppppppp",task.getException().toString());
                                        String Error_Password = "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]";
                                        if (task.getException().toString().equals(Error_Password)){
                                            Toast.makeText(SignupEmail.this, "Mật khẩu không hợp lệ. Phải có ít nhất 6 chữ số", Toast.LENGTH_SHORT).show();
                                        }else {

                                            AlertDialog.Builder aleart = new AlertDialog.Builder(SignupEmail.this)
                                                    .setTitle("Không Thể Đăng Ký")
                                                    .setMessage("Tài khoản đã tồn tại. Vui lòng đăng nhập.")
                                                    .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alertDialog = aleart.create();
                                            alertDialog.show();
                                            // Nếu người dùng tắt dialog thì sẽ chuyển đến màn hình đăng nhập bằng email

                                        }                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        utilities = new Utilities();
        mAuth = FirebaseAuth.getInstance();
        edt_Name = findViewById(R.id.edt_Name);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        mSwitch = findViewById(R.id.mSwitch);
        btn_Signup = findViewById(R.id.btn_Signup);
    }
}
