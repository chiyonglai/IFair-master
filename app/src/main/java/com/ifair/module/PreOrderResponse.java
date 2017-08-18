package com.ifair.module;

import java.util.List;

/**
 * 預購商品內頁
 */

public class PreOrderResponse {


    /**
     * code : 10000
     * message : success
     * data : [{"goods_id":"18","goods_name":"凍頂合作社比賽茶(金萱) -【二朵梅】","original_price":"1500","price":"1100","amount":"0","pre_amount":"2","goods_image2":"http://35.164.137.155/uploads/goods_image/883055302cb4786dead1de445c18afa0.png","introduction":"2016 冬季 凍頂合作社比賽茶【二朵梅】（新品種組）\n今年2016鹿谷鄉的第一場冬季比賽茶評鑑（新品種組＝金萱），成績已揭曉了，今年冬茶「新品種組」參加樣數共有2153樣參加，與往年相當，淘汰近三成，分初、複二階段評審，初審由初評班班長黃耀昌及23位班員品評，複審則是聘請農委會茶業改良場長陳國任、農委會茶業改良場台東分場長吳聲舜評審，目前新品種茶樣已全數評鑑完畢，茶葉包裝完成，目前茶葉已領回，陸續開始出貨，歡迎有需要送禮、或喜歡金萱烏龍的朋友歡迎利用網路訂購或是來電洽詢049-2671873\n\n＊香型：半青熟香\n＊主辦：凍頂茶葉生產合作社\n＊產地：南投縣鹿谷鄉\n＊重量：600克 (g)\n＊包裝：一斤裝禮盒 X 1 (附提袋)\n＊內容物：頂級 手採金萱烏龍茶\n 2016 冬季比賽茶，現貨供應＝免運費＝"}]
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
         * goods_id : 18
         * goods_name : 凍頂合作社比賽茶(金萱) -【二朵梅】
         * original_price : 1500
         * price : 1100
         * amount : 0
         * pre_amount : 2
         * goods_image2 : http://35.164.137.155/uploads/goods_image/883055302cb4786dead1de445c18afa0.png
         * introduction : 2016 冬季 凍頂合作社比賽茶【二朵梅】（新品種組）
         今年2016鹿谷鄉的第一場冬季比賽茶評鑑（新品種組＝金萱），成績已揭曉了，今年冬茶「新品種組」參加樣數共有2153樣參加，與往年相當，淘汰近三成，分初、複二階段評審，初審由初評班班長黃耀昌及23位班員品評，複審則是聘請農委會茶業改良場長陳國任、農委會茶業改良場台東分場長吳聲舜評審，目前新品種茶樣已全數評鑑完畢，茶葉包裝完成，目前茶葉已領回，陸續開始出貨，歡迎有需要送禮、或喜歡金萱烏龍的朋友歡迎利用網路訂購或是來電洽詢049-2671873

         ＊香型：半青熟香
         ＊主辦：凍頂茶葉生產合作社
         ＊產地：南投縣鹿谷鄉
         ＊重量：600克 (g)
         ＊包裝：一斤裝禮盒 X 1 (附提袋)
         ＊內容物：頂級 手採金萱烏龍茶
         2016 冬季比賽茶，現貨供應＝免運費＝
         */

        private String goods_id;
        private String goods_name;
        private String original_price;
        private String price;
        private String amount;
        private String pre_amount;
        private String goods_image2;
        private String introduction;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPre_amount() {
            return pre_amount;
        }

        public void setPre_amount(String pre_amount) {
            this.pre_amount = pre_amount;
        }

        public String getGoods_image2() {
            return goods_image2;
        }

        public void setGoods_image2(String goods_image2) {
            this.goods_image2 = goods_image2;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
