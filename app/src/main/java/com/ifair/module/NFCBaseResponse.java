package com.ifair.module;


import java.util.List;

public class NFCBaseResponse {

    /**
     * code : 10000
     * message : success
     * data : [{"tag_pw":"`0000000000000000","msg_length":"10"}]
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
         * tag_pw : `0000000000000000
         * msg_length : 10
         */

        private String tag_pw;
        private String msg_length;

        public String getTag_pw() {
            return tag_pw;
        }

        public void setTag_pw(String tag_pw) {
            this.tag_pw = tag_pw;
        }

        public String getMsg_length() {
            return msg_length;
        }

        public void setMsg_length(String msg_length) {
            this.msg_length = msg_length;
        }
    }
}
