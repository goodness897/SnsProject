package com.mu.compet.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mu.compet.MyApplication;
import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.data.User;
import com.mu.compet.data.UserItemData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.LoginRequest;
import com.mu.compet.request.NewSignUpRequest;
import com.mu.compet.request.NickNameDuplicateCheckRequest;
import com.mu.compet.util.ToastUtil;

import java.io.File;
import java.util.regex.Pattern;

public class SocialSignUpActivity extends BaseActivity {

    private static final String TAG = SocialSignUpActivity.class.getSimpleName();


    private ImageView imageProfile;
    private ImageView imageCamera;

    private EditText editNickName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_sign_up);
        initToolBar(getString(R.string.activity_sign_up));

        Button completeButton = (Button)findViewById(R.id.btn_complete);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        if (savedInstanceState != null) {
            contentUri = Uri.parse(savedInstanceState.getString("media_url"));
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageCamera = (ImageView) findViewById(R.id.image_camera);

        editNickName = (EditText) findViewById(R.id.edit_nickname);

        final String[] email = bundle.getString("email").split(Pattern.quote("."));
        final String userId = email[0];
        final String userUrl = bundle.getString("image");
        Glide.with(this).load(userUrl).placeholder(R.drawable.image_default_profile).error(R.drawable.image_default_profile)
                .into(imageProfile);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNick = editNickName.getText().toString();
                if (userFile != null) {
                    Log.d(TAG, "파일 : " + userFile.getAbsolutePath());
                }
                signUpRequest(userId, userId, userNick, userFile);
            }
        });
        completeButton.setEnabled(true);
        imageCamera.bringToFront();


    }

    private void signUpRequest(final String userId, final String userPass, String userNick, File userFile) {
        NewSignUpRequest request = new NewSignUpRequest(MyApplication.getContext(), SNS, userId, userId, userNick, userFile);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {

                Log.d(TAG, "회원 가입 성공 : " + result.getMessage());
                loginRequest(userId, userPass);
            }
            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "가입 실패 : " + errorCode);
            }
        });
    }

    private void loginRequest(String userId, String userPass) {

        LoginRequest request = new LoginRequest(this, SNS, userId, userPass);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
            @Override
            public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {

                loginSuccess(result);
            }

            @Override
            public void onFail(NetworkRequest<UserItemData> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "로그인 실패 : " + errorMessage);
            }
        });
    }

    private void loginSuccess(UserItemData result) {

        User user = result.getData();
        String userNum = String.valueOf(user.getUserNum());
        String userNick = user.getUserNick();
        String userType = user.getUserType();
        Log.d(TAG, "로그인 성공 : " + result.getMessage());
        PropertyManager.getInstance().setUserNum(userNum);
        PropertyManager.getInstance().setUserNick(userNick);
        PropertyManager.getInstance().setUserType(userType);
        Intent intent = new Intent(SocialSignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addPictureClicked(View view) {
        // 갤러리를 통한 이미지 가져오기
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RC_GET_IMAGE);
        checkPermission(this);
        Intent intent = new Intent(SocialSignUpActivity.this, AddImageActivity.class);
        startActivityForResult(intent, RC_GET_IMAGE);
    }

    public void addCameraClicked(View view) {
        // 카메라를 통한 이미지 가져오기
        checkPermission(this);
        dispatchTakePictureIntent();

    }

    public void duplicateNickNameCheckClicked(View view) {
        // nickname 중복 요청

        String nickName = editNickName.getText().toString();
        if (!TextUtils.isEmpty(nickName)) {
            NickNameDuplicateCheckRequest request = new NickNameDuplicateCheckRequest(this, nickName);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    ToastUtil.show(MyApplication.getContext(), "사용가능한 닉네임입니다.");
                }
                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    ToastUtil.show(MyApplication.getContext(), "이미 존재하는 닉네임입니다.");
                }
            });
        } else {
            ToastUtil.show(this, "닉네임을 입력하세요");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case RC_GET_IMAGE:
                    StringBuilder str = new StringBuilder();
                        String[] files = intent.getStringArrayExtra("files");
                        for (String s : files) {
                            Log.i("ImageFiles", "files : " + s);
                            str.append(s);
                        }
                        String fileName = str.toString();
                        contentUri = Uri.fromFile(new File(fileName));
                        cropImage(contentUri);

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
                        imageProfile.setImageBitmap(circleBitmap);

                        if (mCurrentPhotoPath != null) {
                            userFile = new File(mCurrentPhotoPath);
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
