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

public class DeleteBoardRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    private final static String BOARD = "board";

    public DeleteBoardRequest(Context context, String boardNum) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
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
        return new TypeToken<ResultMessage>() {
        }.getType();

    }

    @Override
    public Request getRequest() {
        return mRequest;
    }
}
