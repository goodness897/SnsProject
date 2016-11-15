package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.FileData;
import com.mu.compet.data.ListData;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Mu on 2016-10-28.
 */

public class ListFileRequest extends AbstractRequest<ListData<FileData>> {

    Request mRequest;

    private static final String BOARD = "board";
    private static final String FILES = "files";

    public ListFileRequest(Context context, String boardNum) {

        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .addPathSegment(FILES)
                .build();

        mRequest = new Request.Builder()
                .url(url)
                .tag(context)
                .build();
        Log.i("url", mRequest.url().toString());

    }

    @Override
    protected Type getType() {
        return new TypeToken<ListData<FileData>>() {}.getType();
    }

    @Override
    public Request getRequest() {
        return mRequest;
    }
}
