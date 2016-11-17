package com.mu.compet.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

    private Spinner typeSpinner;
    private EditText keywordInputEditText;
    private Button cancelButton;
    private String type;

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
            callSearchResult("name", keyword);
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

    private void callSearchResult(String type, String keyword) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = SearchResultFragment.newInstance(type, keyword);
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
                        callSearchResult(type, keyword);
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
                        type = typeSpinner.getSelectedItem().toString();
                        break;
                    case 2:
                        type = typeSpinner.getSelectedItem().toString();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            String keyword = String.format("%d%d%d", year, monthOfYear + 1, dayOfMonth);
            keywordInputEditText.setText(keyword);
            callSearchResult(type, keyword);
        }
    };

    public void receiveText(String text) {

        keywordInputEditText.setText(text);
        callSearchResult("name", text);
    }
}
