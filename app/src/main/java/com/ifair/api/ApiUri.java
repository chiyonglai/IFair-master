package com.ifair.api;

/**
 * API的連線網址
 */

public class ApiUri {

    //APP送TAGID至主機
    public static final String API_SEND_TAGID = "http://35.164.137.155/api/get_tagid";
    //向雲端查詢驗證碼
    public static final String API_SEND_VERIFY = "http://35.164.137.155/api/get_verify";
    //NFC產品舉報偽品
    public static final String API_SEND_GOODS_FORGED = "http://35.164.137.155/api/send_goods_forged";
    //取得NFC產品屬真品資料
    public static final String API_Get_true_goods = "http://35.164.137.155/api/get_true_goods";
    //取得最新消息
    public static final String API_Get_Latest_news = "http://35.164.137.155/api/get_latest_news";
    //取得最新消息詳細資料
    public static final String API_Get_Latest_news_detail = "http://35.164.137.155/api/get_latest_news_detail";
    //新增按讚數
    public static final String API_Add_like_message = "http://35.164.137.155/api/add_like_message";
    //取得預購商品內頁
    public static final String API_Get_PreOrder = "http://35.164.137.155/api/get_goods_order";
    //帳號/密碼登入
    public static final String API_SENT_Login = "http://35.164.137.155/api/login";
    //註冊帳號
    public static final String API_SENT_Register = "http://35.164.137.155/api/register";
    //修改帳號資料
    public static final String API_SENT_Edit_User_Information = "http://35.164.137.155/api/modify_member";
    //取得商品雜誌資料
    public static final String API_GET_Magazine = "http://35.164.137.155/api/get_goods";
    //取得商品雜誌內頁資料
    public static final String API_GET_Magazine_Detail = "http://35.164.137.155/api/get_goods_detail";
    // 新增我的最愛商品
    public static final String API_SEND_Add_Favorite = "http://35.164.137.155/api/add_member_favourite";
    // 刪除我的最愛商品
    public static final String API_SEND_Delete_Favorite = "http://35.164.137.155/api/del_member_favourite";
    //預購商品內頁
    public static final String API_GET_PreOrder_Detail = "http://35.164.137.155/api/get_goods_order";
    //取得會員預購資訊預存
    public static final String API_GET_PreOrder_Information = "http://35.164.137.155/api/get_order_address";
    //存儲會員預購資訊
    public static final String API_SEND_ADD_PreOrder_Information = "http://35.164.137.155/api/add_order_address";
    //編輯會員預購資訊
    public static final String API_SEND_EDIT_PreOrder_Information = "http://35.164.137.155/api/edit_order_address";
    //設定新密碼
    public static final String API_SEND_Forget_Password = "http://35.164.137.155/api/forgot_password";
    //廣告頁
    public static final String API_GET_Advertising = "http://35.164.137.155/api/get_advertising";
    //送出商品點讚數
    public static final String API_add_like_goods = "http://35.164.137.155/api/add_like_goods";
    //存預購商品訂單
    public static final String API_add_member_order = "http://35.164.137.155/api/add_member_order";
    //商品內頁下方的推薦商品
    public static final String API_GET_GOODS_DOWN= "http://35.164.137.155/api/get_goods_down";
    //廣告被點擊次數
    public static final String API_ADD_AD_CLICK= "http://35.164.137.155/api/add_ad_click";
    //最新消息的分享次數
    public static final String API_ADD_SHARE_MESSAGE= "http://35.164.137.155/api/add_share_message";
    //商品的分享次數
    public static final String API_ADD_SHARE_GOODS= "http://35.164.137.155/api/add_share_goods";
    //推播消息-點擊數
    public static final String API_PUSH_LIKE_MESSAGE= "http://35.164.137.155/api/add_push_like_messag";


}
