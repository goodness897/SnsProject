package com.mu.compet.data;


import java.util.List;

public class ListData<T> implements java.io.Serializable {
    private static final long serialVersionUID = 4222295191657793439L;
//    private T [] data;
    private List<T> data;
    private String message;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

//    public T [] getData() {
//        return this.data;
//    }
//
//    public void setData(T [] data) {
//        this.data = data;
//    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
