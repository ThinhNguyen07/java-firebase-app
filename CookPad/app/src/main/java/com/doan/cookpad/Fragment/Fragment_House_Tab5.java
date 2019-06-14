package com.doan.cookpad.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doan.cookpad.Model.Account;
import com.doan.cookpad.R;
import com.doan.cookpad.Utilities.Utilities;
import com.doan.cookpad.View.AccountEditor;
import com.doan.cookpad.View.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_House_Tab5 extends Fragment {

    private View view;
    private CircleImageView img_CurrentPictureUser;
    private TextView txt_CurrentNameUser,txt_EditAccount;
    private ImageView img_Logout;
    private Utilities utilities;
    private Account currentAccount;

    public Fragment_House_Tab5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_house_tab5, container, false);

        initView();
        initUI();
        initEvent();

        return view;
    }

    private void initEvent() {
        txt_EditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AccountEditor.class));
            }
        });
        img_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Đăng Xuất")
                        .setMessage("Bạn có muốn đăng xuất tài khoản hiện tại không ?")
                        .setPositiveButton("Trở lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                utilities.logOut(getActivity());
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }
                        }).show();
            }
        });
    }

    private void initUI() {
        utilities = new Utilities();

        currentAccount = utilities.getUserFromLocal(getActivity());
        setProfile();
    }

    private void initView() {
        img_CurrentPictureUser = view.findViewById(R.id.img_CurrentPictureUser);
        txt_CurrentNameUser = view.findViewById(R.id.txt_CurrentNameUser);
        txt_EditAccount = view.findViewById(R.id.txt_EditAccount);
        img_Logout = view.findViewById(R.id.img_Logout);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentAccount = utilities.getUserFromLocal(getActivity());
        setProfile();
    }
    private void setProfile(){
        Glide.with(getActivity()).load(currentAccount.getmPicture()).into(img_CurrentPictureUser);
        txt_CurrentNameUser.setText(currentAccount.getmName());
    }
}
