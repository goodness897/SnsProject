package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.ResultMessage;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Mu on 2016-10-27.
 */

// 로그아웃(POST)
    // 예시) /logout

public class LogoutRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    private static final String LOGOUT = "logout";


    public LogoutRequest(Context context) {


        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(LOGOUT)
                .build();

        mRequest = new Request.Builder()
                .url(url)
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
