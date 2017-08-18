package com.ifair.listener;

import com.ifair.module.BaseResponse;

/**
 * 功能
 */

public interface OnAddMemberOrderListener {
    void addMemberOrderResponse(BaseResponse baseResponse, String error);
}
