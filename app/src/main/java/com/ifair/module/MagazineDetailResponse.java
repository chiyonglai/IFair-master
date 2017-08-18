package com.ifair.module;

import java.util.List;

/**
 * 商品雜誌內頁
 */

public class MagazineDetailResponse {


    /**
     * code : 10000
     * message : success
     * data : [{"goods_id":"16","firm_id":"3","firm_name":"Bill測試-點睛科技","goods_name":"極度輕量-大氣墊籃球鞋","original_price":"3600","price":"3200","pre_amount":"2","is_preorder":"false","pre_date_s":"2017-01-19 00:00:00","pre_date_e":"2017-01-28 00:00:00","introduction":"超大氣墊，特殊炫彩霓虹氣墊","like":"0","goods_image":"http://35.164.137.155/uploads/goods_image/6bf776287f8e8ee8d6dda979c7413b01.jpg","is_like":"false","is_favourite":"false","up_date":"","big_image2":"","big_image3":"","big_image4":"","big_image5":"","big_image6":""}]
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
         * goods_id : 16
         * firm_id : 3
         * firm_name : Bill測試-點睛科技
         * goods_name : 極度輕量-大氣墊籃球鞋
         * original_price : 3600
         * price : 3200
         * pre_amount : 2
         * is_preorder : false
         * pre_date_s : 2017-01-19 00:00:00
         * pre_date_e : 2017-01-28 00:00:00
         * introduction : 超大氣墊，特殊炫彩霓虹氣墊
         * like : 0
         * goods_image : http://35.164.137.155/uploads/goods_image/6bf776287f8e8ee8d6dda979c7413b01.jpg
         * is_like : false
         * is_favourite : false
         * up_date :
         * big_image2 :
         * big_image3 :
         * big_image4 :
         * big_image5 :
         * big_image6 :
         */

        private String goods_id;
        private String firm_id;
        private String firm_name;
        private String goods_name;
        private String original_price;
        private String price;
        private String pre_amount;
        private String is_preorder;
        private String pre_date_s;
        private String pre_date_e;
        private String introduction;
        private String like;
        private String youtube_url;
        private List<String> goods_image;
        private String is_like;
        private String is_favourite;
        private String up_date;
        private List<String> big_imag_url;


        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
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

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPre_amount() {
            return pre_amount;
        }

        public void setPre_amount(String pre_amount) {
            this.pre_amount = pre_amount;
        }

        public String getIs_preorder() {
            return is_preorder;
        }

        public void setIs_preorder(String is_preorder) {
            this.is_preorder = is_preorder;
        }

        public String getPre_date_s() {
            return pre_date_s;
        }

        public void setPre_date_s(String pre_date_s) {
            this.pre_date_s = pre_date_s;
        }

        public String getPre_date_e() {
            return pre_date_e;
        }

        public void setPre_date_e(String pre_date_e) {
            this.pre_date_e = pre_date_e;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public List<String> getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(List<String> goods_image) {
            this.goods_image = goods_image;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getIs_favourite() {
            return is_favourite;
        }

        public void setIs_favourite(String is_favourite) {
            this.is_favourite = is_favourite;
        }

        public String getUp_date() {
            return up_date;
        }

        public void setUp_date(String up_date) {
            this.up_date = up_date;
        }

        public List<String> getBig_imag_url() {
            return big_imag_url;
        }

        public void setBig_imag_url(List<String> big_imag_url) {
            this.big_imag_url = big_imag_url;
        }

        public String getYoutube_url() {
            return youtube_url;
        }

        public void setYoutube_url(String youtube_url) {
            this.youtube_url = youtube_url;
        }
    }
}
