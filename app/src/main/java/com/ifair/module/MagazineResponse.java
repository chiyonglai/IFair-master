package com.ifair.module;

import java.util.List;

/**
 * 商品雜誌詳細資料
 */

public class MagazineResponse {

    /**
     * code : 10000
     * message : success
     * data : [{"goods_id":"14","goods_name":"VIP20170104001","introduction":"VIP20170104001","pre_date_s":"2017-01-04 00:00:00","pre_date_e":"2017-01-31 00:00:00","up_date":"2017-01-04 00:00:00","down_date":"","is_preorder":"false","goods_image":"http://35.164.137.155/uploads/goods_image/57aa68308d807151416dd180dde89a45.jpg"},{"goods_id":"15","goods_name":"VIP201701040002","introduction":"test","pre_date_s":"2017-01-05 00:00:00","pre_date_e":"2017-01-30 00:00:00","up_date":"","down_date":"","is_preorder":"false","goods_image":"http://35.164.137.155/uploads/goods_image/1fd8c13ee0ca3f6ebf055e6e2e1ed3de.jpg"}]
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
         * goods_id : 14
         * goods_name : VIP20170104001
         * introduction : VIP20170104001
         * pre_date_s : 2017-01-04 00:00:00
         * pre_date_e : 2017-01-31 00:00:00
         * up_date : 2017-01-04 00:00:00
         * down_date :
         * is_preorder : false
         * goods_image : http://35.164.137.155/uploads/goods_image/57aa68308d807151416dd180dde89a45.jpg
         */

        private String goods_id;
        private String goods_name;
        private String introduction;
        private String pre_date_s;
        private String pre_date_e;
        private String up_date;
        private String down_date;
        private String is_preorder;
        private String goods_image;
        private String goods_image2;
        private String original_price;
        private String price;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
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

        public String getUp_date() {
            return up_date;
        }

        public void setUp_date(String up_date) {
            this.up_date = up_date;
        }

        public String getDown_date() {
            return down_date;
        }

        public void setDown_date(String down_date) {
            this.down_date = down_date;
        }

        public String getIs_preorder() {
            return is_preorder;
        }

        public void setIs_preorder(String is_preorder) {
            this.is_preorder = is_preorder;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_image2() {
            return goods_image2;
        }

        public void setGoods_image2(String goods_image2) {
            this.goods_image2 = goods_image2;
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
    }
}
