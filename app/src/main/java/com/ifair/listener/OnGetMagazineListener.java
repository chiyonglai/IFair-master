package com.ifair.listener;

import com.ifair.module.MagazineResponse;

/**
 * Created by Ideabus on 2017/2/17.
 */

public interface OnGetMagazineListener {
    void onGetMagazineMessage(MagazineResponse magazineResponse, String error);
}
