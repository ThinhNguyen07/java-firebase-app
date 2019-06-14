package com.doan.cookpad.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doan.cookpad.Interface.BookmarkChangeListener;
import com.doan.cookpad.Interface.CheckAccount;
import com.doan.cookpad.Interface.CreateAccount;
import com.doan.cookpad.Interface.DetailsPosts;
import com.doan.cookpad.Interface.FilterFriend;
import com.doan.cookpad.Interface.GetAccount;
import com.doan.cookpad.Interface.GetLikedListener;
import com.doan.cookpad.Interface.HandleLikePosts;
import com.doan.cookpad.Interface.NumberComment;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Bookmark;
import com.doan.cookpad.Model.Comment;
import com.doan.cookpad.Model.Posts;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

// Class này gộp lại tất cả các phương thức có thể tái sử dụng nhiều lần. Ví dụ: đọc dũ liệu của người dùng, lưu dữ liệu của người dùng,....
// Mục đích tạo ra class này vừa nhằm mục đích có thể tái sử dụng nhiều lần, ngoài ra khi đặt các sự kiện lắng nghe ở một đối tượng ngoài activity
// sẽ giúp giúp ứng dụng đạt được hiệu suất cao hơn khi đặt trực tiếp ở activity
public class Utilities {
    private final String KEY_ID = "KEY_ID";
    private final String KEY_NAME = "KEY_NAME";
    private final String KEY_EMAIL = "KEY_EMAIL";
    private final String KEY_PICTURE = "KEY_PICTURE";
    private final String KEY_FEELINGS = "KEY_FEELINGS";
    private final String KEY_POSITION = "KEY_POSITION";

