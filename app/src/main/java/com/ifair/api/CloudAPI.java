package com.ifair.api;

import android.app.Activity;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.google.gson.Gson;
import com.ifair.callback.DialogCallback;
import com.ifair.listener.OnAddLikeGoodsListener;
import com.ifair.listener.OnAddMemberOrderListener;
import com.ifair.listener.OnChangePasswordListener;
import com.ifair.listener.OnGetAddLikeListener;
import com.ifair.listener.OnGetAdvertisingListener;
import com.ifair.listener.OnGetLatestNewsDetailListener;
import com.ifair.listener.OnGetLatestNewsListener;
import com.ifair.listener.OnGetMagazineDetailListener;
import com.ifair.listener.OnGetMagazineGoodsDownListener;
import com.ifair.listener.OnGetMagazineListener;
import com.ifair.listener.OnGetPreOrderDetailListener;
import com.ifair.listener.OnGetPreOrderInformationListener;
import com.ifair.listener.OnGetTrueGoodsListener;
import com.ifair.listener.OnNFCSendTagIdListener;
import com.ifair.listener.OnSendAddFavoriteListener;
import com.ifair.listener.OnSendAddPreOrderInformationListener;
import com.ifair.listener.OnSendDelFavoriteListener;
import com.ifair.listener.OnSendEditPreOrderInformationListener;
import com.ifair.listener.OnSendEditUserInformationListener;
import com.ifair.listener.OnSendForgetPasswordListener;
import com.ifair.listener.OnSendGoodsForgedListener;
import com.ifair.listener.OnSendLoginListener;
import com.ifair.listener.OnSendNewRegisterListener;
import com.ifair.listener.OnSendVerifyListener;
import com.ifair.module.AddFavoriteResponse;
import com.ifair.module.AddPreOrderInformationResponse;
import com.ifair.module.AdvertisingResponse;
import com.ifair.module.BaseResponse;
import com.ifair.module.DelFavoriteResponse;
import com.ifair.module.EditPreOrderInformationResponse;
import com.ifair.module.EditUserInformationResponse;
import com.ifair.module.ForgetPasswordResponse;
import com.ifair.module.LoginResponse;
import com.ifair.module.MagazineDetailResponse;
import com.ifair.module.MagazineGoodsDownResponse;
import com.ifair.module.MagazineResponse;
import com.ifair.module.MessageResponse;
import com.ifair.module.NFCBaseResponse;
import com.ifair.module.NFCVerifyResponse;
import com.ifair.module.NewMessageDetailResponse;
import com.ifair.module.NewRegisterResponse;
import com.ifair.module.NfcProductResponse;
import com.ifair.module.PreOrderResponse;
import com.ifair.module.PreorderInformationResponse;
import com.ifair.myUtil.AppUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 雲端API
 */

public class CloudAPI {


    private static final String TAG = "CloudAPI";

    private Gson gson;

    private CloudAPI() {
    }

    private static class CouldAPIInstance {
        private static CloudAPI INSTANCE = new CloudAPI();

    }

    public static CloudAPI getInstance() {
        return CouldAPIInstance.INSTANCE;
    }


    private OnSendLoginListener onSendLoginListener;

    private OnSendNewRegisterListener onSendNewRegisterListener;

    private OnSendEditUserInformationListener onSendEditUserInformationListener;

    private OnSendForgetPasswordListener onSendForgetPasswordListener;

    private OnNFCSendTagIdListener onNFCSendTagIdListener;

    private OnSendVerifyListener onSendVerifyListener;

    private OnSendGoodsForgedListener onSendGoodsForgedListener;

    private OnGetTrueGoodsListener onGetTrueGoodsListener;

    private OnGetLatestNewsListener onGetLatestNewsListener;

    private OnGetLatestNewsDetailListener onGetLatestNewsDetailListener;

    private OnGetAddLikeListener onGetAddLikeListener;

    private OnGetPreOrderDetailListener onGetPreOrderDetailListener;

