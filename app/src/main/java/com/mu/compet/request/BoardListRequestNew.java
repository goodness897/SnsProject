package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mu.compet.data.ListBoardData;
import com.mu.compet.data.NetworkResultTemp;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NewNetworkRequest;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mu on 2016-11-09.
 */

public class BoardListRequestNew extends NewNetworkRequest implements NewNetworkRequest.AsyncResponse {

    private static final String URL = HOST + HTTP_PORT + "/boards?pageNum=%s&lastBoardNum=%s";

    String url;


    public BoardListRequestNew(Context context, String method, String pageNum, String lastBoardNum) {
        super(context);
        setMethod(method);
        url = String.format(URL, pageNum, lastBoardNum);
    }

    @Override
    public java.net.URL getURL() throws MalformedURLException {
        Log.d("url", url);
        return new URL(url);
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("Accept", "application/json");
    }

    @Override
    public void successResult(String output) {
        Gson gson = new Gson();
        NetworkResultTemp temp = gson.fromJson(output, NetworkResultTemp.class);
        if (temp.getCode() == 1) {
            ListBoardData result = gson.fromJson(output, ListBoardData.class);
            listener.onSuccess(this, result);
        } else {
            ResultMessage<String> result = gson.fromJson(output, ResultMessage.class);
            listener.onFail(this, result.getCode(), result.getMessage());
        }
    }
}
