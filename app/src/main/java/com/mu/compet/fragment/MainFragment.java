package com.mu.compet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mu.compet.MyApplication;
import com.mu.compet.R;
import com.mu.compet.manager.PropertyManager;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener {

    private String userNick;
    private String userNum;
    private String clickFragment;

    private Tracker mTracker;

    private static final String HOME = "home";
    private static final String SEARCH = "search";
    private static final String WRITE = "write";
    private static final String MY_PAGE = "mypage";
    private List<ImageButton> imageButtons;
    private static final int[] BUTTONS_ID = {
            R.id.btn_home,
            R.id.btn_search,
            R.id.btn_write,
            R.id.btn_mypage
    };

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        MyApplication application = (MyApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState != null) {
            clickFragment = savedInstanceState.getString("fragment");
        }

        userNick = PropertyManager.getInstance().getUserNick();
        userNum = PropertyManager.getInstance().getUserNum();


        imageButtons = new ArrayList<>();
        for (int id : BUTTONS_ID) {
            ImageButton imageButton = (ImageButton) view.findViewById(id);
            imageButton.setOnClickListener(this);
            imageButtons.add(imageButton);
        }

        init();

        return view;
    }

    private void init() {

        Log.d("test", "ClickFragment : " + clickFragment);

        if (!TextUtils.isEmpty(clickFragment)) {
            switch(clickFragment) {
                case HOME:
                    imageButtons.get(0).setSelected(true);
                    getChildFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(), HOME).commit();
                    break;
                case SEARCH:
                    imageButtons.get(1).setSelected(true);
                    getChildFragmentManager().beginTransaction().replace(R.id.main_container, new SearchFragment(), SEARCH).commit();
                    break;
                case MY_PAGE:
                    imageButtons.get(3).setSelected(true);
                    getChildFragmentManager().beginTransaction().replace(R.id.main_container, new MyPageFragment(), MY_PAGE).commit();
                    break;
            }
        } else {
            imageButtons.get(0).setSelected(true);
            getChildFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(), HOME).commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fragment", clickFragment);
    }

    @Override
    public void onClick(View view) {
        HomeFragment fragment = (HomeFragment) getChildFragmentManager().findFragmentByTag(HOME);
        view.setSelected(true);
        int id = view.getId();
        for(int i = 0; i < imageButtons.size(); i++) {
            if(imageButtons.get(i).getId() == id) {
                imageButtons.get(i).setSelected(true);
            } else {
                imageButtons.get(i).setSelected(false);
            }
        }
        switch (id) {
            case R.id.btn_home:

                clickFragment = HOME;

                mTracker.setScreenName(clickFragment);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());

                if (view.isSelected() && fragment != null) {
                    fragment.upScroll();
                } else {
                    getChildFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(), HOME).commit();
                }
                break;
            case R.id.btn_search:
                clickFragment = SEARCH;
                getChildFragmentManager().beginTransaction().replace(R.id.main_container, new SearchFragment(), SEARCH).commit();
                break;
            case R.id.btn_write:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new WriteFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_mypage:
                clickFragment = MY_PAGE;
                getChildFragmentManager().beginTransaction().replace(R.id.main_container, new MyPageFragment(), MY_PAGE).commit();
                break;

            default:
                clickFragment = HOME;
                getChildFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment(), HOME).commit();
                break;
        }

    }
}