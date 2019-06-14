package com.doan.cookpad.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.doan.cookpad.Adapter.ConversationListAdapter;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.Model.Channel;
import com.doan.cookpad.Module.ConversationManager.ChannelList;
import com.doan.cookpad.Module.ConversationManager.FilterConversation;
import com.doan.cookpad.Module.ConversationManager.ListConversationFilter;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.doan.cookpad.View.FilterAccountConversation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_House_Tab4 extends Fragment{

    private View view;
    private FrameLayout mIntroLayout,mIntroConversation;
    private LinearLayout mSearchLayout;
    private ImageView img_Search,img_CloseSearch,img_CreateConversation;
    private EditText edt_Search;
    private RecyclerView mRecyclerView_Conversation;
    private ConversationListAdapter conversationListAdapter;
    private ArrayList<Channel> mList;
    private Account account;
    private ListConversationFilter conversationFilter;

    private boolean OpenSearch = false;

    public Fragment_House_Tab4() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_tab4, container, false);

        initView();
        initUI();
        initEvent();

        return view;
    }

    private void initUI() {
        mRecyclerView_Conversation.setHasFixedSize(true);
        mRecyclerView_Conversation.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_Conversation.setAdapter(conversationListAdapter);

        conversationFilter = new ListConversationFilter();
    }

    private void initEvent() {
        conversationFilter.setChannelListener(new ChannelList() {
            @Override
            public void onSuccess(ArrayList<Channel> mListChannel) {
                mList = mListChannel;
                conversationListAdapter.setNewList(mListChannel);
            }
        });
        conversationFilter.getAllConversation(account.getmID());
        conversationFilter.setOnFilterListener(new FilterConversation() {
            @Override
            public void onSuccess(ArrayList<Channel> mListChannel) {
                conversationListAdapter.setNewList(mListChannel);
            }
        });

        edt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    conversationFilter.onFilter(s.toString());
                }else {
                    conversationListAdapter.setNewList(mList);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        img_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!OpenSearch){
                    mSearchLayout.setVisibility(View.VISIBLE);
                    mIntroLayout.setVisibility(View.GONE);
                    OpenSearch = true;
                }
            }
        });
        img_CloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OpenSearch){
                    mSearchLayout.setVisibility(View.GONE);
                    mIntroLayout.setVisibility(View.VISIBLE);
                    OpenSearch = false;
                    edt_Search.setText("");
                }
            }
        });
        img_CreateConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FilterAccountConversation.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        Utilities utilities = new Utilities();
        account = utilities.getUserFromLocal(getActivity());
        mIntroLayout = view.findViewById(R.id.mIntroLayout);
        mSearchLayout = view.findViewById(R.id.mSearchLayout);
        img_Search = view.findViewById(R.id.img_Search);
        mIntroConversation = view.findViewById(R.id.mIntroConversation);
        img_CloseSearch = view.findViewById(R.id.img_CloseSearch);
        edt_Search = view.findViewById(R.id.edt_Search);
        img_CreateConversation = view.findViewById(R.id.img_CreateConversation);
        mRecyclerView_Conversation = view.findViewById(R.id.mRecyclerView_Conversation);
        mList = new ArrayList<>();
        conversationListAdapter = new ConversationListAdapter(getActivity());
    }
}
