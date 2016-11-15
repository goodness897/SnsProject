package com.mu.compet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mu.compet.R;
import com.mu.compet.data.Board;
import com.mu.compet.data.User;
import com.mu.compet.data.UserItemData;
import com.mu.compet.fragment.UserAllPostFragment;
import com.mu.compet.fragment.UserOnlyPictureFragment;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.AbstractRequest;
import com.mu.compet.request.DetailUserRequest;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.mu.compet.MyApplication.getContext;

public class DetailUserActivity extends BaseActivity {

    private static final String TAG = DetailUserActivity.class.getSimpleName();

    private TextView nickNameView;
    private TextView postCountView;
    private ImageView profileImageView;
    private String userId;
    private FragmentTabHost tabHost;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Intent intent = getIntent();
        board = (Board)intent.getSerializableExtra("board");

        nickNameView = (TextView) findViewById(R.id.text_nickname);
        postCountView = (TextView) findViewById(R.id.text_post_count);
        profileImageView = (ImageView) findViewById(R.id.image_profile);
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);

        tabHost.setup(getContext(), getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                        .setIndicator(getTabIndicator(getContext(), R.drawable.my_list_tab_selector))
                , UserAllPostFragment.newInstance(board.getUserNick()).getClass(), null);
        tabHost.addTab(tabHost.newTabSpec("tab2")
                        .setIndicator(getTabIndicator(getContext(), R.drawable.my_picture_tab_selector))
                , UserOnlyPictureFragment.newInstance().getClass(), null);

        initData(String.valueOf(board.getUserNum()));

    }

    private View getTabIndicator(Context context, int res) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(res);
        return view;
    }

    private void initData(String userNum) {

        DetailUserRequest request = new DetailUserRequest(this, userNum);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
            @Override
            public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {
                Log.d("DetailUserActivity", "성공 : " + result.getMessage());
                User user = result.getData();
                setUserData(user);
            }
            @Override
            public void onFail(NetworkRequest<UserItemData> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("DetailUserActivity", "성공 : " + errorMessage);

            }
        });
    }

    private void setUserData(User user) {
        String fileUrl = "http://" + AbstractRequest.getHOST() +":" + AbstractRequest.getHttpPort()
                + "/user/" + board.getUserNum() + "/image";
        nickNameView.setText(user.getUserNick());
        initToolBar(user.getUserId());
        if(user.getImageUrl() != null) {
            Log.d("DetailUserActivity", fileUrl);
            Glide.with(profileImageView.getContext()).load(fileUrl).bitmapTransform(new CropCircleTransformation(this)).into(profileImageView);
        }
    }
    
}
