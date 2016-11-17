package com.mu.compet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mu.compet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultSearchFragment extends Fragment implements View.OnClickListener {


    public DefaultSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_default_search, container, false);

        String[] dummy = {"박무성", "은미", "이정호", "영훈", "박용범", "명화", "성협", "명지"};
        ViewGroup flowLayout = (ViewGroup) view.findViewById(R.id.flow_container);

        for (int i = 0; i < dummy.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(dummy[i]);
            tv.setBackground(getResources().getDrawable(R.drawable.edit_shape_not_solid));
            tv.setOnClickListener(this);
            flowLayout.addView(tv);
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        TextView tv = (TextView) view;
        Log.d("fragment", "Parent framgnet : " + getParentFragment());
        SearchFragment searchFragment = (SearchFragment)getParentFragment();
        String s = tv.getText().toString();
        searchFragment.receiveText(s);
//        fragment.receiveText(s);

    }
}
