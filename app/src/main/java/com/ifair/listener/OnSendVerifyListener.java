package com.ifair.listener;

import com.ifair.module.NFCBaseResponse;
import com.ifair.module.NFCVerifyResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnSendVerifyListener {
    void onSendVerifyMessage(NFCVerifyResponse nfcVerifyResponse, String error);
}
