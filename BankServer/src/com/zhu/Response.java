package com.zhu;

import java.io.Serializable;

//响应
public class Response<String,T> implements Serializable {

    private String type;

    private T value;

    public Response(String type, T value) {
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
