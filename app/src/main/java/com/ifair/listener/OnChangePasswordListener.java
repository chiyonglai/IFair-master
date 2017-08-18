package com.ifair.listener;

import com.ifair.module.BaseResponse;

/**
 * 功能
 */
public interface OnChangePasswordListener {
    void changePasswordResponse(BaseResponse baseResponse, String error);
}
