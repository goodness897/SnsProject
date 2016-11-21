package com.mu.compet.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mu on 2016-11-09.
 */

public abstract class NewNetworkRequest extends AsyncTask<Void, Void, String> {

    public AsyncResponse result = null;
    public HashMap<String, String> map = new HashMap<>();

    protected final static String HOST = "http://192.168.6.18:"; // 공통 ip
    protected final static int HTTP_PORT = 9102; // 영훈 port

    public static final String POST = "POST";
    private String method;

    private int code;
    private String errorMessage;
    private Context contenxt;

    protected boolean mSession = false;
    protected String mCookies = "";
    CookieManager cookieManager;



    public interface AsyncResponse {
        void successResult(String output);
    }

    public NewNetworkManager.OnResultListener listener;

    public NewNetworkRequest(Context context) {
        this.contenxt = context;
        result = (AsyncResponse) this;
    }

    public abstract URL getURL() throws MalformedURLException;

    public void setOnResultListener(NewNetworkManager.OnResultListener listener) {
        this.listener = listener;
    }

    public void setNetworkConfig(HttpURLConnection conn) {
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(10000);
    }

    protected void setRequestProperty(HttpURLConnection conn) {
    }

    protected void write(OutputStream out) {
    }


    protected void saveCookie(HttpURLConnection conn) {
        List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
        if (cookieList != null) {
            for (String cookieTemp : cookieList) {
                cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
            }
        }

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... objects) {
        try {

            URL url = getURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            cookieManager = CookieManager.getInstance();
            String cookie = cookieManager.getCookie(conn.getURL().toString());
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            setNetworkConfig(conn);
            String method = getMethod();
            conn.setRequestMethod(method);
            if (POST.equals(method)) {
                conn.setDoOutput(true);
            }
            setRequestProperty(conn);
            if (conn.getDoOutput()) {
                write(conn.getOutputStream());
            }
            int code = conn.getResponseCode();
            saveCookie(conn);
            if (code >= 200 && code < 300) { // 성공한 경우
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }
                return sb.toString();
            } else {
                sendError(code, conn.getResponseMessage());
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            sendError(-1, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            result.successResult(s);
        } else {
            Log.d("NewNetworkRequest", "실패");
        }
    }


    protected void sendError(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
        if (listener != null) {
            listener.onFail(this, code, errorMessage);
        }
    }

    protected String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
