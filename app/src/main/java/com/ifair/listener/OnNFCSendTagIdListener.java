package com.ifair.listener;

import android.nfc.tech.IsoDep;

import com.ifair.module.NFCBaseResponse;

/**
 * 將TAGID送至雲端，監聽雲端回傳值
 */
public interface OnNFCSendTagIdListener {
    void onNFCSendTagIdMessage(NFCBaseResponse nfcBaseResponse, String error, String tag_id, IsoDep isoDepCurrentTag);
}
