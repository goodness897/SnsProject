package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.ResultMessage;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Created by jeon on 2016-09-04.
 */
public class UpdateUserPasswordRequest extends AbstractRequest<ResultMessage> {
    Request mRequest;

    MediaType jpeg = MediaType.parse("image/jpeg");
    private final static String USER = "user";
    private final static String MODIFY = "modify";
    private final static String USER_PASS = "userPass";
    private final static String USER_NICKNAME = "userNick";
    private final static String USER_FILE = "userFile";

    public UpdateUserPasswordRequest(Context context, String userPass) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(USER)
                .addPathSegment(MODIFY)
                .build();

        MultipartBody.Builder body = new MultipartBody.Builder()
                .addFormDataPart(USER_PASS, userPass);
        MultipartBody requestBody = body.build();

        mRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
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
