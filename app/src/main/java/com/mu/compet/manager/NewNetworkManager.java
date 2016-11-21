package com.mu.compet.manager;

/**
 * Created by Mu on 2016-11-09.
 */

public class NewNetworkManager {
    private static NewNetworkManager instance;

    public static NewNetworkManager getInstance() {
        if(instance == null) {
            instance = new NewNetworkManager();
        }
        return instance;
    }
    private NewNetworkManager() {

    }
    public interface OnResultListener {
        public void onSuccess(NewNetworkRequest request, Object result);

        public void onFail(NewNetworkRequest request, int errorCode, String errorMessage);
    }

    public void getNetworkData(NewNetworkRequest request, OnResultListener listener) {
        request.setOnResultListener(listener);
        request.execute();
    }

}
