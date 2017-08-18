package com.ifair.listener;

import com.ifair.module.PreOrderResponse;

/**
 * 監聽雲端回傳值
 */
public interface OnGetPreOrderDetailListener {
    void getPreOrderDetailMessage(PreOrderResponse preOrderResponse, String error);
}