    private OnGetMagazineListener onGetMagazineListener;

    private OnGetMagazineDetailListener onGetMagazineDetailListener;

    private OnSendAddFavoriteListener onSendAddFavoriteListener;

    private OnSendDelFavoriteListener onSendDelFavoriteListener;

    private OnGetPreOrderInformationListener onGetPreOrderInformationListener;

    private OnSendAddPreOrderInformationListener onSendAddPreOrderInformationListener;

    private OnSendEditPreOrderInformationListener onSendEditPreOrderInformationListener;

    private OnGetAdvertisingListener onGetAdvertisingListener;

    private OnChangePasswordListener onChangePasswordListener;

    private OnAddLikeGoodsListener onAddLikeGoodsListener;

    private OnAddMemberOrderListener onAddMemberOrderListener;

    private OnGetMagazineGoodsDownListener onGetMagazineGoodsDownListener;

    //---------------------------------------------------------------------------------------------------
    public void setOnSendForgetPasswordListener(OnSendForgetPasswordListener onSendForgetPasswordListener) {
        this.onSendForgetPasswordListener = onSendForgetPasswordListener;
    }

    public void setOnSendNewRegisterListener(OnSendNewRegisterListener onSendNewRegisterListener) {
        this.onSendNewRegisterListener = onSendNewRegisterListener;
    }

    public void setOnSendLoginListener(OnSendLoginListener onSendLoginListener) {
        this.onSendLoginListener = onSendLoginListener;
    }

    public void setOnGetMagazineListener(OnGetMagazineListener onGetMagazineListener) {
        this.onGetMagazineListener = onGetMagazineListener;
    }

    public void setOnGetMagazineDetailListener(OnGetMagazineDetailListener onGetMagazineDetailListener) {
        this.onGetMagazineDetailListener = onGetMagazineDetailListener;
    }

    public void setOnSendDelFavoriteListener(OnSendDelFavoriteListener onSendDelFavoriteListener) {
        this.onSendDelFavoriteListener = onSendDelFavoriteListener;
    }

    public void setOnSendAddFavoriteListener(OnSendAddFavoriteListener onSendAddFavoriteListener) {
        this.onSendAddFavoriteListener = onSendAddFavoriteListener;
    }

    public void setOnGetPreOrderInformationListener(OnGetPreOrderInformationListener onGetPreOrderInformationListener) {
        this.onGetPreOrderInformationListener = onGetPreOrderInformationListener;
    }

    public void setOnSendAddPreOrderInformationListener(OnSendAddPreOrderInformationListener onSendAddPreOrderInformationListener) {
        this.onSendAddPreOrderInformationListener = onSendAddPreOrderInformationListener;
    }

    public void setOnSendEditPreOrderInformationListener(OnSendEditPreOrderInformationListener onSendEditPreOrderInformationListener) {
        this.onSendEditPreOrderInformationListener = onSendEditPreOrderInformationListener;
    }

    public void setOnSendEditUserInformationListener(OnSendEditUserInformationListener listener) {
        this.onSendEditUserInformationListener = listener;
    }

    public void setOnNFCSendTagIdListener(OnNFCSendTagIdListener onNFCSendTagIdListener) {
        this.onNFCSendTagIdListener = onNFCSendTagIdListener;
    }

    public void setOnSendVerifyListener(OnSendVerifyListener listener) {
        this.onSendVerifyListener = listener;
    }

    public void setOnSendGoodsForgedListener(OnSendGoodsForgedListener listener) {
        this.onSendGoodsForgedListener = listener;
    }

    public void setOnGetTrueGoodsListener(OnGetTrueGoodsListener listener) {
        this.onGetTrueGoodsListener = listener;
    }

    public void setOnGetLatestNewsListener(OnGetLatestNewsListener latestNewsListener) {
        this.onGetLatestNewsListener = latestNewsListener;
    }

