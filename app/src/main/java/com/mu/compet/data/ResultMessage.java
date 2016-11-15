package com.mu.compet.data;


import java.io.Serializable;

//메세지 처리 데이터 클래스
public class ResultMessage implements Serializable {

    private String message;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
