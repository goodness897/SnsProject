package com.mu.compet.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.AddBoardRequest;
import com.mu.compet.util.ToastUtil;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private File mSavedFile, mContentFile;
    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAMERA = 2;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView[] imageViews;
    private ImageButton btnPicture;
    private ImageButton btnCamera;
    private File[] files;

    private EditText editContent;


    public WriteFragment() {
        // Required empty public constructor
    }

    public static WriteFragment newInstance() {
        WriteFragment fragment = new WriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }


    public Uri getSaveFile() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        ), "my_image");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mSavedFile = new File(dir, "my_picture_" + System.currentTimeMillis() + ".jpg");
        return Uri.fromFile(mSavedFile);
    }

    public void initToolBar(final String title, View view) {

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView titleText = (TextView) view.findViewById(R.id.toolbar_title);
        titleText.setText(title);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("정보가 저장 되지 않았습니다. 그대로 끝내시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 그대로 종료
                                finishFragment();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_new_write_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String boardContent = editContent.getText().toString();

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete) {
            AddBoardRequest request = new AddBoardRequest(getContext(), boardContent, files);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    Log.d("NewWriteActivity", "성공 : " + result.getMessage());
                    finishFragment();
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    Log.d("NewWriteActivity", "실패 : " + errorMessage);

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private void finishFragment() {

        getActivity().onBackPressed();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        initToolBar(getString(R.string.activity_new_write), view);
        files = new File[3];
        editContent = (EditText) view.findViewById(R.id.edit_content);

        firstImage = (ImageView) view.findViewById(R.id.image_camera_first);
        secondImage = (ImageView) view.findViewById(R.id.image_camera_second);
        thirdImage = (ImageView) view.findViewById(R.id.image_camera_third);

        btnCamera = (ImageButton) view.findViewById(R.id.image_camera);

        if (count < 3) {
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = getSaveFile();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, RC_CAMERA);
                }
            });
            btnPicture = (ImageButton) view.findViewById(R.id.image_picture);
            btnPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_GET_IMAGE);
                }
            });
        } else {
            ToastUtil.show(getContext(), "더 이상 사진을 넣을 수 없어요");
        }


        imageViews = new ImageView[]{firstImage, secondImage, thirdImage};

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("savedfile");
            if (!TextUtils.isEmpty(path)) {
                mSavedFile = new File(path);
            }
            path = savedInstanceState.getString("contentfile");
            if (!TextUtils.isEmpty(path)) {
                mContentFile = new File(path);
                int dstWidth = 300;
                int dstHeight = 300;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bmImg = BitmapFactory.decodeFile(mContentFile.getAbsolutePath(), options);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap resized = Bitmap.createScaledBitmap(bmImg, dstWidth, dstHeight, true);
                Bitmap rotateBitmap = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(), resized.getHeight(), matrix, true);

                for (int i = 0; i < imageViews.length; i++) {
                    if (imageViews[i].getDrawable() == null) {
                        files[i] = new File(mContentFile.getAbsolutePath());
                        imageViews[i].setImageBitmap(rotateBitmap);
                        break;
                    }
                }
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedFile != null) {
            outState.putString("savedfile", mSavedFile.getAbsolutePath());
        }
        if (mContentFile != null) {
            outState.putString("contentfile", mContentFile.getAbsolutePath());
        }
    }

    int count = 0;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            // 카메라를 통한 이미지 가져오기에 대한 결과
            case RC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    mContentFile = mSavedFile;
                    Log.i("Image", "path : " + mContentFile.getAbsolutePath());
                }

                break;
            // 앨범에서 이미지 가져오기에 대한 결과
            case RC_GET_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri fileUri = intent.getData();
                    Cursor c = getActivity().getContentResolver().query(fileUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    int dstWidth = 300;
                    int dstHeight = 300;
                    if (c.moveToNext()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                        Log.i("Image", "path : " + path);
                        Bitmap bmImg = BitmapFactory.decodeFile(path, options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap resized = Bitmap.createScaledBitmap(bmImg, dstWidth, dstHeight, true);
                        Bitmap rotateBitmap = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(), resized.getHeight(), matrix, true);


                        for (int i = 0; i < imageViews.length; i++) {
                            if (imageViews[i].getDrawable() == null) {
                                if (files.length > 0) {
                                    files[i] = new File(path);
                                    Log.d("NewWriteActivity", files[i].getAbsolutePath());
                                }
                                imageViews[i].setImageBitmap(rotateBitmap);
                                count++;
                                break;
                            }
                        }
                    }
                    break;
                }
        }
    }
}
