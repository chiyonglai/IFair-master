package com.ifair.myUtil;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.ifair.myAnimators.CustomBounceInterpolator;

/**
 * 功能
 */

public class MyAnimationUtils {

    /**
     * 按讚或愛心的動畫
     */
    public static ScaleAnimation imageScaleAnimation() {
        ScaleAnimation scale = new ScaleAnimation(0,1,0,1,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setInterpolator(new CustomBounceInterpolator());
        scale.setDuration(1000);

        return scale;
    }

    /**
     * Nfc 真品，圖片放大
     */
    public static ScaleAnimation nfcAnimation() {
        ScaleAnimation scale = new ScaleAnimation(3,1,3,1,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(800);
        scale.setInterpolator(new CustomBounceInterpolator());

        return scale;

    }

    /**
     * Nfc 真品，字 Alpha
     */
    public static AlphaAnimation nfcTextAnimation() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(100);

        return alphaAnimation;

    }
}
