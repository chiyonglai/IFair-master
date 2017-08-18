package com.ifair.module;

import java.util.List;

/**
 * 修改預購資訊
 */

public class EditPreOrderInformationResponse {
    private int code;
    private String message;

    public List<dataBean> getData() {
        return data;
    }

    public void setData(List<dataBean> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<dataBean> data;
    public class dataBean{

    }
}
