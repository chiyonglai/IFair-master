package com.ifair.listener;

import com.ifair.module.AdvertisingResponse;

/**
 *廣告頁監聽
 */

public interface OnGetAdvertisingListener {
    void GetAdvertisingMessage(AdvertisingResponse AdvertisingResponse, String Error);
}
