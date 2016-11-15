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
 * Created by jeon on 2016-09-04.
 */
public class UpdateReplyRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;


    private final static String BOARD = "board";
    private final static String REPLY = "reply";
    private final static String REPLY_CONTENT = "replyContent";

    public UpdateReplyRequest(Context context, String boardNum, String replyNum, String replyContent) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .addPathSegment(REPLY)
                .addPathSegment(replyNum)
                .build();

        RequestBody body = new FormBody.Builder()
                .add(REPLY_CONTENT, replyContent)
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
