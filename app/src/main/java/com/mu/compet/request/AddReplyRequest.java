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
 * Created by Mu on 2016-10-28.
 */


// 댓글 등록(POST)

// /board/{boardNum}/reply
//boardNum(int) 댓글을 등록할 게시글의 고유번호

public class AddReplyRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    private final static String BOARD = "board";
    private final static String REPLY = "reply";
    private final static String REPLY_CONTENT = "replyContent";

    public AddReplyRequest(Context context, String boardNum, String replyContent) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .addPathSegment(REPLY)
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
