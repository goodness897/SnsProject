package com.mu.compet.data;


import java.io.Serializable;

//메세지 처리 데이터 클래스
public class ResultMessage<T> implements Serializable {

    private T message;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
