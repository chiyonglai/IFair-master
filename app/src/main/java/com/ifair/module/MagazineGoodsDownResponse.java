package com.ifair.module;

import java.util.List;

/**
 * 商品雜誌內頁
 */

public class MagazineGoodsDownResponse {


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
        private String goods_name;
        private String pre_date_s;
        private String pre_date_e;
        private String up_date;
        private String down_date;
        private String is_preorder;
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
