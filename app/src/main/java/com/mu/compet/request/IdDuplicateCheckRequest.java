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

// 아이디 중복 확인(GET)
// 예시)/user/checkid/{userId}

public class IdDuplicateCheckRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    private static final String USER = "user";
    private static final String CHECK_ID = "checkid";

    public IdDuplicateCheckRequest(Context context, String userId) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(USER)
                .addPathSegment(CHECK_ID)
                .addPathSegment(userId)
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
