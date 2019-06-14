package com.doan.cookpad.View;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.doan.cookpad.Adapter.FilterConversationAdapter;
import com.doan.cookpad.Constant;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Module.SeachAccount.AccountFilter;
import com.doan.cookpad.Module.SeachAccount.FilterAccount;
import com.doan.cookpad.R;
import com.doan.cookpad.SQLite.SQLite;

import java.util.ArrayList;

// Class này có chức năng lọc các tài khoản ở mục Conversation để tìm kiếm và tham gia cuộc trò chuyện mới
public class FilterAccountConversation extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView mRecyclerView_Filter;
    private FilterConversationAdapter conversationAdapter;
    private ArrayList<Account> mList;
    private ArrayList<Account> mListFilter;
    private SearchView mSearchView;
    private AccountFilter accountFilter;
    private ProgressDialog progressDialog;
    private boolean cancelDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_account_conversation);

        initView();
        initUI();
        initEvent();
    }

    private void initEvent() {
        accountFilter.setFilterAccount(new FilterAccount() {
            @Override
            public void onComplate(ArrayList<Account> mListAccount) {
                mList = mListAccount;
                conversationAdapter.setNewArrayList(mListAccount);
                cancelDialog = true;
                progressDialog.cancel();
            }
            @Override
            public void onSearchComplate(ArrayList<Account> mListAccount) {
                conversationAdapter.setNewArrayList(mListAccount);
            }
        });
    }

    private void initUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black_32dp);

        mRecyclerView_Filter.setHasFixedSize(true);
        mRecyclerView_Filter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView_Filter.setAdapter(conversationAdapter);

        accountFilter = new AccountFilter();

        progressDialog = new ProgressDialog(FilterAccountConversation.this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!cancelDialog)
                    finish();
            }
        });
        progressDialog.show();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView_Filter = findViewById(R.id.mRecyclerView_Filter);
        mList = new ArrayList<>();
        mListFilter = new ArrayList<>();
        conversationAdapter = new FilterConversationAdapter(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter_conversation,menu);
        MenuItem itemSearch = menu.findItem(R.id.mSearch);
        mSearchView = (SearchView) itemSearch.getActionView();
        mSearchView.setQueryHint("Gửi tới:");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!TextUtils.isEmpty(s)){
            accountFilter.onFilter(s);
        }else {
            conversationAdapter.setNewArrayList(mList);
        }

        return true;
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
