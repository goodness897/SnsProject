package com.mu.compet.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mu.compet.R;
import com.mu.compet.util.ToastUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    Spinner typeSpinner;
    private EditText keywordInputEditText;
    private Button cancelButton;

    private int year, month, day;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyword = keywordInputEditText.getText().toString();
        if (!TextUtils.isEmpty(keyword) && typeSpinner.getSelectedItem().toString().equals("닉네임")) {
            callSearchResult(keyword);
        } else {
            callDefaultSearch();
        }
        typeSpinner.setSelection(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void callSearchResult(String keyword) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = SearchResultFragment.newInstance(keyword);
        ft.replace(R.id.search_container, fragment, "search");
        ft.commit();
    }

    private void callDefaultSearch() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = new DefaultSearchFragment();
        ft.replace(R.id.search_container, fragment, "default");
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        typeSpinner = (Spinner) view.findViewById(R.id.spinner_search_type);
        keywordInputEditText = (EditText) view.findViewById(R.id.edit_search_keyword);
        keywordInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = keywordInputEditText.getText().toString();
                    if (!TextUtils.isEmpty(keyword)) {
                        callSearchResult(keyword);
                        return true;

                    } else {
                        ToastUtil.show(getContext(), "내용을 입력해주세요");
                    }

                }
                return false;
            }
        });

        cancelButton = (Button) view.findViewById(R.id.btn_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordInputEditText.setText("");
                callDefaultSearch();
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        new DatePickerDialog(getContext(), dateSetListener, year, month, day).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String type = typeSpinner.getSelectedItem().toString();
        Log.d("SearchFragment", "타입 : " + type);
        if ("날짜".equals(type)) {
            keywordInputEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch(motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            new DatePickerDialog(getContext(), dateSetListener, year, month, day).show();
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }

        return view;
    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            String msg = String.format("%d . %d . %d", year, monthOfYear + 1, dayOfMonth);
            keywordInputEditText.setText(msg);
        }
    };

    public void receiveText(String text) {

        keywordInputEditText.setText(text);
        callSearchResult(text);
    }
}
