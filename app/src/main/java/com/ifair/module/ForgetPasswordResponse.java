package com.ifair.module;

import java.util.List;

/**
 * 忘記密碼回傳
 */

public class ForgetPasswordResponse {
    private int code;
    private String message;
    private List<DataBean> data;

    public List<ForgetPasswordResponse.DataBean> getData() {
        return data;
    }

    public void setData(List<ForgetPasswordResponse.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * data :
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
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
}
