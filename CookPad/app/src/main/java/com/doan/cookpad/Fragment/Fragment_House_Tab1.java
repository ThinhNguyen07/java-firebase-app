package com.doan.cookpad.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.doan.cookpad.Adapter.PostsAdapter;
import com.doan.cookpad.Interface.LoadSuccess;
import com.doan.cookpad.Model.Posts;
import com.doan.cookpad.Module.SearchFilter.SearchFilter;
import com.doan.cookpad.R;
import com.doan.cookpad.View.SearchActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Fragment_House_Tab1 extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private FrameLayout mSearchLayout;
    private ImageView img_SearchVoice;
    private PostsAdapter postsAdapter;
    private ArrayList<Posts> mListPosts;
    private int REQUEST_CODE_SEARCH_VOICE = 113;

    public Fragment_House_Tab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_tab1, container, false);

        initView();
        initArrayList();
        initUI();
        initEvent();

        return view;
    }

    private void initArrayList() {
        mListPosts = new ArrayList<>();
    }

    private void initUI() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsAdapter = new PostsAdapter(getActivity());
        mRecyclerView.setAdapter(postsAdapter);
    }

    private void initEvent() {
        img_SearchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        mSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.getAllPosts(new LoadSuccess() {
            @Override
            public void onComplate(ArrayList<Posts> mListPost) {
                ArrayList<Posts> list = mListPost;
                Collections.reverse(list);
                postsAdapter.setNewList(list);
            }
        });
    }

    private void initView() {
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mSearchLayout = view.findViewById(R.id.mSearchLayout);
        img_SearchVoice = view.findViewById(R.id.img_SearchVoice);
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó");
        try {
            startActivityForResult(intent, REQUEST_CODE_SEARCH_VOICE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    "Xin lỗi!! Thiết bị của bạn không hỗ trợ chức năng này",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_SEARCH_VOICE&&resultCode == RESULT_OK && null != data){
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Intent intent = new Intent(getActivity(),SearchActivity.class);
            intent.putExtra("search",result.get(0));
            startActivity(intent);
        }
    }
}
