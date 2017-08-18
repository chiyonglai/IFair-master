package com.ifair.myAnimators;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;

/**
 * 功能
 */

public class SlideInTopAnimationAdapter extends AnimationAdapter {

    public SlideInTopAnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(adapter);
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", 0, view.getMeasuredHeight())
        };
    }
}
