package com.ifair.listener;

import com.ifair.module.MessageResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnGetLatestNewsListener {
    void getLatestNewsMessage(MessageResponse messageResponse, String error);
}
