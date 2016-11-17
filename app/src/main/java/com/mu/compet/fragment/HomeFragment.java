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
import android.widget.AbsListView;
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


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private BoardAdapter mAdapter;
    private boolean mLockListView;
    private View footerView;


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

        mLockListView = false;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        listView = (ListView) view.findViewById(R.id.listView);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, null);

        footerView.setVisibility(View.GONE);

        listView.addFooterView(footerView);
        listView.setOnScrollListener(this);
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
        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment", "onResume 실행");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();

            }
        });
    }

    private void initToolBar(String title, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        TextView titleText = (TextView) view.findViewById(R.id.toolbar_title);
        titleText.setText(title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    int loadPage = 3;
    int lastBoardNum = 0;

    private void initData() {

        footerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        mLockListView = true;
        ListBoardRequest request = new ListBoardRequest(getContext(), String.valueOf(loadPage), "");
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                if (result.getData().size() > 0) {
                    Log.d("HomeFragment", "성공 : " + result.getMessage() + " 유저 : " + result.getData().get(0).getUserNick());
                    Log.d("HomeFragment", "성공 : " + result.getMessage() + " 유저 : " + result.getData().get(0).getFirstImageUrl());
                    mAdapter.addAll(result.getData());
                    lastBoardNum = result.getData().get(result.getData().size()-1).getBoardNum();
                    swipeRefreshLayout.setRefreshing(false);
                    footerView.setVisibility(View.GONE);
                    mLockListView = false;

                }
            }

            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("HomeFragment", "실패 : " + errorMessage);
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                mLockListView = false;

            }
        });
    }

    private void addData() {

        footerView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        mLockListView = true;
        ListBoardRequest request = new ListBoardRequest(getContext(), String.valueOf(loadPage), String.valueOf(lastBoardNum));

        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                swipeRefreshLayout.setRefreshing(false);

                if (result.getData().size() > 0) {
                    Log.d("HomeFragment", "목록 조회 성공 : " + result.getMessage() + " 유저 : " + result.getData().get(0).getUserNick());
                    mAdapter.addItem(result.getData());
                    lastBoardNum = result.getData().get(result.getData().size()-1).getBoardNum();
                    mLockListView = false;
                } else {
                    footerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                mLockListView = true;
                Log.d("HomeFragment", "목록 조회 실패 : " + errorMessage);
                footerView.setVisibility(View.GONE);

            }
        });
    }


    public void upScroll() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        Log.d("HomeFragment", "onRefresh 실행");
        lastBoardNum = 0;
        initData();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    int count = 0;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;

        if(firstVisibleItem >= count && totalItemCount != 0 && !mLockListView) {
            addData();

        }

    }
}
