package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.ResultMessage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Mu on 2016-11-01.
 */

public class NewSignUpRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    private final static String USER = "user";
    private final static String USER_ID = "userId";
    private final static String USER_PASS = "userPass";
    private final static String USER_NICK = "userNick";
    private final static String USER_FILE = "userFile";

    MediaType jpeg = MediaType.parse("image/jpeg");

    public NewSignUpRequest(Context context, String userId, String userPass, String userNick, File userFile) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(USER)
                .build();

       MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(USER_ID, userId)
                .addFormDataPart(USER_PASS, userPass)
                .addFormDataPart(USER_NICK, userNick);


        if (userFile != null) {
            body.addFormDataPart(USER_FILE, userFile.getName(),
                    RequestBody.create(jpeg, userFile));
        } else {
            body.addFormDataPart(USER_FILE, "");
        }

        RequestBody requestBody = body.build();

        mRequest = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .tag(context)
                .build();

        Log.i("url", url.toString());

    }

    @Override
    protected ResultMessage parse(ResponseBody body) throws IOException {
        String text = body.string();
        Gson gson = new Gson();
        ResultMessage temp = gson.fromJson(text, getType());
        Log.i("result", text);
        return temp;
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
