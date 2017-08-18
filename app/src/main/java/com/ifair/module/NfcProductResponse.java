package com.ifair.module;

import java.util.List;

/**
 * NFC便是真品後，取得商品資訊
 */

public class NfcProductResponse {


    /**
     * code : 10000
     * message : success
     * data : [{"goods_id":"30","goods_code":"20170419005","goods_name":"多邊造型-藍芽喇叭-7.0杜比環繞音效","pre_date_s":"2017-04-19 00:00:00","firm_id":"6","firm_name":"點睛良品","tel":"04-23015352","url":"http://ideabus.dig.tw/tw/","address":"台中市西屯區公益路161號13F-4","youtube_url":"","batch_image":"","sgs_url":"","batch_url":"","note":"代理商\n捲門從line\n遠端遙控智慧鐵捲門","goods_image3":["http://35.164.137.155/uploads/goods_image/25c0efcd020108f7fb25f7c46985a54a.png","http://35.164.137.155/uploads/goods_image/64a33817e075ab2048c5f9161f4c839b.png","http://35.164.137.155/uploads/goods_image/58f2df7007c7884dad83c127fd2afea5.png","http://35.164.137.155/uploads/goods_image/1bbeff9b7ac72f996b7eef59d5edd15c.png","http://35.164.137.155/uploads/goods_image/ecec8e89e110a42ad17d650d38f12309.png","http://35.164.137.155/uploads/goods_image/99eb6621301d0b566326703db58e43e2.png"],"good_imag_url":["","","","","",""]}]
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
         * goods_id : 30
         * goods_code : 20170419005
         * goods_name : 多邊造型-藍芽喇叭-7.0杜比環繞音效
         * pre_date_s : 2017-04-19 00:00:00
         * firm_id : 6
         * firm_name : 點睛良品
         * tel : 04-23015352
         * url : http://ideabus.dig.tw/tw/
         * address : 台中市西屯區公益路161號13F-4
         * youtube_url :
         * batch_image :
         * sgs_url :
         * batch_url :
         * note : 代理商
         捲門從line
         遠端遙控智慧鐵捲門
         * goods_image3 : ["http://35.164.137.155/uploads/goods_image/25c0efcd020108f7fb25f7c46985a54a.png","http://35.164.137.155/uploads/goods_image/64a33817e075ab2048c5f9161f4c839b.png","http://35.164.137.155/uploads/goods_image/58f2df7007c7884dad83c127fd2afea5.png","http://35.164.137.155/uploads/goods_image/1bbeff9b7ac72f996b7eef59d5edd15c.png","http://35.164.137.155/uploads/goods_image/ecec8e89e110a42ad17d650d38f12309.png","http://35.164.137.155/uploads/goods_image/99eb6621301d0b566326703db58e43e2.png"]
         * good_imag_url : ["","","","","",""]
         */

        private String goods_id;
        private String goods_code;
        private String goods_name;
        private String pre_date_s;
        private String firm_id;
        private String firm_name;
        private String tel;
        private String url;
        private String address;
        private String youtube_url;
        private String batch_image;
        private String sgs_url;
        private String batch_url;
        private String note;
        private List<String> goods_image3;
        private List<String> good_imag_url;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_code() {
            return goods_code;
        }

        public void setGoods_code(String goods_code) {
            this.goods_code = goods_code;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getPre_date_s() {
            return pre_date_s;
        }

        public void setPre_date_s(String pre_date_s) {
            this.pre_date_s = pre_date_s;
        }

        public String getFirm_id() {
            return firm_id;
        }

        public void setFirm_id(String firm_id) {
            this.firm_id = firm_id;
        }

        public String getFirm_name() {
            return firm_name;
        }

        public void setFirm_name(String firm_name) {
            this.firm_name = firm_name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getYoutube_url() {
            return youtube_url;
        }

        public void setYoutube_url(String youtube_url) {
            this.youtube_url = youtube_url;
        }

        public String getBatch_image() {
            return batch_image;
        }

        public void setBatch_image(String batch_image) {
            this.batch_image = batch_image;
        }

        public String getSgs_url() {
            return sgs_url;
        }

        public void setSgs_url(String sgs_url) {
            this.sgs_url = sgs_url;
        }

        public String getBatch_url() {
            return batch_url;
        }

        public void setBatch_url(String batch_url) {
            this.batch_url = batch_url;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public List<String> getGoods_image3() {
            return goods_image3;
        }

        public void setGoods_image3(List<String> goods_image3) {
            this.goods_image3 = goods_image3;
        }

        public List<String> getGood_imag_url() {
            return good_imag_url;
        }

        public void setGood_imag_url(List<String> good_imag_url) {
            this.good_imag_url = good_imag_url;
        }
    }
}
