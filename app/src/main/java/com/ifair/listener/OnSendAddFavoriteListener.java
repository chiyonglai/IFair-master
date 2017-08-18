package com.ifair.listener;

import com.ifair.module.AddFavoriteResponse;

/**
 * 商品加到我的最愛
 */

public interface OnSendAddFavoriteListener {
    void SendAddFavoriteMessage(AddFavoriteResponse addFavoriteResponse, String Error);
}
