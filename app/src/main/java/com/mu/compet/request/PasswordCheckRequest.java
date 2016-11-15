package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.ResultMessage;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Mu on 2016-10-27.
 */

// 비밀번호 확인(POST)
// 예시) /user/checkpass

public class PasswordCheckRequest extends AbstractRequest<ResultMessage> {
    Request mRequest;

    private static final String USER = "user";
    private static final String CHECK_PASS = "checkpass";
    private static final String USER_PASS = "userPass";


    public PasswordCheckRequest(Context context, String userPass) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(USER)
                .addPathSegment(CHECK_PASS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add(USER_PASS, userPass)
                .build();


        mRequest = new Request.Builder()
                .url(url)
                .post(body)
                .tag(context)
                .build();

        Log.i("url", mRequest.url().toString());
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultMessage>() {
        }.getType();
    }

    @Override
    public Request getRequest() {
        return mRequest;
    }
}
