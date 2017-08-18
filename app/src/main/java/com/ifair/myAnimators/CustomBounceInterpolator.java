package com.ifair.myAnimators;

import android.view.animation.Interpolator;

/**
 * 功能
 */

public class CustomBounceInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        if (input == 0.0f || input == 1.0f)
            return input;
        else {
            float p = 0.6f;
            float two_pi = (float) (Math.PI * 2.7f);
            return (float) Math.pow(2.0f, -10.0f * input) * (float) Math.sin((input - (p/5.0f)) * two_pi/p) + 1.0f;
        }
    }


}
