package com.mu.compet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.compet.BoardAdapter;
import com.mu.compet.R;
import com.mu.compet.activity.DetailBoardActivity;
import com.mu.compet.activity.DetailUserActivity;
import com.mu.compet.data.Board;
import com.mu.compet.data.BoardView;
import com.mu.compet.data.ListData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.ListBoardRequest;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private BoardAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initToolBar(getString(R.string.app_name), view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                initData();
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new BoardAdapter(0);
        mAdapter.setOnAdapterUserClickListener(new BoardAdapter.OnAdapterUserClickListener() {
            @Override
            public void onAdapterUserClick(BoardAdapter adapter, BoardView view, Board board) {
                Intent intent = new Intent(getContext(), DetailUserActivity.class);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });
        mAdapter.setOnAdapterPostClickListener(new BoardAdapter.OnAdapterPostClickListener() {
            @Override
            public void onAdapterPostClick(BoardAdapter adapter, BoardView view, Board board) {
                Intent intent = new Intent(getContext(), DetailBoardActivity.class);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        listView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment", "onResume 실행");
        onRefresh();
    }

    private void initToolBar(String title, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        TextView titleText = (TextView) view.findViewById(R.id.toolbar_title);
        titleText.setText(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initData() {

        swipeRefreshLayout.setRefreshing(true);
        ListBoardRequest request = new ListBoardRequest(getContext(), "", "");
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                if (result.getData().size() > 0) {
                    Log.d("HomeFragment", "성공 : " + result.getMessage() + " 유저 : " + result.getData().get(0).getUserNick());
                    Log.d("HomeFragment", "성공 : " + result.getMessage() + " 유저 : " + result.getData().get(0).getFirstImageUrl());
                    mAdapter.addAll(result.getData());
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("HomeFragment", "실패 : " + errorMessage);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void upScroll() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        Log.d("HomeFragment", "onRefresh 실행");
        initData();
    }
}
