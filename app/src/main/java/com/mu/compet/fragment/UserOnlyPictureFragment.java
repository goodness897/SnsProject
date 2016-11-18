package com.mu.compet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mu.compet.MyPageGridAdapter;
import com.mu.compet.R;
import com.mu.compet.data.Board;
import com.mu.compet.data.FileData;
import com.mu.compet.data.ListData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.ListFileRequest;
import com.mu.compet.request.SearchBoardRequest;

import java.util.List;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOnlyPictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOnlyPictureFragment extends Fragment {

    int loadPage = 9;
    int lastBoardNum = 0;
    private boolean mLockListView;


    private MyPageGridAdapter mAdapter;
    private String userNick;

    public UserOnlyPictureFragment() {
        // Required empty public constructor
    }

    public static UserOnlyPictureFragment newInstance(String userNick) {
        UserOnlyPictureFragment fragment = new UserOnlyPictureFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_page_only_picture, container, false);
        mAdapter = new MyPageGridAdapter(getContext(), R.layout.view_my_page_only_image);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        initData();


        return view;
    }

    private void initData() {

        SearchBoardRequest request = new SearchBoardRequest(getContext(), "9", "", "name", userNick);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {

                Log.d("DetailUserActivity", "게시물 검색 성공 : " + result.getMessage());
                List<Board> list = result.getData();
                for(int i = 0; i < list.size(); i++) {
                    imageRequest(list.get(i));
                }
            }

            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("DetailUserActivity", "실패 : " + errorMessage);


            }
        });

    }

    private void imageRequest(Board board) {
        ListFileRequest request = new ListFileRequest(getContext(), String.valueOf(board.getBoardNum()));
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<FileData>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<FileData>> request, ListData<FileData> result) {

                Log.d(TAG, "파일르스트 성공 : " + result.getMessage());
                insertImage(result.getData());

            }

            @Override
            public void onFail(NetworkRequest<ListData<FileData>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "파일리스트 실패 : " + errorMessage);
            }
        });
    }

    private void insertImage(List<FileData> list) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFileType().startsWith("image") && list.get(i).getFileUrl() != null) {
                mAdapter.add(list.get(i).getFileUrl());
                Log.d(TAG, "파일 : " + list.get(i).getFileUrl());
            }
        }
    }

}
