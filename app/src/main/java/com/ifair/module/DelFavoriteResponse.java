package com.ifair.module;

import java.util.List;

/**
 * 商品從我的最愛移除
 */

public class DelFavoriteResponse {
    private int code;
    private String message;
    private List<dataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<dataBean> getData() {
        return data;
    }

    public void setData(List<dataBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class dataBean {

    }
}
