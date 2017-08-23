package com.ifair.listener;

import com.ifair.module.PushSWITCResponse;

/**
 * 獲得Push開關
 */

public interface OnGetPushSWITCListener {
    void onGetPushSWITCMessage(String json, String error);
}
