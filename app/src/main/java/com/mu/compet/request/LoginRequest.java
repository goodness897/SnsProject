package com.mu.compet.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.UserItemData;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Mu on 2016-10-27.
 */

// 로그인(POST)
//  예시) /login


    // body
    // userId(필수) : 로그인 시도 유저의 아이디
    // userPass(필수) : 로그인 시도 유저의 비밀번호


public class LoginRequest extends AbstractRequest<UserItemData> {

    Request mRequest;

    private static final String LOGIN = "login";
    private static final String USER_ID = "userId";
    private static final String USER_PASS = "userPass";
    private static final String USER_TYPE = "userType";

    public LoginRequest(Context context, String userType, String userId, String userPass) {


        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(LOGIN)
                .build();

        RequestBody body = new FormBody.Builder()
                .add(USER_ID, userId)
                .add(USER_TYPE, userType)
                .add(USER_PASS, userPass)
                .build();

        mRequest = new Request.Builder()
                .url(url)
                .post(body)
                .tag(context)
                .build();

    }

    @Override
    protected Type getType() {
        return new TypeToken<UserItemData>() {
        }.getType();
    }

    @Override
    public Request getRequest() {
        return mRequest;
    }
}
