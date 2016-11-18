package com.mu.compet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.SearchBoardRequest;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserAllPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAllPostFragment extends Fragment implements AbsListView.OnScrollListener {

    int loadPage = 3;
    int lastBoardNum = 0;
    private boolean mLockListView;


    private ListView listView;
    private BoardAdapter mAdapter;
    private TextView noBoardView;
    private View footerView;


    private String userNick;

    public UserAllPostFragment() {
        // Required empty public constructor
    }

    public static UserAllPostFragment newInstance(String userNick) {
        UserAllPostFragment fragment = new UserAllPostFragment();
        Bundle args = new Bundle();
        args.putString("userNick", userNick);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userNick = getArguments().getString("userNick");
        } else {
            userNick = PropertyManager.getInstance().getUserNick();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page_all_post, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer, null);
        footerView.setVisibility(View.GONE);
        listView.addFooterView(footerView);

        listView.setOnScrollListener(this);


        noBoardView = (TextView) view.findViewById(R.id.text_no_board);
        mAdapter = new BoardAdapter(1);
        listView.setAdapter(mAdapter);
        mAdapter.setOnAdapterPostClickListener(new BoardAdapter.OnAdapterPostClickListener() {
            @Override
            public void onAdapterPostClick(BoardAdapter adapter, BoardView view, Board board) {
                Intent intent = new Intent(getContext(), DetailBoardActivity.class);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });
        initData();

        return view;
    }

    private void initData() {

        footerView.setVisibility(View.VISIBLE);
        mLockListView = true;


        SearchBoardRequest request = new SearchBoardRequest(getContext(), String.valueOf(loadPage), "", "name", userNick);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                Log.d("DetailUserActivity", "성공 : " + result.getMessage());
                if(result.getData() != null && result.getData().size() > 0) {
                    noBoardView.setVisibility(View.GONE);
                    mAdapter.addAll(result.getData());
                    lastBoardNum = result.getData().get(result.getData().size()-1).getBoardNum();

                    footerView.setVisibility(View.GONE);
                    mLockListView = false;
                    if(getActivity() instanceof DetailUserActivity) {
                        ((DetailUserActivity) getActivity()).receiveCount(result.getData().size());
                    }
                }
            }


            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("DetailUserActivity", "실패 : " + errorMessage);
                footerView.setVisibility(View.GONE);
                mLockListView = false;

            }
        });

    }

    private void addData() {

        footerView.setVisibility(View.VISIBLE);
        mLockListView = true;
        SearchBoardRequest request = new SearchBoardRequest(getContext(), String.valueOf(loadPage), String.valueOf(lastBoardNum), "name", userNick);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                Log.d("DetailUserActivity", "성공 : " + result.getMessage());
                if(result.getData() != null && result.getData().size() > 0) {
                    noBoardView.setVisibility(View.GONE);
                    mAdapter.addItem(result.getData());
                    lastBoardNum = result.getData().get(result.getData().size()-1).getBoardNum();

                    footerView.setVisibility(View.GONE);
                    mLockListView = false;
                    if(getActivity() instanceof DetailUserActivity) {
                        ((DetailUserActivity) getActivity()).receiveCount(result.getData().size());
                    }
                }
            }


            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("DetailUserActivity", "실패 : " + errorMessage);
                footerView.setVisibility(View.GONE);
                mLockListView = true;

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;

        if(firstVisibleItem >= count && totalItemCount != 0 && !mLockListView) {
            addData();

        }

    }
}
