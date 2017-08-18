package com.ifair.listener;

import com.ifair.module.BaseResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnSendGoodsForgedListener {
    void onSendGoodsForgedMessage(BaseResponse baseResponse, String error);
}
