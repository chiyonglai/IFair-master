package com.ifair.listener;

import com.ifair.module.EditPreOrderInformationResponse;

/**
 *  修改預購資訊
 */

public interface OnSendEditPreOrderInformationListener {
    void setEditPreOrderInformationMessage(EditPreOrderInformationResponse editPreOrderInformationResponse, String Error);
}
