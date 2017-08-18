package com.ifair.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ifair.R;

public class CustomTitleView extends View {

    private RectF rectF = new RectF();
    private Paint backgroundPaint = new Paint();
    private Paint linePaint = new Paint();
    private RectF shadowRectF = new RectF();

    private static final float RADIUS = 80;
    private float MARGIN;

    private float leftMargin, rightMargin;
    private float radius;

    private boolean isExpandAnim, isNarrowAnim;

    private int width;

    public CustomTitleView(Context context) {
        super(context);
        init();
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 設定寬度
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
        MARGIN = (float) (width * 0.2);
        leftMargin = MARGIN;
        rightMargin = width - MARGIN;
        invalidate();
    }

    /**
     * 初始化設定
     */
    private void init(){

        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(getResources().getColor(R.color.color_news_background));

        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(getResources().getColor(R.color.color_shadow2));

        radius = RADIUS;
        rectF.set(leftMargin, 10, rightMargin, getResources().getDimension(R.dimen.header_bar_height));

//        shadowPaint.setAntiAlias(true);
//        shadowPaint.setColor(getResources().getColor(R.color.color_shadow1));
//        shadowPaint.setShadowLayer(20, 0,0, getResources().getColor(R.color.color_shadow2));
//        shadowRectF.set(leftMargin, 0, rightMargin, getResources().getDimension(R.dimen.header_bar_height));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            //因為setShadowLayer不支援硬體加速，所以如果要使用，須將硬體加速關掉
//            setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.left = leftMargin;
        rectF.right = rightMargin;
        shadowRectF.left = leftMargin;
        shadowRectF.right = rightMargin;

        canvas.drawRoundRect(rectF, radius, radius < 0 ? 0 : radius, backgroundPaint);
        canvas.drawRoundRect(rectF, radius, radius < 0 ? 0 : radius, linePaint);
//        Log.i("!!@@@@@@@@!!!!!", "onDraw: " + leftMargin + "," + rightMargin + "," + radius);
    }

    /**
     * 放大
     */
    public void setExpandAnim(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isExpandAnim)
                    return;
                isExpandAnim = true;
                isNarrowAnim = false;
                while(isExpandAnim && (radius > 0 || leftMargin > 0 || rightMargin < width)){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (radius > 0) {
                        //如果沒判斷大於0，會一直閃
                        radius = radius < 0 ? 0 : --radius;
                    }
                    leftMargin = leftMargin < 0 ? 0 : --leftMargin;
                    rightMargin = rightMargin > width ? width : ++rightMargin;
                    postInvalidate();
                }
                isExpandAnim = false;
            }
        }).start();
    }

    /**
     * 縮小
     */
    public void setNarrowAnim(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isNarrowAnim)
                    return;
                isNarrowAnim = true;
                isExpandAnim = false;
                while(isNarrowAnim && (radius < RADIUS || leftMargin < MARGIN || rightMargin > width - MARGIN)){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    radius = radius > RADIUS ? RADIUS : ++radius;
                    leftMargin = leftMargin > MARGIN ? MARGIN : ++leftMargin;
                    rightMargin = rightMargin < width - MARGIN ? width - MARGIN : --rightMargin;
                    postInvalidate();
                }
                isNarrowAnim = false;
            }
        }).start();
    }
}
