package com.mu.compet.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mu.compet.request.SearchBoardRequest;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {

    private ListView listView;
    private BoardAdapter mAdapter;
    private String keyWord;
    private TextView resultView;


    public SearchResultFragment() {
        // Required empty public constructor
    }

    public static SearchResultFragment newInstance(String type, String keyWord) {

        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();

        args.putString("keyword", keyWord);
        args.putString("searchType", type);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String keyword = getArguments().getString("keyword");
            String type = getArguments().getString("searchType");
            performSearch(type, keyword);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        resultView = (TextView) view.findViewById(R.id.text_result);
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

    private void performSearch(String type, String keyWord) {

        SearchBoardRequest request = new SearchBoardRequest(getContext(), "", "", type, keyWord);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ListData<Board>>() {
            @Override
            public void onSuccess(NetworkRequest<ListData<Board>> request, ListData<Board> result) {
                Log.d("SearchFragment", "성공 : " + result.getMessage());
                if(result.getData().size() > 0) {
                    resultView.setVisibility(View.GONE);
                    mAdapter.addAll(result.getData());
                } else {
                    resultView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFail(NetworkRequest<ListData<Board>> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("SearchFragment", "실패 : " + errorMessage);

            }
        });
    }



}
