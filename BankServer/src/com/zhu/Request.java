package com.zhu;

import java.io.*;

//请求
public class Request<String,T> implements Serializable{

    private String type;

    private T value;

    public Request(String type, T value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
