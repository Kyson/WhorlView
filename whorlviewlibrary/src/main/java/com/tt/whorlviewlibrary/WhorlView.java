package com.tt.whorlviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 类似螺纹的加载view<br></>
 * 可以自定义的属性：颜色、旋转速度<p/>
 * Created by Kyson on 2015/8/9.
 */
public class WhorlView extends View {
    private int[] mLayerColors = new int[3];
    private int mCircleTime;

    public WhorlView(Context context) {
        this(context, null, 0, 0);
    }

    public WhorlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public WhorlView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WhorlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Resources res = getResources();
        final int defaultSmallColor = res.getColor(R.color.material_red);
        final int defaultMiddleColor = res.getColor(R.color.material_green);
        final int defaultBigColor = res.getColor(R.color.material_blue);
        //默认最小的一秒一圈
        final int defaultCircleTime = 1000;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.Whorl_Style);
            mLayerColors[0] = typedArray.getColor(R.styleable.Whorl_Style_smallWhorlColor, defaultSmallColor);
            mLayerColors[1] = typedArray.getColor(R.styleable.Whorl_Style_middleWhorlColor, defaultMiddleColor);
            mLayerColors[2] = typedArray.getColor(R.styleable.Whorl_Style_bigWhorlColor, defaultBigColor);
            mCircleTime = typedArray.getInt(R.styleable.Whorl_Style_circleTime, defaultCircleTime);
        } else {
            mLayerColors[0] = defaultSmallColor;
            mLayerColors[1] = defaultMiddleColor;
            mLayerColors[2] = defaultBigColor;
            mCircleTime = defaultCircleTime;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
