package com.mu.compet.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mu.compet.R;
import com.mu.compet.fragment.SelectImageCheckDialogFragment;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.data.User;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.UpdateProfileRequest;

import java.io.File;

public class UpdateMyProfileActivity extends AppCompatActivity {

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
        user = (User)intent.getSerializableExtra("user");

        nickNameEditText = (EditText) findViewById(R.id.edit_my_nickname);
        if(!TextUtils.isEmpty(user.getUserNick())){
            nickNameEditText.setText(user.getUserNick());
        }

        changeImageText = (TextView) findViewById(R.id.text_change_image);
        profileView = (ImageView) findViewById(R.id.image_my_profile);

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("savedfile");
            if (!TextUtils.isEmpty(path)) {
                mSavedFile = new File(path);
            }
            path = savedInstanceState.getString("contentfile");
            if (!TextUtils.isEmpty(path)) {
                mContentFile = new File(path);
                int dstWidth = 200;
                int dstHeight = 200;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bmImg = BitmapFactory.decodeFile(mContentFile.getAbsolutePath(), options);
                Bitmap resized = Bitmap.createScaledBitmap(bmImg, dstWidth, dstHeight, true);
                profileView.setImageBitmap(resized);
            }
        }


        changeImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.fragment_select_image_dialog, null);
                PopupWindow popup = new PopupWindow(view, Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
                popup.setContentView(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setWindowLayoutType(WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);
                }
                popup.setTouchable(true);
                popup.setOutsideTouchable(true);
                popup.setFocusable(true);
                popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popup.showAtLocation(view, Gravity.CENTER, 0, 300);

                TextView galleryText = (TextView) view.findViewById(R.id.text_gallery);
                TextView cameraText = (TextView) view.findViewById(R.id.text_camera);


                galleryText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RC_GET_IMAGE);

                    }
                });

                cameraText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri uri = getSaveFile();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, RC_CAMERA);
                    }
                });
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

    private void initToolBar(String title) {

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
                if(oldNickName.equals(newNickName)){
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
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case RC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    mContentFile = mSavedFile;
                    mContentFile = userFile;
                    Log.i("Image", "path : " + mContentFile.getAbsolutePath());
                }
                break;

            case RC_GET_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    dialogFragment.dismiss();
                    Uri fileUri = intent.getData();
                    Cursor c = getContentResolver().query(fileUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    int dstWidth = 200;
                    int dstHeight = 200;
                    if (c.moveToNext()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                        userFile = new File(path);
                        Bitmap bmImg = BitmapFactory.decodeFile(path, options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap resized = Bitmap.createScaledBitmap(bmImg, dstWidth, dstHeight, true);
                        Bitmap rotateBitmap = Bitmap.createBitmap(resized, 0, 0, resized.getWidth(), resized.getHeight(), matrix, true);
                        profileView.setImageBitmap(rotateBitmap);
                        Log.i("Image", "path : " + path);
                    }
                    break;
                }
        }
    }

}

