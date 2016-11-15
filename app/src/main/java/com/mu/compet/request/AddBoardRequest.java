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
 * Created by Tacademy on 2016-08-29.
 */
//

//        boardContent : 등록할 메시지 (필수)
//        files List<file> : 등록하고 싶은 파일의 list 파일의 타입은 image/*,audio/*,video/*로 한정
//
//        예시) /board
public class AddBoardRequest extends AbstractRequest<ResultMessage> {
    Request mRequest;
    MediaType jpeg = MediaType.parse("image/jpeg");


    private final static String BOARD = "board";
    private final static String BOARD_CONTENT = "boardContent";
    private final static String USER_PASSWORD = "userPass";
    private final static String USER_NICKNAME = "userNick";
    private final static String USER_FILE = "files";

    public AddBoardRequest(Context context, String boardContent, File[] files) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .build();

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(BOARD_CONTENT, boardContent);

        if (files.length > 0) {
            for (File image_file : files) {
                if (image_file != null) {
                    body.addFormDataPart(USER_FILE, image_file.getName(),
                            RequestBody.create(jpeg, image_file));
                }
            }
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
    public Request getRequest() {
        return mRequest;
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
}
