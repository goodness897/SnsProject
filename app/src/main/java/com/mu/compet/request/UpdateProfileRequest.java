package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.ResultMessage;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jeon on 2016-09-04.
 */
public class UpdateProfileRequest extends AbstractRequest<ResultMessage> {
    Request mRequest;

    MediaType jpeg = MediaType.parse("image/jpeg");
    private final static String USER = "user";
    private final static String MODIFY = "modify";
    private final static String USER_NICKNAME = "userNick";
    private final static String USER_FILE = "userFile";

    public UpdateProfileRequest(Context context, String userNick, File userFile) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(USER)
                .addPathSegment(MODIFY)
                .build();

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(USER_NICKNAME, userNick);
        if (userFile != null) {
            body.addFormDataPart(USER_FILE, userFile.getName(),
                    RequestBody.create(jpeg, userFile));

        } else {
            body.addFormDataPart(USER_FILE, "");
        }

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
