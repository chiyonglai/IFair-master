package com.ifair.module;

import java.util.List;

/**
 * 獲得Push開關
 */

public class PushSWITCResponse {


    /**
     * code : 10000
     * message : success
     * data:[{“is_push”:”1”}]
     * {"code":10000,"message":"success","data":[{“is_push”:”1”}]}
     */

    private int code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * is_push : 1 or 0
         */

        private String is_push;

        public String getIsPush() {
            return is_push;
        }

        public void setIsPush(String goods_id) {
            this.is_push = goods_id;
        }

    }
}
