package com.mu.compet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.data.User;
import com.mu.compet.fragment.SelectImageCheckDialogFragment;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.UpdateProfileRequest;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class UpdateMyProfileActivity extends BaseActivity {

    private static final String TAG = UpdateMyProfileActivity.class.getSimpleName();


    private EditText nickNameEditText;
    private TextView changeImageText;

    private String img_file_path;
    private File mSavedFile, mContentFile;
    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAMERA = 2;

    private File userFile = null;

    private ImageView profileView;

    private User user;

    private SelectImageCheckDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);
        initToolBar(getString(R.string.activity_update_my_profile));

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        nickNameEditText = (EditText) findViewById(R.id.edit_my_nickname);
        if (!TextUtils.isEmpty(user.getUserNick())) {
            nickNameEditText.setText(user.getUserNick());
        }

        changeImageText = (TextView) findViewById(R.id.text_change_image);
        profileView = (ImageView) findViewById(R.id.image_my_profile);
        Glide.with(this).load(user.getImageUrl()).placeholder(R.drawable.image_default_profile).error(R.drawable.image_default_profile)
                .bitmapTransform(new CropCircleTransformation(this)).into(profileView);

        if (savedInstanceState != null) {
            contentUri = Uri.parse(savedInstanceState.getString("media_url"));
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        changeImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();

//                dialogFragment = new SelectImageCheckDialogFragment();
//                dialogFragment.show(getSupportFragmentManager(), "select image");
            }
        });

        Button initButton = (Button) findViewById(R.id.btn_init);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickNameEditText.setText("");
            }
        });
    }

    public void showAlertDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_select_image_dialog, null);
        TextView galleryText = (TextView) view.findViewById(R.id.text_gallery);
        TextView cameraText = (TextView) view.findViewById(R.id.text_camera);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).setCancelable(true).create();
        alertDialog.show();

        galleryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission(v.getContext());
                Intent intent = new Intent(UpdateMyProfileActivity.this, AddImageActivity.class);
                startActivityForResult(intent, RC_GET_IMAGE);
                alertDialog.dismiss();

            }
        });

        cameraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(v.getContext());
                dispatchTakePictureIntent();
                alertDialog.dismiss();

            }
        });
    }


    public void initToolBar(String title) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleText = (TextView) findViewById(R.id.toolbar_title);
        titleText.setText(title);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_not_circle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldNickName = user.getUserNick();
                String newNickName = nickNameEditText.getText().toString();
                if (oldNickName.equals(newNickName)) {
                    // 기존의 NickName 과 변경된 사항이 같을 경우 AlertDialog 를 띄우지 않음.
                    finish();
                } else {
                    alertCancel();
                }
            }
        });
    }

    private void alertCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_not_saved)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 그대로 종료
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_update_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_complete) {
            // 수정사항 변경 완료
            final String userNick = nickNameEditText.getText().toString();

            if (userFile != null) {
                Log.d(TAG, "파일 : " + userFile.getAbsolutePath());
            }
            UpdateProfileRequest request = new UpdateProfileRequest(this, userNick, userFile);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    Log.d("UpdateMyProfileActivity", "성공 : " + result.getMessage());
                    PropertyManager.getInstance().setUserNick(userNick);
                    finish();
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    Log.d("UpdateMyProfileActivity", "실패 : " + errorMessage);

                }

            });

        }
        return super.onOptionsItemSelected(item);
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


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case RC_GET_IMAGE:
                    StringBuilder str = new StringBuilder();
                    if (resultCode == RESULT_OK) {
                        String[] files = intent.getStringArrayExtra("files");
                        for (String s : files) {
                            Log.i("ImageFiles", "files : " + s);
                            str.append(s);
                        }
                        String fileName = str.toString();
                        contentUri = Uri.fromFile(new File(fileName));
                        cropImage(contentUri);
                    }

                    break;
//                case RC_GET_IMAGE:
//                    Log.d("SignUpActivity", "uri : " + contentUri);
//                    contentUri = intent.getData();
                case RC_CAMERA:
                    rotatePhoto();
                    cropImage(contentUri);
                    break;
                case RC_CROP:
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        Bitmap circleBitmap = circleImage(bitmap);
                        profileView.setImageBitmap(circleBitmap);
                        userFile = new File(contentUri.getPath());

                        if (mCurrentPhotoPath != null) {
                            if (userFile.exists()) {
                                userFile.delete();
                            }
//                            mCurrentPhotoPath = null;
                        }
                    }
                    break;
            }
        }
    }

}

