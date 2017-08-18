package com.ifair.listener;

import com.ifair.module.AddPreOrderInformationResponse;

/**
 * 儲存預購資訊
 */

public interface OnSendAddPreOrderInformationListener {
    void setAddPreOrderInformationMessage(AddPreOrderInformationResponse addPreOrderInformationResponse, String Error);
}
