package com.mu.compet.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.mu.compet.R;
import com.mu.compet.fragment.MainFragment;
import com.mu.compet.manager.PropertyManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Tracker mTracker;

    private FrameLayout frameLayout;
    private long backKeyPressedTime = 0;
    private String userNick;
    private String userNum;
    private MainFragment fragment;

    private FragmentManager fm;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String cookie = CookieManager.getInstance().getCookie("http://192.168.6.18:9103/boards?pageNum=&lastBoardNum=");
        Log.d(TAG, "Cookie : " + cookie);
        PropertyManager.getInstance().setKeyCookie(cookie);

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
