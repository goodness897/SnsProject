package com.mu.compet.data;

/**
 * Created by Mu on 2016-10-31.
 */

public class BoardItemData implements java.io.Serializable {

    private Board data;
    private String message;
    private int code;

    public Board getData() {
        return data;
    }

    public void setData(Board data) {
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
