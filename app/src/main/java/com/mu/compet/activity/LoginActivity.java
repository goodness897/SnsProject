package com.mu.compet.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.data.User;
import com.mu.compet.data.UserItemData;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.manager.PropertyManager;
import com.mu.compet.request.IdDuplicateCheckRequest;
import com.mu.compet.request.LoginRequest;
import com.mu.compet.util.ToastUtil;

import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;


    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    private long backKeyPressedTime = 0;
    private EditText inputId;
    private EditText inputPassword;
    private Button loginButton;
    private Button signUpButton;
    private TextView passwordVisibleView;

    private String id;
    private String passWord;
    private boolean isShowPass = false;

    private LoginButton loginFacebook;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();


        getTracker();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        inputId = (EditText) findViewById(R.id.edit_id);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signUpButton = (Button) findViewById(R.id.btn_sign_up);
        passwordVisibleView = (TextView) findViewById(R.id.text_view_password);

        inputId.addTextChangedListener(setIdTextWatcher());
        inputPassword.addTextChangedListener(setPasswordTextWatcher());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = inputId.getText().toString();
                String userPass = inputPassword.getText().toString();
                loginRequest(userId, userPass);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        passwordVisibleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword();
            }
        });

        id = inputId.getText().toString();
        passWord = inputPassword.getText().toString();


        loginFacebook = (LoginButton) findViewById(R.id.login_button);
        loginFacebook.setReadPermissions("email");
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                processAfterFacebookLogin();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void processAfterFacebookLogin() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            String token = accessToken.getToken();
            String email = accessToken.getUserId();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            Intent intent = new Intent(LoginActivity.this, SocialSignUpActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        }
    }


    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Bundle bundle = new Bundle();
            bundle.putString("email", acct.getEmail());
            if (acct.getPhotoUrl() != null) {
                bundle.putString("image", acct.getPhotoUrl().toString());
            }
            duplicateIdCheck(bundle);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    private void duplicateIdCheck(final Bundle bundle) {
        String[] convertEmail = bundle.getString("email").split(Pattern.quote("."));
        final String email = convertEmail[0];


        IdDuplicateCheckRequest request = new IdDuplicateCheckRequest(this, email);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                Intent intent = new Intent(LoginActivity.this, SocialSignUpActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                snsLoginRequest(email, email);
            }
        });

    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
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


    private void snsLoginRequest(String userId, String userPass) {


        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPass)) {
            LoginRequest request = new LoginRequest(this, SNS, userId, userPass);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
                @Override
                public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {

                    loginSuccess(result);
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


    private void loginRequest(String userId, String userPass) {


        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPass)) {
            LoginRequest request = new LoginRequest(this, USER, userId, userPass);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserItemData>() {
                @Override
                public void onSuccess(NetworkRequest<UserItemData> request, UserItemData result) {
                    loginSuccess(result);

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

    private void loginSuccess(UserItemData result) {

        User user = result.getData();
        String userNum = String.valueOf(user.getUserNum());
        String userNick = user.getUserNick();
        String userType = user.getUserType();
        Log.d(TAG, "성공 : " + result.getMessage());
        PropertyManager.getInstance().setUserNum(userNum);
        PropertyManager.getInstance().setUserNick(userNick);
        PropertyManager.getInstance().setUserType(userType);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
