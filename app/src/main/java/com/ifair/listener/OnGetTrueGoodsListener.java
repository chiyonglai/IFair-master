package com.ifair.listener;

import com.ifair.module.NfcProductResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnGetTrueGoodsListener {
    void getTrueGoodsMessage(NfcProductResponse nfcProductResponse, String error);
}
