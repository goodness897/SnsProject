package com.mu.compet.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mu.compet.MyApplication;
import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.IdDuplicateCheckRequest;
import com.mu.compet.request.NewSignUpRequest;
import com.mu.compet.request.NickNameDuplicateCheckRequest;
import com.mu.compet.util.ToastUtil;

import java.io.File;

public class SignUpActivity extends BaseActivity {




    private ImageView imageProfile;
    private ImageView imageCamera;

    private EditText editId;
    private EditText editNickName;
    private EditText editPassword;
    private EditText editPasswordCheck;

    private Button completeButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initToolBar(getString(R.string.activity_sign_up));

        if (savedInstanceState != null) {
            contentUri = Uri.parse(savedInstanceState.getString("media_url"));
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageCamera = (ImageView) findViewById(R.id.image_camera);

        editId = (EditText) findViewById(R.id.edit_id);
        editNickName = (EditText) findViewById(R.id.edit_nickname);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editPasswordCheck = (EditText) findViewById(R.id.edit_password_check);
        editPasswordCheck.addTextChangedListener(setTextWatcher());

        completeButton = (Button) findViewById(R.id.btn_complete);
        imageCamera.bringToFront();


    }

    public void addPictureClicked(View view) {
        // 갤러리를 통한 이미지 가져오기
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RC_GET_IMAGE);
        checkPermission(this);
        Intent intent = new Intent(SignUpActivity.this, AddImageActivity.class);
        startActivityForResult(intent, RC_GET_IMAGE);
    }

    public void addCameraClicked(View view) {
        // 카메라를 통한 이미지 가져오기
        checkPermission(this);
        dispatchTakePictureIntent();

    }

    private TextWatcher setTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String edit = s.toString();
                if (edit.length() > 0 && editId.getText().toString().length() > 0
                        && editNickName.getText().toString().length() > 0
                        && editPasswordCheck.getText().toString().length() > 0) { //활성화
                    completeButton.setBackgroundColor(getResources().getColor(R.color.mainRedColor));
                    completeButton.setEnabled(true);

                } else {
                    completeButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    completeButton.setEnabled(false);
                }
            }
        };
        return textWatcher;
    }

    // SignUp 완료 버튼
    public void loginCompleteSignUp(View view) {

        String userId = editId.getText().toString();
        String userPass = editPassword.getText().toString();
        String userNick = editNickName.getText().toString();
//        if(!TextUtils.isEmpty(mCurrentPhotoPath)) {
//            userFile = new File(mCurrentPhotoPath);
//        }
        if (userFile != null) {
            Log.d("SignUpActivity", "파일 : " + userFile.getAbsolutePath());
        }
        NewSignUpRequest request = new NewSignUpRequest(this, userId, userPass, userNick, userFile);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {

                Log.d("SignUpActivity", "성공 : " + result.getCode());
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("SignUpActivity", "실패 : " + errorCode);


            }
        });


    }

    public void duplicateIdCheckClicked(View view) {
        // id 중복 요청
        String userId = editId.getText().toString();
        if (!TextUtils.isEmpty(userId)) {

            IdDuplicateCheckRequest request = new IdDuplicateCheckRequest(this, userId);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    Toast.makeText(SignUpActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(SignUpActivity.this, "실패", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            ToastUtil.show(this, "아이디를 입력하세요");
        }


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
                    ToastUtil.show(MyApplication.getContext(), "실패");
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
