package com.ifair.listener;

import com.ifair.module.MagazineGoodsDownResponse;
import com.ifair.module.PreorderInformationResponse;

/**
 * 取得預購資訊預存列表
 */

public interface OnGetMagazineGoodsDownListener {
    void getMagazineGoodsDown(MagazineGoodsDownResponse magazineGoodsDownResponse, String Error);
}
