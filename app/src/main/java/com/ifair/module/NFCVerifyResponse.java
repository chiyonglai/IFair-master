package com.ifair.module;

import java.util.List;

/**
 * NFC驗證碼的回應
 */

public class NFCVerifyResponse {


    /**
     * code : 10000
     * message : success
     * data : [{"goods_code":"VIP20170104001"}]
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
         * goods_code : VIP20170104001
         */

        private String goods_code;

        public String getGoods_code() {
            return goods_code;
        }

        public void setGoods_code(String goods_code) {
            this.goods_code = goods_code;
        }
    }
}
