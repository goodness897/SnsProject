package com.mu.compet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mu.compet.R;
import com.mu.compet.data.User;
import com.mu.compet.data.UserItemData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.LoginRequest;
import com.mu.compet.util.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();


    private long backKeyPressedTime = 0;
    private EditText inputId;
    private EditText inputPassword;
    private Button loginButton;
    private Button signUpButton;
    private TextView passwordVisibleView;

    private String id;
    private String passWord;
    private boolean isShowPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputId = (EditText) findViewById(R.id.edit_id);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signUpButton = (Button) findViewById(R.id.btn_sign_up);
        passwordVisibleView = (TextView) findViewById(R.id.text_view_password);

        inputId.addTextChangedListener(setIdTextWatcher());
        inputPassword.addTextChangedListener(setPasswordTextWatcher());

        passwordVisibleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword();
            }
        });

        id = inputId.getText().toString();
        passWord = inputPassword.getText().toString();


    }


    private void showPassword() {

        if (isShowPass) { // 비밀번호 형식 보인다면
            inputPassword.setInputType(129);
            passwordVisibleView.setText(R.string.Look);
            isShowPass = false;
        } else {
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibleView.setText(R.string.password_hide);
            isShowPass = true;

        }
    }

    private TextWatcher setPasswordTextWatcher() {

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
                if (edit.length() > 0 && inputId.getText().toString().length() > 0) {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.mainRedColor));
                    loginButton.setEnabled(true);

                } else {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    loginButton.setEnabled(false);
                }


            }
        };
        return textWatcher;
    }

    private TextWatcher setIdTextWatcher() {

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
                if (edit.length() > 0 && inputPassword.getText().toString().length() > 0) {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.mainRedColor));
                    loginButton.setEnabled(true);

                } else {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    loginButton.setEnabled(false);
                }


            }
        };
        return textWatcher;
    }

    public void loginButtonClicked(View view) {

        loginRequest();

    }

    public void signUpButtonClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loginRequest() {
        String userId = inputId.getText().toString();
        String userPass = inputPassword.getText().toString();

        if(!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPass)) {
            LoginRequest request = new LoginRequest(this, userId, userPass);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
                @Override
                public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {
                    User user = result.getData();
                    String userNum = String.valueOf(user.getUserNum());
                    String userNick = user.getUserNick();
                    Log.d(TAG, "성공 : " + result.getMessage());
                    PropertyManager.getInstance().setUserNum(userNum);
                    PropertyManager.getInstance().setUserNick(userNick);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFail(NetworkRequest<UserItemData> request, int errorCode, String errorMessage, Throwable e) {
                    Log.d(TAG, "실패 : " + errorMessage);
                    ToastUtil.show(LoginActivity.this, "아이디와 패스워드를 확인하세요");
                }
            });
        } else {
            ToastUtil.show(LoginActivity.this, "값을 입력해주세요");
        }


    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
}
