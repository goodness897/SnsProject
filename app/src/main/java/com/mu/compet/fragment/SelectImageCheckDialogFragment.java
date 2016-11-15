package com.mu.compet.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.mu.compet.R;
import com.mu.compet.activity.UpdateMyProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectImageCheckDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectImageCheckDialogFragment extends DialogFragment {

    private TextView galleryText;
    private TextView cameraText;

    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAMERA = 2;


    public SelectImageCheckDialogFragment() {
        // Required empty public constructor
    }

    public static SelectImageCheckDialogFragment newInstance() {
        SelectImageCheckDialogFragment fragment = new SelectImageCheckDialogFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_image_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        galleryText = (TextView) view.findViewById(R.id.text_gallery);
        cameraText = (TextView) view.findViewById(R.id.text_camera);


        galleryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                UpdateMyProfileActivity activity = (UpdateMyProfileActivity) getActivity();
                activity.startActivityForResult(intent, RC_GET_IMAGE);

            }
        });

        cameraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateMyProfileActivity activity = (UpdateMyProfileActivity) getActivity();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = activity.getSaveFile();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(intent, RC_CAMERA);
            }
        });

        return view;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


}
