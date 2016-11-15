package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.BoardItemData;

import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Tacademy on 2016-08-29.
 */
//

//        게시물 상세 조회(GET)
//        boardNum(int) 보고싶은 게시글의 고유 번호
//
//        예시) /board/1
public class DetailBoardRequest extends AbstractRequest<BoardItemData> {

    Request mRequest;
    final static String BOARD = "board";

    public DetailBoardRequest(Context context, String boardNum) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .build();


        mRequest = new Request.Builder()
                .url(url)
                .tag(context)
                .build();
        Log.i("url", mRequest.url().toString());
    }

    @Override
    public Request getRequest() {
        return mRequest;
    }

    @Override
    protected Type getType() {
        return new TypeToken<BoardItemData>() {
        }.getType();
    }
}