    public void setOnGetLatestNewsDetailListener(OnGetLatestNewsDetailListener listener) {
        this.onGetLatestNewsDetailListener = listener;
    }

    public void setOnGetAddLikeListener(OnGetAddLikeListener listener) {
        this.onGetAddLikeListener = listener;
    }

    public void setOnGetPreOrderDetailListener(OnGetPreOrderDetailListener listener) {
        this.onGetPreOrderDetailListener = listener;
    }

    public void setOnGetAdvertisingListener(OnGetAdvertisingListener onGetAdvertisingListener) {
        this.onGetAdvertisingListener = onGetAdvertisingListener;
    }

    public void setOnChangePasswordListener(OnChangePasswordListener listener) {
        this.onChangePasswordListener = listener;
    }

    public void setOnAddLikeGoodsListener(OnAddLikeGoodsListener listener) {
        this.onAddLikeGoodsListener = listener;
    }

    public void setOnAddMemberOrderListener(OnAddMemberOrderListener listener) {
        this.onAddMemberOrderListener = listener;
    }
    public void setOnGetMagazineGoodsDownListener(OnGetMagazineGoodsDownListener listener) {
        this.onGetMagazineGoodsDownListener = listener;
    }

    //---------------------------------------------------------------------------------------------------

    /**
     * 帳號登入
     *
     * @param email 帳號
     * @param pw    密碼
     */
    public void sendLogin(String email, String pw, String push_unique_id,  Activity activity) {
        Logger.d(push_unique_id);
        OkGo.post(ApiUri.API_SENT_Login)
                .tag(this)
                .params("email", email)
                .params("pw", pw)
                .params("push_unique_id",push_unique_id)
                .params("os", "android")
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendLoginListener != null) {
                            onSendLoginListener.onSendLoginMessage(s, null);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendLoginListener != null) {
                            onSendLoginListener.onSendLoginMessage("", e.getMessage());
                        }
                    }
                });
    }

    /**
     * 註冊帳號
     *
     * @param email         信箱
     * @param pw            密碼
     * @param name          密碼
     * @param birthday      生日
     * @param sex           性別(0：女，1：男)
     *                      imei          MAC Address
     * @param register_type 是否是第三方(1：是第三方，0：否第三方)
     * @param phone         手機號碼
     * @param area          地區
     */
    public void SendRegister(String email, String pw, String name, String birthday, String sex, String register_type, String phone, String area,String lat,String lon, Activity activity) {
        OkGo.post(ApiUri.API_SENT_Register)
                .tag(this)
                .params("email", email)
                .params("pw", pw)
                .params("name", name)
                .params("birthday", birthday)
                .params("sex", sex)
                .params("imei", AppUtil.getMacAddress())
                .params("register_type", register_type)
                .params("phone", phone)
                .params("area", area)
                .params("lat", lat)
                .params("lon", lon)
                .execute(new DialogCallback(activity) {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG, "onSuccess: " + s);
                        if (onSendNewRegisterListener != null) {
                            gson = new Gson();
                            NewRegisterResponse newRegisterResponse = gson.fromJson(s, NewRegisterResponse.class);
                            onSendNewRegisterListener.onSendNewRegisterMessage(newRegisterResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendNewRegisterListener != null) {
                            onSendNewRegisterListener.onSendNewRegisterMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 忘記密碼
     *
     * @param email 帳號
     */
    public void SendForgetPassword(String email, Activity activity) {
        OkGo.post(ApiUri.API_SEND_Forget_Password)
                .tag(this)
                .params("email", email)
                .execute(new DialogCallback(activity) {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (onSendForgetPasswordListener != null) {
                            gson = new Gson();
                            ForgetPasswordResponse forgetPasswordResponse = gson.fromJson(s, ForgetPasswordResponse.class);
                            onSendForgetPasswordListener.OnSendForgetPasswordMessage(forgetPasswordResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendForgetPasswordListener != null) {
                            onSendForgetPasswordListener.OnSendForgetPasswordMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 修改帳號
     *
     * @param email    帳號
     * @param name     姓名
     * @param birthday 生日
     * @param sex      性別(0：女，1：男)
     */
    public void SendEditUserInformation(String email, String name, String birthday, String sex,String phone,String area,String lat,String lon, Activity activity) {
        OkGo.post(ApiUri.API_SENT_Edit_User_Information)
                .tag(this)
                .params("email", email)
                .params("name", name)
                .params("birthday", birthday)
                .params("sex", sex)
                .params("changpw", "false")
                .params("phone", phone)
                .params("area", area)
                .params("lat",lat)
                .params("lon",lon)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (onSendEditUserInformationListener != null) {
                            onSendEditUserInformationListener.onSendEditUserInformationMessage(s, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (onSendEditUserInformationListener != null) {
                            onSendEditUserInformationListener.onSendEditUserInformationMessage("", e.getMessage());
                        }
                    }
                });
    }

    /**
     * 更改密碼
     *
     * @param oldPassword
     * @param newPassword
     * @param activity
     */
    public void changePassword(String email, String oldPassword, String newPassword, Activity activity) {

        OkGo.post(ApiUri.API_SENT_Edit_User_Information)
                .tag(this)
                .params("email", email)
                .params("oldpw", oldPassword)
                .params("pw", newPassword)
                .params("changpw", "true")
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.json(s);
                        if (onChangePasswordListener != null) {
                            gson = new Gson();
                            BaseResponse baseResponse = gson.fromJson(s, BaseResponse.class);
                            onChangePasswordListener.changePasswordResponse(baseResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onChangePasswordListener != null) {
                            onChangePasswordListener.changePasswordResponse(null, e.getMessage());
                        }
                    }
                });
    }


    /**
     * 取得商品雜誌
     *
     * @param email 當空值是商品雜誌，非空值是我的最愛
     */
    public void getMagazine(String email, Activity activity) {
        OkGo.post(ApiUri.API_GET_Magazine)
                .tag(this)
                .params("email", email)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onGetMagazineListener != null) {
                            onGetMagazineListener.onGetMagazineMessage(null, e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onGetMagazineListener != null) {
                            gson = new Gson();
                            MagazineResponse magazineResponse = gson.fromJson(s, MagazineResponse.class);
                            onGetMagazineListener.onGetMagazineMessage(magazineResponse, "");
                        }
                    }
                });
    }

    /**
     * 商品雜誌內頁
     *
     * @param goods_id
     */

    public void GetMagazineDetail(String unique_value, String goods_id, Activity activity) {
        OkGo.post(ApiUri.API_GET_Magazine_Detail)
                .tag(this)
                .params("goods_id", goods_id)
                .params("unique_value", unique_value)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        if (onGetMagazineDetailListener != null) {
                            gson = new Gson();
                            MagazineDetailResponse magazineDetailResponse = gson.fromJson(s, MagazineDetailResponse.class);
                            onGetMagazineDetailListener.GetMagazineDetailMessage(magazineDetailResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onGetMagazineDetailListener != null) {
                            onGetMagazineDetailListener.GetMagazineDetailMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 預購商品內頁
     *
     * @param goods_id 商品編號
     */
    public void GetPreOrderDetail(String goods_id, Activity activity) {
        OkGo.post(ApiUri.API_GET_PreOrder_Detail)
                .tag(this)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onGetPreOrderDetailListener != null) {
                            gson = new Gson();
                            PreOrderResponse preOrderResponse = gson.fromJson(s, PreOrderResponse.class);
                            onGetPreOrderDetailListener.getPreOrderDetailMessage(preOrderResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (onGetPreOrderDetailListener != null) {
                            onGetPreOrderDetailListener.getPreOrderDetailMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 商品新增我的愛最愛
     *
     * @param email    email帳號
     * @param goods_id 商品編號
     */
    public void SetAddFavorite(String email, String goods_id, Activity activity) {
        OkGo.post(ApiUri.API_SEND_Add_Favorite)
                .tag(this)
                .params("unique_value", email)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(activity, "") {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendAddFavoriteListener != null) {
                            gson = new Gson();
                            AddFavoriteResponse addFavoriteResponse = gson.fromJson(s, AddFavoriteResponse.class);
                            onSendAddFavoriteListener.SendAddFavoriteMessage(addFavoriteResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendAddFavoriteListener != null) {
                            onSendAddFavoriteListener.SendAddFavoriteMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 商品刪除我的愛最愛
     *
     * @param email    email帳號
     * @param goods_id 商品編號
     */
    public void SetDelFavorite(String email, String goods_id, Activity activity) {
        OkGo.post(ApiUri.API_SEND_Delete_Favorite)
                .tag(this)
                .params("unique_value", email)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(activity, "") {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendDelFavoriteListener != null) {
                            gson = new Gson();
                            DelFavoriteResponse delFavoriteResponse = gson.fromJson(s, DelFavoriteResponse.class);
                            onSendDelFavoriteListener.SendDelFavoriteMessage(delFavoriteResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendDelFavoriteListener != null) {
                            onSendDelFavoriteListener.SendDelFavoriteMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 取得預購資訊列表
     *
     * @param email 帳號
     */
    public void getProOrderInformation(String email, Activity activity) {
        OkGo.post(ApiUri.API_GET_PreOrder_Information)
                .tag(this)
                .params("email", email)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onGetPreOrderInformationListener != null) {
                            gson = new Gson();
                            PreorderInformationResponse preorderInformationResponse = gson.fromJson(s, PreorderInformationResponse.class);
                            onGetPreOrderInformationListener.getPreOrderInformationMessage(preorderInformationResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onGetPreOrderInformationListener != null) {
                            onGetPreOrderInformationListener.getPreOrderInformationMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 儲存預購資訊
     *
     * @param email   帳號
     * @param name    姓名
     * @param type    常用資料名稱，可單選一個。
     * @param phone   電話
     * @param address 地址
     */
    public void setAddProOrderInformation(String email, String name, String type, String phone, String address, Activity activity) {
        OkGo.post(ApiUri.API_SEND_ADD_PreOrder_Information)
                .tag(this)
                .params("email", email)
                .params("name", name)
                .params("type", type)
                .params("phone", phone)
                .params("address", address)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendAddPreOrderInformationListener != null) {
                            gson = new Gson();
                            AddPreOrderInformationResponse addPreOrderInformationResponse = gson.fromJson(s, AddPreOrderInformationResponse.class);
                            onSendAddPreOrderInformationListener.setAddPreOrderInformationMessage(addPreOrderInformationResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendAddPreOrderInformationListener != null) {
                            onSendAddPreOrderInformationListener.setAddPreOrderInformationMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 編輯預購資訊
     *
     * @param id      消費者預購地址編號( order_address_id)
     * @param email   帳號
     * @param name    姓名
     * @param type    常用資料名稱，可單選一個。
     * @param phone   電話
     * @param address 地址
     */
    public void setEditProOrderInformation(String id, String email, String name, String type, String phone, String address, Activity activity) {
        OkGo.post(ApiUri.API_SEND_EDIT_PreOrder_Information)
                .tag(this)
                .params("id", id)
                .params("email", email)
                .params("name", name)
                .params("type", type)
                .params("phone", phone)
                .params("address", address)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendEditPreOrderInformationListener != null) {
                            gson = new Gson();
                            EditPreOrderInformationResponse editPreOrderInformationResponse = gson.fromJson(s, EditPreOrderInformationResponse.class);
                            onSendEditPreOrderInformationListener.setEditPreOrderInformationMessage(editPreOrderInformationResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onSendEditPreOrderInformationListener != null) {
                            onSendEditPreOrderInformationListener.setEditPreOrderInformationMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 輪播廣告頁
     */
    public void getAdvertising(Activity activity) {
        OkGo.post(ApiUri.API_GET_Advertising)
                .tag(this)
                .execute(new DialogCallback(activity) {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        if (onGetAdvertisingListener != null) {
                            gson = new Gson();
                            AdvertisingResponse advertisingResponse = gson.fromJson(s, AdvertisingResponse.class);
                            onGetAdvertisingListener.GetAdvertisingMessage(advertisingResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onGetAdvertisingListener != null) {
                            onGetAdvertisingListener.GetAdvertisingMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * APP送TagID至主機
     *
     * @param email 信箱
     * @param tagId NFC TagId
     */
    public void sendTAGId(String email, String tagId, String area,String lat,String lon, IsoDep isoDepCurrentTag) {
        OkGo.post(ApiUri.API_SEND_TAGID)
                .tag(this)
                .params("email", email)
                .params("tagid", tagId)
                .params("area", area)
                .params("lat", lat)
                .params("lon", lon)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.json(s);
                        if (onNFCSendTagIdListener != null) {
                            gson = new Gson();
                            NFCBaseResponse nfcBaseResponse = gson.fromJson(s, NFCBaseResponse.class);
                            onNFCSendTagIdListener.onNFCSendTagIdMessage(nfcBaseResponse, "", tagId, isoDepCurrentTag);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                        if (onNFCSendTagIdListener != null) {
                            onNFCSendTagIdListener.onNFCSendTagIdMessage(null, e.toString(), tagId, isoDepCurrentTag);
                        }
                    }
                });
    }

    /**
     * Tag內驗證碼資料傳送至雲端，比對回傳真偽結果。
     *
     * @param email      信箱
     * @param tagId      NFC TagId
     * @param verifyCode Tag內的資料（驗證碼）
     */
    public void sendVerify(String email, String tagId, String verifyCode) {
        Log.i(TAG, "sendVerify: tagId: " + tagId + ", verifyCode:" + verifyCode);
        OkGo.post(ApiUri.API_SEND_VERIFY)
                .tag(this)
                .params("email", email)
                .params("tagid", tagId)
                .params("verifycode", verifyCode)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.d(s);
                        if (onSendVerifyListener != null) {
                            gson = new Gson();
                            NFCVerifyResponse nfcVerifyResponse = gson.fromJson(s, NFCVerifyResponse.class);
                            onSendVerifyListener.onSendVerifyMessage(nfcVerifyResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onSendVerifyListener != null) {
                            onSendVerifyListener.onSendVerifyMessage(null, "");
                        }
                    }
                });
    }

    /**
     * NFC產品舉報偽品
     *
     * @param email        信箱
     * @param forged_image 舉報照片
     * @param location     購買地點
     * @param note         原因
     * @param mActivity
     */
    public void sendGoodsForged(String email, String forged_image, String location, String note, Activity mActivity) {
        OkGo.post(ApiUri.API_SEND_GOODS_FORGED)
                .tag(this)
                .params("email", email)
                .params("location", location)
                .params("note", note)
                .params("forged_image", new File(forged_image))
                .execute(new DialogCallback(mActivity, "上傳中") {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        if (onSendGoodsForgedListener != null) {
                            gson = new Gson();
                            BaseResponse baseResponse = gson.fromJson(s, BaseResponse.class);
                            onSendGoodsForgedListener.onSendGoodsForgedMessage(baseResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (onSendGoodsForgedListener != null) {
                            onSendGoodsForgedListener.onSendGoodsForgedMessage(null, "");
                        }
                    }
                });
    }

    /**
     * 當NFC辦識結果屬於真品時
     *
     * @param goods_code 商品ID
     * @param mActivity
     */
    public void getTrueGoods(String goods_code, Activity mActivity) {
        OkGo.post(ApiUri.API_Get_true_goods)
                .tag(this)
                .params("goods_code", goods_code)
                .execute(new DialogCallback(mActivity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        if (onGetTrueGoodsListener != null) {
                            gson = new Gson();
                            NfcProductResponse nfcProductResponse = gson.fromJson(s, NfcProductResponse.class);
                            onGetTrueGoodsListener.getTrueGoodsMessage(nfcProductResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onGetTrueGoodsListener != null) {
                            onGetTrueGoodsListener.getTrueGoodsMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 取得最新消息
     *
     * @param mac_address
     */
    public void getLatestNews(String mac_address) {
        OkGo.post(ApiUri.API_Get_Latest_news)
                .tag(this)
                .params("mac_address", mac_address)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.d(s);
                        gson = new Gson();
                        MessageResponse messageResponse = gson.fromJson(s, MessageResponse.class);
                        if (onGetLatestNewsListener != null) {
                            onGetLatestNewsListener.getLatestNewsMessage(messageResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onGetLatestNewsListener != null) {
                            onGetLatestNewsListener.getLatestNewsMessage(null, "");
                        }
                    }
                });
    }

    /**
     * 取得最新消息詳細資料
     *
     * @param mac_address
     * @param message_id
     * @param activity
     */
    public void getLatestNewsDetail(String mac_address, String message_id, Activity activity) {
        OkGo.post(ApiUri.API_Get_Latest_news_detail)
                .tag(this)
                .params("mac_address", mac_address)
                .params("message_id", message_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        gson = new Gson();
                        NewMessageDetailResponse messageDetailResponse = gson.fromJson(s, NewMessageDetailResponse.class);
                        if (onGetLatestNewsDetailListener != null) {
                            onGetLatestNewsDetailListener.getLatestNewsDetailMessage(messageDetailResponse, "");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onGetLatestNewsDetailListener != null) {
                            onGetLatestNewsDetailListener.getLatestNewsDetailMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 新增讚數
     *
     * @param message_id
     * @param unique_value imei或email帳號
     */
    public void addLikeMessage(String message_id, String unique_value) {
        OkGo.post(ApiUri.API_Add_like_message)
                .tag(this)
                .params("unique_value", unique_value)
                .params("message_id", message_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.d(s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (onGetAddLikeListener != null) {
                                onGetAddLikeListener.getAddLikeMessage(jsonObject.getInt("code"), "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onGetAddLikeListener != null) {
                            onGetAddLikeListener.getAddLikeMessage(0, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 取得預購商品資訊
     *
     * @param goods_id
     * @param mActivity
     */
    public void getPreOrderDetail(String goods_id, Activity mActivity) {
        OkGo.post(ApiUri.API_Get_PreOrder)
                .tag(this)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(mActivity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        if (onGetPreOrderDetailListener != null) {
                            gson = new Gson();
                            PreOrderResponse preOrderResponse = gson.fromJson(s, PreOrderResponse.class);
                            onGetPreOrderDetailListener.getPreOrderDetailMessage(preOrderResponse, "");
                        }


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onGetPreOrderDetailListener != null) {
                            onGetPreOrderDetailListener.getPreOrderDetailMessage(null, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 商品雜誌內頁，送出按讚
     *
     * @param unique_value
     * @param goods_id
     * @param mActivty
     */
    public void addLikeGoods(String unique_value, String goods_id, Activity mActivty) {
        OkGo.post(ApiUri.API_add_like_goods)
                .tag(this)
                .params("unique_value", unique_value)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(mActivty, "") {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(s, BaseResponse.class);
                        if (onAddLikeGoodsListener != null) {
                            onAddLikeGoodsListener.addLikeGoodsMessage(baseResponse.getCode(), "");
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        if (onAddLikeGoodsListener != null) {
                            onAddLikeGoodsListener.addLikeGoodsMessage(0, e.getMessage());
                        }
                    }
                });
    }

    /**
     * 存預購商品訂單
     *
     * @param unique_value 手機唯一碼
     * @param email        信箱
     * @param name         姓名
     * @param phone        電話
     * @param address      地址
     * @param goods_code   商品編號
     * @param good_name    商品名稱
     * @param price        當前售價
     * @param amount       購買數量
     * @param total_price  限購金額
     */
    public void addMemberOrder(String unique_value, String email, String name, String phone, String address,
                               String goods_code, String good_name, String price, String amount,
                               String total_price, Activity mActivity) {
        Log.i(TAG, "addMemberOrder: " + unique_value + "," + email + "," + name + "," + phone + "," + address
                + "," + goods_code + "," + good_name + "," + price + "," + amount + "," + total_price);

        OkGo.post(ApiUri.API_add_member_order)
                .tag(this)
                .params("unique_value", unique_value)
                .params("email", email)
                .params("name", name)
                .params("phone", phone)
                .params("address", address)
                .params("goods_code", goods_code)
                .params("goods_name", good_name)
                .params("price", price)
                .params("amount", amount)
                .params("total_price",total_price )
                .execute(new DialogCallback(mActivity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.d(s);
                        gson = new Gson();
                        BaseResponse baseResponse = gson.fromJson(s, BaseResponse.class);
                        if (onAddMemberOrderListener != null)
                            onAddMemberOrderListener.addMemberOrderResponse(baseResponse, "");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (onAddMemberOrderListener != null)
                            onAddMemberOrderListener.addMemberOrderResponse(null, e.getMessage());
                    }
                });

    }

    /**
     * 取得推薦商品列表
     *
     * @param email 帳號
     */
    public void getMagazineGoodsDown(String email, String firm_id, Activity activity) {
        OkGo.post(ApiUri.API_GET_GOODS_DOWN)
                .tag(this)
                .params("email", email)
                .params("firm_id", firm_id)
                .execute(new DialogCallback(activity) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.json(s);
                if (onGetMagazineGoodsDownListener != null) {
                    gson = new Gson();
                    MagazineGoodsDownResponse magazineGoodsDownResponse = gson.fromJson(s, MagazineGoodsDownResponse.class);
                    onGetMagazineGoodsDownListener.getMagazineGoodsDown(magazineGoodsDownResponse, "");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.d(e.toString());
                if (onGetMagazineGoodsDownListener != null) {
                    onGetMagazineGoodsDownListener.getMagazineGoodsDown(null, e.getMessage());
                }
            }
        });
    }

    /**
     * 新增廣告點擊次數
     *
     */
    public void onAddAdClick(String unique_value, String ad_id, Activity activity) {
        OkGo.post(ApiUri.API_ADD_AD_CLICK)
                .tag(this)
                .params("unique_value", unique_value)
                .params("ad_id", ad_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                    }
                });
    }

    /**
     *送出最新消息的分享次數
     */
    public void onAddShareMessage(String unique_value, String message_id, Activity activity) {
        Log.d(TAG, "onAddShareMessage: message_id="+message_id+" unique_value="+unique_value);
        OkGo.post(ApiUri.API_ADD_SHARE_MESSAGE)
                .tag(this)
                .params("unique_value", unique_value)
                .params("message_id", message_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                    }
                });
    }

    /**
     *送出商品的分享次數
     */
    public void onAddShareGoods(String unique_value, String goods_id, Activity activity) {
        Log.d(TAG, "onAddShareGoods: goods_id="+goods_id+" unique_value="+unique_value);
        OkGo.post(ApiUri.API_ADD_SHARE_GOODS)
                .tag(this)
                .params("unique_value", unique_value)
                .params("goods_id", goods_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                    }
                });
    }

    /**
     *送出推播消息-點擊數
     */
    public void onAddPushLikeMessage(String unique_value, String message_id, Activity activity) {
        Log.d(TAG, "onAddPushLikeMessage: message_id="+message_id+" unique_value="+unique_value);
        OkGo.post(ApiUri.API_PUSH_LIKE_MESSAGE)
                .tag(this)
                .params("unique_value", unique_value)
                .params("message_id", message_id)
                .execute(new DialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.toString());
                    }
                });
    }

}
