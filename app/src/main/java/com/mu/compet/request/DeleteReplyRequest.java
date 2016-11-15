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

public class DeleteReplyRequest extends AbstractRequest<ResultMessage>{

    Request mRequest;

    private final static String BOARD = "board";
    private final static String REPLY = "reply";

    public DeleteReplyRequest(Context context, String boardNum, String replyNum) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .addPathSegment(REPLY)
                .addPathSegment(replyNum)
                .build();

        mRequest = new Request.Builder()
                .url(url)
                .delete()
                .tag(context)
                .build();

        Log.i("url", mRequest.url().toString());

    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultMessage>(){}.getType();

    }
    @Override
    public Request getRequest() {
        return mRequest;
    }
}
