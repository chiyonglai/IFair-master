package com.ifair.module;

import java.util.List;

/**
 * 取得預購資訊預存列表
 */

public class PreorderInformationResponse {
    private int code;

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

    private String message;
    private List<dataBean> data;

    public class dataBean {

        private String order_address_id;//                消費者預購地址編號
        private String email;//                消費者帳號
        private String type;//        常用資料名稱，可單選一個。
        private String name;//                姓名
        private String phone;//                電話
        private String address;//                地址

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrder_address_id() {
            return order_address_id;
        }

        public void setOrder_address_id(String order_address_id) {
            this.order_address_id = order_address_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
