package com.mu.compet.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mu.compet.data.NetworkResultTemp;
import com.mu.compet.data.UserItemData;
import com.mu.compet.manager.NewNetworkRequest;
import com.mu.compet.manager.PropertyManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Mu on 2016-11-09.
 */

public class LoginRequestNew extends NewNetworkRequest implements NewNetworkRequest.AsyncResponse {

    private static final String URL = HOST + HTTP_PORT +"/login";
    private static final String USER_ID = "userId";
    private static final String USER_PASS = "userPass";
    private static final String USER_TYPE = "userType";

    public LoginRequestNew(Context context, String method, String userType, String userId, String userPass) {
        super(context);
        setMethod(method);
        map.put(USER_ID, userId);
        map.put(USER_TYPE, userType);
        map.put(USER_PASS, userPass);
    }

    @Override
    public java.net.URL getURL() throws MalformedURLException {
        Log.d("url", URL.toString());
        return new URL(URL);
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }

    @Override
    protected void write(OutputStream out) {
        super.write(out);
        try {
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            write.write(getPostDataString(map));
            write.flush();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successResult(String output) {
        Gson gson = new Gson();
        NetworkResultTemp temp = gson.fromJson(output, NetworkResultTemp.class);
        if (temp.getCode() == 1) {
            UserItemData result = gson.fromJson(output, UserItemData.class);
            listener.onSuccess(this, result);
        }
    }

    protected void saveCookie(HttpURLConnection conn) {

        Map<String, List<String>> imap = conn.getHeaderFields();
        if (imap.containsKey("Set-Cookie")) {
            List<String> cookies = imap.get("Set-Cookie");
            mCookies = cookies.get(0).split(";")[0];
            Log.d("cookie", mCookies);
            PropertyManager.getInstance().setKeyCookie(mCookies);
            mSession = true;
        } else {
            mSession = false;
        }
    }

}
