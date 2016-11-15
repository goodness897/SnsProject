package com.mu.compet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.LeaveUserRequest;
import com.mu.compet.request.LogoutRequest;
import com.mu.compet.request.PasswordCheckRequest;
import com.mu.compet.request.UpdateUserPasswordRequest;

import static com.mu.compet.MyApplication.getContext;

public class SettingActivity extends BaseActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();


    private AlertDialog.Builder builder;
    private EditText newPasswordView;
    private EditText newCheckPasswordView;
    private EditText oldPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar(getString(R.string.activity_setting));

        builder = new AlertDialog.Builder(this);
        Button btn = (Button) findViewById(R.id.btn_change_password);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePasswordDialog();

            }
        });

        btn = (Button) findViewById(R.id.btn_logout);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                builder.setMessage(R.string.logout_message)
                        .setView(null)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                performLogout();
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        btn = (Button) findViewById(R.id.btn_leave);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage(R.string.leave_message)
                        .setView(null)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                performLeave();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void changePasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        builder.setMessage("비밀번호 수정").setView(dialogView);

        oldPasswordView = (EditText) dialogView.findViewById(R.id.edit_old_password);
        newPasswordView = (EditText) dialogView.findViewById(R.id.edit_new_password);
        newCheckPasswordView = (EditText) dialogView.findViewById(R.id.edit_new_password_check);


        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button posButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newPassword = newPasswordView.getText().toString();
                        String newCheckPassword = newCheckPasswordView.getText().toString();
                        if (!TextUtils.isEmpty(newPassword) && newPassword.equals(newCheckPassword)) {
                            passWordCheck();
                            alertDialog.dismiss();

                        } else {
                            Toast.makeText(getContext(), "비밀번호를 확인하세요", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });
    }

    private void passWordCheck() {

        String userPass = oldPasswordView.getText().toString();
        PasswordCheckRequest request = new PasswordCheckRequest(getContext(), userPass);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                changePassword();

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
            }
        });

    }

    private void changePassword() {
        String newUserPass = newPasswordView.getText().toString();

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(getContext(), newUserPass);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                Log.d(TAG, "성공" + result.getMessage());
            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                Log.d(TAG, "실패" + errorMessage);


            }
        });
    }

    private void performLeave() {
        LeaveUserRequest request = new LeaveUserRequest(this);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                Log.d("SettingActivity", "성공 : " + result.getMessage());
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                // 실패
                Log.d("SettingActivity", "실패 : " + errorMessage);

            }
        });
    }

    private void performLogout() {

        LogoutRequest request = new LogoutRequest(this);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                // 성공
                Log.d("SettingActivity", "성공 : " + result.getMessage());

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                // 실패
                Log.d("SettingActivity", "실패 : " + errorMessage);

            }
        });
    }
}

//        btn = (Button) findViewById(R.id.btn_language);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                selectLanguage();
//
//
//            }
//        });


//    private void selectLanguage() {
//
//        View view = getLayoutInflater().inflate(R.layout.view_select_language, null);
//        PopupWindow popup = new PopupWindow(view, Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
//        popup.setContentView(view);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            popup.setWindowLayoutType(WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);
//        }
//        popup.setTouchable(true);
//        popup.setOutsideTouchable(true);
//        popup.setFocusable(true);
//        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popup.showAtLocation(view, Gravity.CENTER, 0, 300);
//
//        TextView koreanText = (TextView) view.findViewById(R.id.text_korean);
//        TextView englishText = (TextView) view.findViewById(R.id.text_english);
//
//        koreanText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                setLocaleCode("ko");
//                finish();
//
//            }
//        });
//        englishText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setLocaleCode("en");
//                finish();
//            }
//        });
//
//    }
