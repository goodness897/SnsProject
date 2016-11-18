package com.mu.compet.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mu.compet.R;
import com.mu.compet.activity.SettingActivity;
import com.mu.compet.activity.UpdateMyProfileActivity;
import com.mu.compet.data.User;
import com.mu.compet.data.UserItemData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.DetailUserRequest;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment {

    private FragmentTabHost tabHost;
    private ImageView profileImage;
    private TextView nickNameView;
    private User user;

    private Bundle userBundle;

    private static final String USER_NUM = "userNum";
    private static final String USER_NICK = "userNick";

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(String userNick, String userNum) {

        MyPageFragment fragment = new MyPageFragment();

        Bundle args = new Bundle();
        args.putString(USER_NUM, userNum);
        args.putString(USER_NICK, userNick);
        Log.d("MyPageFragment", "newInstance 유저 닉 : " + userNick + ", 유저 넘 : " + userNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyPageFragment", "onCreate");
        if(getArguments() != null) {
            userBundle = getArguments();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("MyPageFragment", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        initToolBar(getString(R.string.app_name), view);

        profileImage = (ImageView) view.findViewById(R.id.image_profile);
        nickNameView = (TextView) view.findViewById(R.id.text_nickname);
        tabHost = (FragmentTabHost) view.findViewById(R.id.tabHost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1")
                        .setIndicator(getTabIndicator(getContext(), R.drawable.my_list_tab_selector))
                , UserAllPostFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2")
                        .setIndicator(getTabIndicator(getContext(), R.drawable.my_picture_tab_selector))
                , UserOnlyPictureFragment.class, null);


        Button updateProfileButton = (Button) view.findViewById(R.id.btn_update_my_profile);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateMyProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileRequest(PropertyManager.getInstance().getUserNum());

    }

    private void profileRequest(String userNum) {

        DetailUserRequest request = new DetailUserRequest(getContext(), userNum);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
            @Override
            public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {
                Log.d("MyPageFragment", "회원 조회 성공 : " + result.getMessage());

                user = result.getData();
                setUserData(user);

            }

            @Override
            public void onFail(NetworkRequest<UserItemData> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("MyPageFragment", "회원 조회 실패 : " + errorMessage);

            }
        });
    }

    private void setUserData(User user) {
        nickNameView.setText(user.getUserNick());
        Glide.with(this).load(user.getImageUrl()).placeholder(R.drawable.image_default_profile).error(R.drawable.image_default_profile)
                .bitmapTransform(new CropCircleTransformation(getContext())).into(profileImage);
//        profileImage.setImageURI(Uri.parse(user.getImageUrl()));

    }
    private View getTabIndicator(Context context, int res) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(res);
        return view;
    }


    private void initToolBar(String title, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        TextView titleText = (TextView) view.findViewById(R.id.toolbar_title);
        ImageButton settingBtn = (ImageButton) view.findViewById(R.id.btn_setting);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        titleText.setText(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}
