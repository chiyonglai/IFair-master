package com.ifair.listener;

import com.ifair.module.DelFavoriteResponse;

/**
 * 商品從我的最愛移除
 */

public interface OnSendDelFavoriteListener {
    void SendDelFavoriteMessage(DelFavoriteResponse delFavoriteResponse, String Error);
}
