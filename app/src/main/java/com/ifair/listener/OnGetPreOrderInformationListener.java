package com.ifair.listener;

import com.ifair.module.PreorderInformationResponse;

/**
 * 取得預購資訊預存列表
 */

public interface OnGetPreOrderInformationListener {
    void getPreOrderInformationMessage(PreorderInformationResponse preorderInformationResponse, String Error);
}
