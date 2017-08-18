package com.ifair.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 功能
 */

public class MagazineGoodsDownAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragmentList;

    public MagazineGoodsDownAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override public float getPageWidth(int position) { return(0.45f); }

}