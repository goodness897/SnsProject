package com.mu.compet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mu.compet.MyPageGridAdapter;
import com.mu.compet.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOnlyPictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOnlyPictureFragment extends Fragment {

    public UserOnlyPictureFragment() {
        // Required empty public constructor
    }

    public static UserOnlyPictureFragment newInstance() {
        UserOnlyPictureFragment fragment = new UserOnlyPictureFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_page_only_picture, container, false);
        MyPageGridAdapter mAdapter = new MyPageGridAdapter(getContext(), R.layout.view_my_page_only_image);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);


        return view;
    }

}