    // Kiểm tra tài khoản có tồn tại hay không thông qua ID
    public void checkAccount(String ID, final CheckAccount account){
        try {
            FirebaseDatabase.getInstance().getReference().child("Account").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        account.Exist(dataSnapshot.getValue(Account.class));
                    }else {
                        account.NotExist();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }catch (NullPointerException error){

        }
    }
    // Lưu dữ liệu của người dùng hiện tại vào SharedPreferences
    public void saveUserInfor(Account account, Context context){
        SharedPreferences preferences = context.getSharedPreferences("Account",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_ID, account.getmID());
        editor.putString(KEY_NAME, account.getmName());
        editor.putString(KEY_EMAIL, account.getmEmail());
        editor.putString(KEY_PICTURE, account.getmPicture());
        editor.putString(KEY_FEELINGS, account.getmFeelings());
        editor.putString(KEY_POSITION, account.getmPosition());
        editor.apply();
    }
    // Kiểm tra xem trạng thái đăng nhập hiện tại.
    // Trả về true nếu đã đăng nhập
    public boolean checkLogin(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Account",Context.MODE_PRIVATE);
        if (preferences.getString(KEY_ID,null)!=null){
            return true;
        }else {
            return false;
        }
    }
    // Đăng xuất tài khoản
    public void logOut(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        SharedPreferences preferences = context.getSharedPreferences("Account",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        if (Profile.getCurrentProfile()!=null){
            LoginManager.getInstance().logOut();
        }

    }
    // Tạo dữ liệu người dùng mới lên Database
    public void CreateNewAccount(String ID, Account account, final CreateAccount createAccount){
        try {
            FirebaseDatabase.getInstance().getReference().child("Account").child(ID).setValue(account, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError==null){
                        createAccount.Success();
                    }else {
                        createAccount.Failure();
                    }
                }
            });
        }catch (NullPointerException error){
            createAccount.Failure();
        }
    }
    // Lấy dữ liệu người dùng đã lưu ở SharedPreferences
    public Account getUserFromLocal(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Account",Context.MODE_PRIVATE);
        Account account = new Account();
        account.setmID(preferences.getString(KEY_ID,null));
        account.setmName(preferences.getString(KEY_NAME,null));
        account.setmEmail(preferences.getString(KEY_EMAIL,null));
        account.setmPicture(preferences.getString(KEY_PICTURE,null));
        account.setmFeelings(preferences.getString(KEY_FEELINGS,null));
        account.setmPosition(preferences.getString(KEY_POSITION,null));
        return account;
    }
    // Lấy dữ liệu người dùng ở Database thông qua ID của người dùng
    public void getUserFromID(String ID, final GetAccount getAccount){
        try {
            FirebaseDatabase.getInstance().getReference().child("Account").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        getAccount.Success(dataSnapshot.getValue(Account.class));
                    }else {
                        getAccount.Success(null);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    getAccount.Failure();
                }
            });
        }catch (NullPointerException error){
            getAccount.Failure();
        }
    }
    // Lắng nghe sự kiện cập nhật và thay đổi trạng thái khi người dùng "Like" bài viết
    public void getLikeChangeListener(final String idPost, final HandleLikePosts handleLikePosts){
        try {
            FirebaseDatabase.getInstance().getReference().child("Posts").child("Like").child(idPost).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        handleLikePosts.onLikeChangeListener(dataSnapshot.getValue(Integer.class));
                    }else {
                        int i = 0;
                        FirebaseDatabase.getInstance().getReference().child("Posts").child("Like").child(idPost).setValue(i);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }catch (NullPointerException error){
            handleLikePosts.onFailure(error.toString());
        }
    }
    // Lưu bài viết vào Bookmark
    public void setBookmark(String currentID, String idPost,String ID_CreatePosts,boolean create,int numberBookmark){
        Bookmark bookmark = new Bookmark();
        bookmark.setIdPosts(idPost);
        bookmark.setIdUser(ID_CreatePosts);
        if (create){
            FirebaseDatabase.getInstance().getReference().child("Bookmark").child(currentID).child(idPost).setValue(bookmark);

            numberBookmark++;
            FirebaseDatabase.getInstance().getReference().child("Posts").child("Bookmark").child(idPost).setValue(numberBookmark);
        }else {
            FirebaseDatabase.getInstance().getReference().child("Bookmark").child(currentID).child(idPost).setValue(null);

            numberBookmark--;
            FirebaseDatabase.getInstance().getReference().child("Posts").child("Bookmark").child(idPost).setValue(numberBookmark);
        }
    }
    // Lắng nghe sự kiện thay đổi trạng thái "Thêm vào Bookmark" hay "Xóa Bookmark" của người dùng
    public void getBookmarkChangeListener(String currentID, final String idPosts, final BookmarkChangeListener listener){
        try {
            FirebaseDatabase.getInstance().getReference().child("Bookmark").child(currentID).child(idPosts).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        listener.BookmarkExits(true);
                    }else {
                        listener.BookmarkExits(false);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.Failure(databaseError.toString());
                }
            });
            FirebaseDatabase.getInstance().getReference().child("Posts").child("Bookmark").child(idPosts).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        listener.Result(dataSnapshot.getValue(Integer.class));
                    }else {
                        int like = 0;
                        FirebaseDatabase.getInstance().getReference().child("Posts").child("Bookmark").child(idPosts).setValue(like);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.Failure(databaseError.toString());
                }
            });
        }catch (NullPointerException error){
            listener.Failure(error.toString());
        }
    }
    // Chuyển từ milisecond sang dạng Date
    public String formatMilisecodToDate(long milisecond){
        long currentMilisecond = System.currentTimeMillis();
        String resultDate = null;

        SimpleDateFormat formatterDays = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterHours = new SimpleDateFormat("hh:mm");

        String CurrentDate = formatterDays.format(currentMilisecond);
        String[] arrayCurrentDays = CurrentDate.split("/");
        int CurrentDay = Integer.valueOf(arrayCurrentDays[0]);
        int CurrentMonth = Integer.valueOf(arrayCurrentDays[1]);
        int CurrentYears = Integer.valueOf(arrayCurrentDays[2]);

        String Date = formatterDays.format(milisecond);
        String[] arrayDays = Date.split("/");
        int Days = Integer.valueOf(arrayDays[0]);
        int Month = Integer.valueOf(arrayDays[1]);
        int Years = Integer.valueOf(arrayDays[2]);

        String CurrentHours = formatterHours.format(currentMilisecond);
        String[] arrayCurrentHours = CurrentHours.split(":");
        int currentHours = Integer.valueOf(arrayCurrentHours[0]);
        int currentMins = Integer.valueOf(arrayCurrentHours[1]);

        String Hours = formatterHours.format(milisecond);
        String[] arrayHours = Hours.split(":");
        int hours = Integer.valueOf(arrayHours[0]);
        int mins = Integer.valueOf(arrayHours[1]);

        // Cùng một ngày
        if (CurrentDay == Days && CurrentMonth == Month && CurrentYears == Years){
            if (currentHours == (hours+1)&& currentMins>mins){
                resultDate = "1 giờ trước";
            }else if (currentHours == (hours+1)&& currentMins<mins){
                resultDate = (currentMins + (60-mins)) + " phút trước";
            }else if (currentHours==hours){
                resultDate = (currentMins-mins) + " phút trước";
            }else {
                resultDate = (currentHours-hours) + " giờ trước";
            }
        }
        // Hơn 1 ngày
        else if (CurrentDay > Days && CurrentMonth == Month && CurrentYears == Years){

            if (CurrentDay==(Days+1)){
                if (currentHours>=hours){
                    resultDate = "1 ngày trước";
                }else {
                    resultDate = (currentHours+ (24-hours)) + " giờ trước";
                }
            }else {
                resultDate = (CurrentDay-Days) + " ngày trước";
            }
        }
        // Hơn 1 tháng
        else if (CurrentMonth > Month && CurrentYears == Years){
            resultDate = (CurrentMonth-Month) + " tháng trước";
        }
        // Hơn 1 năm
        else if (CurrentYears > Years){
            resultDate = (CurrentYears-Years) + " năm trước";
        }
        return resultDate;
    }
    // Lấy tất cả dữ liệu của bài viết theo ID bài viết
    public void getPostListener(String iDPost, final DetailsPosts detailsPosts){
        try {
            FirebaseDatabase.getInstance().getReference().child("Posts").child("TimeLine").child(iDPost).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        detailsPosts.Success(dataSnapshot.getValue(Posts.class));
                    }else {
                        detailsPosts.Success(null);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    detailsPosts.Failure(databaseError.toString());
                }
            });
        }catch (NullPointerException error){
            detailsPosts.Failure(error.toString());
        }
    }
    // Lắng nghe số lượng người dùng đã comment vào bài viết
    public void getNumberComment(String idPost, final NumberComment numberComment){
        final ArrayList arrayList = new ArrayList();
        FirebaseDatabase.getInstance().getReference().child("Posts").child("Comment").child(idPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        arrayList.add(data.getValue(Comment.class));
                    }
                    numberComment.Success(arrayList.size());
                }else {
                    numberComment.Success(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                numberComment.Failure(databaseError.toString());
            }
        });
    }
    // Lắng nghe số lượng "Like" bài viết
    public void getLikedListener(final String iDUser, final String iDPosts, final GetLikedListener getLikedListener){
        FirebaseDatabase.getInstance().getReference().child("Posts").child("Liked").child(iDUser).child(iDPosts).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    getLikedListener.Success(dataSnapshot.getValue(Boolean.class));
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Posts").child("Liked").child(iDUser).child(iDPosts).setValue(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                getLikedListener.Failure(databaseError.toString());
            }
        });
    }
    // Cập nhật số lượng "Like" bài viết và lưu trạng thái "Like" vào người dùng
    public void setLikedListener(final String iDUser, final String iDPosts, final boolean like){
        FirebaseDatabase.getInstance().getReference().child("Posts").child("Like").child(iDPosts).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int liked = dataSnapshot.getValue(Integer.class);
                    if (like){
                        liked++;
                        FirebaseDatabase.getInstance().getReference().child("Posts").child("Liked").child(iDUser).child(iDPosts).setValue(true);
                    }else {
                        liked--;
                        FirebaseDatabase.getInstance().getReference().child("Posts").child("Liked").child(iDUser).child(iDPosts).setValue(false);
                    }
                    FirebaseDatabase.getInstance().getReference().child("Posts").child("Like").child(iDPosts).setValue(liked);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
