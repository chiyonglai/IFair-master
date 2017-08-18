package com.ifair.eventModule;

/**
 * NFC事件通知
 */

public class NfcEvent {

    private String goodCode;

    public NfcEvent(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodCode() {
        return goodCode;
    }
}
