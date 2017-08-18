package com.ifair.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ifair.R;

/**
 * Created by luguanyu on 2017/1/19.
 */

public class BounceCircleView extends View{

    private Paint paint = new Paint();


    public BounceCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParam();
    }

    private void initParam() {
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, 80, paint);
    }
}
