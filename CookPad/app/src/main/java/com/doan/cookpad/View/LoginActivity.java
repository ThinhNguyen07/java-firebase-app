package com.doan.cookpad.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.doan.cookpad.Interface.CheckAccount;
import com.doan.cookpad.Interface.CreateAccount;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// Class này có chức năng giúp người dùng tùy chọn đăng nhập
public class LoginActivity extends AppCompatActivity {

    private FrameLayout login_Facebook,login_Google;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private int REQUEST_CODE_LOGIN_GOOGLE = 155;
    private SharedPreferences preferences;
    private Utilities utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("Account",MODE_PRIVATE);
        utilities = new Utilities();

        login_Check();
        login_Facebook();
        login_Google();
        getKeyHash();
    }

    public void login_Email(View view) {
        startActivity(new Intent(getApplicationContext(),LoginEmail.class));
    }
    public void signup_Email(View view){
        startActivity(new Intent(getApplicationContext(),SignupEmail.class));
    }

    private void login_Google() {
        login_Google = findViewById(R.id.login_Google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        login_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE_LOGIN_GOOGLE);
            }
        });
    }

    // Kiểm tra người dùng đã đăng nhập hay chưa, nếu đã đăng nhập thì sẽ chuyển qua màn hình chính
    private void login_Check() {
        if (utilities.checkLogin(getApplicationContext())){
            startActivity(new Intent(getApplicationContext(),HouseActivity.class));
            finish();
        }
    }

    private void login_Facebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        // Kiểm tra tài khoản đã tồn tại hay chưa
                        utilities.checkAccount(loginResult.getAccessToken().getUserId(), new CheckAccount() {
                            @Override
                            public void NotExist() {
                                // Request đến Graph để lấy thông tin người dùng
                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        loginResult.getAccessToken().getUserId(),
                                        null,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                // Nếu chưa tồn tại sẽ đăng ký
                                                String mID = loginResult.getAccessToken().getUserId();
                                                String mName = null;
                                                String mEmail = null;
                                                try {
                                                    mName = response.getJSONObject().getString("name");
                                                    mEmail = response.getJSONObject().getString("email");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                String mPicture = "https://graph.facebook.com/" +
                                                        loginResult.getAccessToken().getUserId() +
                                                        "/picture?type=large";

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
                                                        Toast.makeText(LoginActivity.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                ).executeAsync();
                            }
                            @Override
                            public void Exist(Account account) {
                                // Nếu tồn tại thì sẽ lưu các thông tin vào bộ nhớ và chuyển tới màn hình chính
                                utilities.saveUserInfor(account,getApplicationContext());
                                startActivity(new Intent(getApplicationContext(),HouseActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Thoát đăng nhập", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Errorrrrrrrr",exception.toString());
                        Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
        login_Facebook = findViewById(R.id.login_Facebook);
        login_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                // Kiểm tra tài khoản đã tồn tại hay chưa
                utilities.checkAccount(account.getId(), new CheckAccount() {
                    @Override
                    public void NotExist() {
                        // Nếu chưa tồn tại sẽ đăng ký
                        String mID = account.getId();
                        String mName = account.getDisplayName();
                        String mEmail = account.getEmail();
                        String mPicture = account.getPhotoUrl().toString();

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
                                Toast.makeText(LoginActivity.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void Exist(Account account) {
                        // Nếu tồn tại thì sẽ lưu các thông tin vào bộ nhớ và chuyển tới màn hình chính
                        utilities.saveUserInfor(account,getApplicationContext());
                        startActivity(new Intent(getApplicationContext(),HouseActivity.class));
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
                Log.d("ttttttttt",e.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void getKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }
}
