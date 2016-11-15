package com.mu.compet.data;

/**
 * Created by Mu on 2016-10-31.
 */

public class ReplyItemData implements java.io.Serializable {

    private Reply data;
    private String message;
    private int code;

    public Reply getData() {
        return data;
    }

    public void setData(Reply data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
