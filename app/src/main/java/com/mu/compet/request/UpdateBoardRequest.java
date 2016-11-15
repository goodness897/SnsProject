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
public class UpdateBoardRequest extends AbstractRequest<ResultMessage> {

    Request mRequest;

    MediaType jpeg = MediaType.parse("image/jpeg");
    private final static String BOARD = "board";
    private final static String BOARD_CONTENT = "boardContent";
    private final static String FILES = "files";
    private final static String DELFILES = "delFiles";

    public UpdateBoardRequest(Context context, String boardNum, String boardContent, File[] files, File[] delFiles) {
        HttpUrl url = getBaseUrlBuilder()
                .addPathSegment(BOARD)
                .addPathSegment(boardNum)
                .build();

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(BOARD_CONTENT, boardContent);
        if (files != null) {
            for(int i = 0; i < files.length; i++)
            body.addFormDataPart(FILES, files[i].getName(),
                    RequestBody.create(jpeg, files[i]));

        } else {
            body.addFormDataPart(FILES, "");
        }
        if (delFiles != null) {
            for(int i = 0; i < delFiles.length; i++)
                body.addFormDataPart(DELFILES, delFiles[i].getName(),
                        RequestBody.create(jpeg, delFiles[i]));

        } else {
            body.addFormDataPart(DELFILES, "");
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
