package com.ifair.module;

import java.util.List;

/**
 * 廣告頁
 */
public class AdvertisingResponse {
    private int code;
    private String message;

    public List<dataBean> getData() {
        return data;
    }

    public void setData(List<dataBean> data) {
        this.data = data;
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

    private List<dataBean> data;

    public class dataBean {

        private String ad_id;//        廣告流水號，參考在APP輪播用。
        private String img_file;//                廣告圖
        private String on_time;//                上架日期
        private String off_time;//                下架日期
        private String link_page;//                連結網頁
        private String link_image;//                連結圖片

        public String getClick_count() {
            return click_count;
        }

        public void setClick_count(String click_count) {
            this.click_count = click_count;
        }

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getImg_file() {
            return img_file;
        }

        public void setImg_file(String img_file) {
            this.img_file = img_file;
        }

        public String getLink_image() {
            return link_image;
        }

        public void setLink_image(String link_image) {
            this.link_image = link_image;
        }

        public String getLink_page() {
            return link_page;
        }

        public void setLink_page(String link_page) {
            this.link_page = link_page;
        }

        public String getOff_time() {
            return off_time;
        }

        public void setOff_time(String off_time) {
            this.off_time = off_time;
        }

        public String getOn_time() {
            return on_time;
        }

        public void setOn_time(String on_time) {
            this.on_time = on_time;
        }

        private String click_count;//                被點擊次數

    }
}
