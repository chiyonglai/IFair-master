package com.ifair.listener;

import com.ifair.module.NewMessageDetailResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnGetLatestNewsDetailListener {
    void getLatestNewsDetailMessage(NewMessageDetailResponse messageDetailResponse, String error);
}
