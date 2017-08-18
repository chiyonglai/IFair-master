package com.ifair.listener;

import com.ifair.module.MagazineDetailResponse;

/**
 * Created by Ideabus on 2017/2/21.
 */

public interface OnGetMagazineDetailListener {
    void GetMagazineDetailMessage(MagazineDetailResponse magazineDetailResponse, String Error);
}
