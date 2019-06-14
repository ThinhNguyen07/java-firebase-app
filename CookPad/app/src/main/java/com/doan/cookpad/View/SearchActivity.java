package com.doan.cookpad.View;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.doan.cookpad.Adapter.SearchAdapter;
import com.doan.cookpad.Interface.LoadSuccess;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.Module.SearchFilter.FilterSearchs;
import com.doan.cookpad.Module.SearchFilter.SearchFilter;
import com.doan.cookpad.R;

import java.util.ArrayList;

// Class này có chức năng lọc cái bài viết công thức theo từ khóa người dùng nhập vào
public class SearchActivity extends AppCompatActivity{

    private EditText edt_Search;
    private SearchFilter searchFilter;
    private ProgressDialog progressDialog;
    private boolean cancelDialog = false;
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initUI();
        initEvent();
    }

    private void initUI() {
        // Khởi tạo đối tượng tùy chỉnh SearchFilter
        searchFilter = new SearchFilter();

        progressDialog = new ProgressDialog(SearchActivity.this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelDialog)
                finish();
            }
        });
        progressDialog.show();

        // Khởi tạo các View RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchAdapter = new SearchAdapter(getApplicationContext());
        mRecyclerView.setAdapter(searchAdapter);
    }

    private void initEvent() {
        // Lắng nghe dữ liệu bài viết đã được lấy về thành công chưa
        searchFilter.getAllPosts(new LoadSuccess() {
            @Override
            public void onComplate(ArrayList<Posts> mListPosts) {
                cancelDialog = true;
                progressDialog.cancel();
                // Nếu người dùng sử dụng Search Voice thì sẽ search dữ liệu ngay
                if (getIntent().getStringExtra("search")!=null){
                    edt_Search.setText(getIntent().getStringExtra("search"));
                    searchFilter.onFilterResource(getIntent().getStringExtra("search"));
                }
            }
        });
        // Thêm dữ liệu tất cả bài viết vào Adapter của RecyclerView
        searchFilter.setOnFilterListener(new FilterSearchs() {
            @Override
            public void onComplate(ArrayList<Posts> mListItem) {
                searchAdapter.setNewList(mListItem);
            }
        });
        // Lắng nghe sự kiện người dùng thay đổi text trên Edittext Search
        edt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra dữ liệu đã được nhập vào edittext hay chưa
                if (!TextUtils.isEmpty(s)){
                    // Nếu đã nhập thì sẽ chạy phương thức lọc dữ liệu theo từ khóa người dùng nhập vào edittext
                    searchFilter.onFilterResource(s.toString());
                }else {
                    // Nếu chưa nhập thì sẽ xóa danh sách trong recyclerview
                    searchAdapter.setClearList();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initView() {
        edt_Search = findViewById(R.id.edt_Search);
        mRecyclerView = findViewById(R.id.mRecyclerView);
    }

    public void onBack(View v){
        finish();
    }
    public void onClear(View view){
        edt_Search.setText("");
    }

}
