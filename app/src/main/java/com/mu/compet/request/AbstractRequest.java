package com.mu.compet.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mu.compet.data.NetworkResultTemp;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkRequest;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-29.
 */
public abstract class AbstractRequest<T> extends NetworkRequest<T> {

//    private final static String HOST = "192.168.6.62"; // 용범 ip
    private final static String HOST = "192.168.6.122"; // 영훈 ip
//    private final static String HOST = "192.168.6.11"; // 정호 ip
//    private final static int HTTPS_PORT = 443;
    private final static int HTTP_PORT = 8080;



//    private final static String HOST = "192.168.6.18"; // 정호 ip
//    private final static int HTTP_PORT = 9101; // 정호 IP
//    private final static int HTTP_PORT = 9102; // 영훈 IP
//    private final static int HTTP_PORT = 9103; // 용범 IP



//    protected HttpUrl.Builder getBaseUrlHttpsBuilder() {
//        HttpUrl.Builder builder = new HttpUrl.Builder();
//        builder.scheme("https");
//        builder.host(HOST);
//        builder.port(HTTPS_PORT);
//        return builder;
//    }

    protected HttpUrl.Builder getBaseUrlBuilder() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http");
        builder.host(HOST);
        builder.port(HTTP_PORT);
        return builder;
    }

    @Override
    protected T parse(ResponseBody body) throws IOException {

        String text = body.string();
        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject = parser.parse(text).getAsJsonObject();
        NetworkResultTemp temp = gson.fromJson(text, NetworkResultTemp.class);
//        T result = gson.fromJson(text, getType(temp.getCode()));
//        return result;

        if (temp.getCode() == 1) { // 성공
            T result = gson.fromJson(text, getType());
            return result;
        } else if (temp.getCode() == 0) { // 실패
            Type type = new TypeToken<ResultMessage>(){}.getType();
            ResultMessage result = gson.fromJson(text, type);
            throw new IOException(result.getMessage());
        } else {
            T result = gson.fromJson(text, getType(temp.getCode()));
            return result;
        }

    }
    protected Type getType(int code) {
        return getType();
    }

    protected abstract Type getType();

    public static String getHOST() {
        return HOST;
    }

    public static int getHttpPort() {
        return HTTP_PORT;
    }
}
