package com.ifair.module;

import java.util.List;

/**
 * 登入回傳值
 */

public class LoginResponse {

    /**
     * code : 10000
     * message : success
     * data : [{"email":"z0furu@gmail.com","name":"盧冠宇","birthday":"1997-11-22","sex":"1","register_type":"1"}]
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
         * email : z0furu@gmail.com
         * name : 盧冠宇
         * birthday : 1997-11-22
         * sex : 1
         * register_type : 1
         */

        private String email;
        private String name;
        private String birthday;
        private String sex;
        private String register_type;
        private String phone;
        private String area;

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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRegister_type() {
            return register_type;
        }

        public void setRegister_type(String register_type) {
            this.register_type = register_type;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}
