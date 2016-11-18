package com.mu.compet.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mu.compet.R;
import com.mu.compet.fragment.MainFragment;
import com.mu.compet.manager.PropertyManager;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FrameLayout frameLayout;
    private long backKeyPressedTime = 0;
    private String userNick;
    private String userNum;
    private MainFragment fragment;

    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTracker();

        userNick = PropertyManager.getInstance().getUserNick();
        userNum = PropertyManager.getInstance().getUserNum();
        Log.d(TAG, "로그인 유저 닉네임 : " + userNick + " 로그인 유저 넘버 : " + userNum);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        fragment = new MainFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, fragment, "main").commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {

        if(fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}
