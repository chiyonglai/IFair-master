package com.ifair.module;

import java.util.List;

/**
 * 最新消息詳細資料
 */

public class NewMessageDetailResponse {


    /**
     * code : 10000
     * message : success
     * data : [{"message_id":"24","firm_name":"bill點睛測試","title":"鞋子","on_time":"2017-05-31 00:00:00","off_time":"2017-06-01 00:00:00","content":"AIR JORDAN XXXI\nBLACK CAT\n$185.00\nWith its enhanced cushioning and explosive responsiveness, the Air Jordan XXXI is built for soft landings and springy first steps. Flightspeed technology puts it at the top of the performance footwear food chain, while a Flyweave upper offers flexible, breathable support. The Black Cat colorway evokes MJ's powerful style of play and his status as the game's all-time apex predator.\nSIZE\nBUY $185.00","like":"0","url":"https://www.nike.com/launch/t/air-jordan-31-black-cat","img_file":["http://35.164.137.155/uploads/message_image/0434b8e5e116e75357a111148ea0bbb4.png"],"source_content":"","img_url":["","","","","",""],"pre_order_url":"","youtube_url":"","goods_id":"50","is_like":"false","is_new":"true","is_buy":"true"}]
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
         * message_id : 24
         * firm_name : bill點睛測試
         * title : 鞋子
         * on_time : 2017-05-31 00:00:00
         * off_time : 2017-06-01 00:00:00
         * content : AIR JORDAN XXXI
         BLACK CAT
         $185.00
         With its enhanced cushioning and explosive responsiveness, the Air Jordan XXXI is built for soft landings and springy first steps. Flightspeed technology puts it at the top of the performance footwear food chain, while a Flyweave upper offers flexible, breathable support. The Black Cat colorway evokes MJ's powerful style of play and his status as the game's all-time apex predator.
         SIZE
         BUY $185.00
         * like : 0
         * url : https://www.nike.com/launch/t/air-jordan-31-black-cat
         * img_file : ["http://35.164.137.155/uploads/message_image/0434b8e5e116e75357a111148ea0bbb4.png"]
         * source_content :
         * img_url : ["","","","","",""]
         * pre_order_url :
         * youtube_url :
         * goods_id : 50
         * is_like : false
         * is_new : true
         * is_buy : true
         */

        private String message_id;
        private String firm_name;
        private String title;
        private String on_time;
        private String off_time;
        private String content;
        private String like;
        private String url;
        private String source_content;
        private String pre_order_url;
        private String youtube_url;
        private String goods_id;
        private String is_like;
        private String is_new;
        private String is_buy;
        private List<String> img_file;
        private List<String> img_url;

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getFirm_name() {
            return firm_name;
        }

        public void setFirm_name(String firm_name) {
            this.firm_name = firm_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOn_time() {
            return on_time;
        }

        public void setOn_time(String on_time) {
            this.on_time = on_time;
        }

        public String getOff_time() {
            return off_time;
        }

        public void setOff_time(String off_time) {
            this.off_time = off_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSource_content() {
            return source_content;
        }

        public void setSource_content(String source_content) {
            this.source_content = source_content;
        }

        public String getPre_order_url() {
            return pre_order_url;
        }

        public void setPre_order_url(String pre_order_url) {
            this.pre_order_url = pre_order_url;
        }

        public String getYoutube_url() {
            return youtube_url;
        }

        public void setYoutube_url(String youtube_url) {
            this.youtube_url = youtube_url;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }

        public List<String> getImg_file() {
            return img_file;
        }

        public void setImg_file(List<String> img_file) {
            this.img_file = img_file;
        }

        public List<String> getImg_url() {
            return img_url;
        }

        public void setImg_url(List<String> img_url) {
            this.img_url = img_url;
        }
    }
}
