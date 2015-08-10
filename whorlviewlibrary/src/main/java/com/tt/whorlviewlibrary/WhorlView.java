package com.tt.whorlviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 类似螺纹的加载view<br></>
 * 可以自定义的属性：颜色、旋转速度（X弧度/s）<p/>
 * Created by Kyson on 2015/8/9.
 */
public class WhorlView extends View {
    private static final int CIRCLE_NUM = 3;

    public static final int FAST = 1;
    public static final int MEDIUM = 0;
    public static final int SLOW = 2;

    private static final int PARALLAX_FAST = 60;
    private static final int PARALLAX_MEDIUM = 72;
    private static final int PARALLAX_SLOW = 90;

    private static final float SWEEP_ANGLE = 90f;

    private static final float STOKE_WIDTH = 5f;

    private static final long REFRESH_DURATION = 16L;

    private long mCircleTime;
    private int[] mLayerColors = new int[CIRCLE_NUM];
    private int mCircleSpeed;
    private int mParallaxSpeed;

    public WhorlView(Context context) {
        this(context, null, 0);
    }

    public WhorlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhorlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        final int defaultSmallColor = res.getColor(R.color.material_red);
        final int defaultMiddleColor = res.getColor(R.color.material_green);
        final int defaultBigColor = res.getColor(R.color.material_blue);
        //默认外层最慢180度/s
        final int defaultCircleSpeed = 270;
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(
                    attrs, R.styleable.WhorlView_Style);
            mLayerColors[0] = typedArray.getColor(R.styleable.WhorlView_Style_WhorlView_SmallWhorlColor, defaultSmallColor);
            mLayerColors[1] = typedArray.getColor(R.styleable.WhorlView_Style_WhorlView_MiddleWhorlColor, defaultMiddleColor);
            mLayerColors[2] = typedArray.getColor(R.styleable.WhorlView_Style_WhorlView_BigWhorlColor, defaultBigColor);
            mCircleSpeed = typedArray.getInt(R.styleable.WhorlView_Style_WhorlView_CircleSpeed, defaultCircleSpeed);
            int index = typedArray.getInt(R.styleable.WhorlView_Style_WhorlView_Parallax, 0);
            setParallax(index);
            typedArray.recycle();
        } else {
            mLayerColors[0] = defaultSmallColor;
            mLayerColors[1] = defaultMiddleColor;
            mLayerColors[2] = defaultBigColor;
            mCircleSpeed = defaultCircleSpeed;
            mParallaxSpeed = PARALLAX_MEDIUM;
        }
    }


    private void setParallax(int index) {
        switch (index) {
            case FAST:
                mParallaxSpeed = PARALLAX_FAST;
                break;
            case MEDIUM:
                mParallaxSpeed = PARALLAX_MEDIUM;
                break;
            case SLOW:
                mParallaxSpeed = PARALLAX_SLOW;
                break;
            default:
                throw new IllegalStateException("no such parallax type");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < CIRCLE_NUM; i++) {
            float angle = (mCircleSpeed + mParallaxSpeed * (CIRCLE_NUM - i - 1)) * mCircleTime * 0.001f;
            drawArc(canvas, i, angle);
        }
    }

    private boolean isCircling;

    /**
     * 旋转开始 <功能简述>
     */
    public void start() {
        isCircling = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                mCircleTime = 0L;
                while (isCircling) {
                    invalidateWrap();
                    mCircleTime = mCircleTime + REFRESH_DURATION;
                    try {
                        Thread.sleep(REFRESH_DURATION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        isCircling = false;
        mCircleTime = 0L;
        invalidateWrap();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void invalidateWrap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postInvalidateOnAnimation();
        } else {
            postInvalidate();
        }
    }

    /**
     * 画弧
     *
     * @param canvas
     * @param index      由内而外
     * @param startAngle
     */
    private void drawArc(Canvas canvas, int index, float startAngle) {
        Paint paint = checkArcPaint(index);
        //最大圆是view的边界
        RectF oval = checkRectF(calcuRadiusRatio(index));
        canvas.drawArc(oval, startAngle, SWEEP_ANGLE, false, paint);
    }

    private Paint mArcPaint;

    private Paint checkArcPaint(int index) {
        if (mArcPaint == null) {
            mArcPaint = new Paint();
        } else {
            mArcPaint.reset();
        }
        mArcPaint.setColor(mLayerColors[index]);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(STOKE_WIDTH);
        mArcPaint.setAntiAlias(true);
        return mArcPaint;
    }

    private RectF mOval;

    private RectF checkRectF(float radiusRatio) {
        if (mOval == null) {
            mOval = new RectF();
        }
        float start = getMinLength() * 0.5f * (1 - radiusRatio) + STOKE_WIDTH;
        float end = getMinLength() - start;
        mOval.set(start, start, end, end);
        return mOval;
    }

    private static final float RADIUS_RATIO_P = 0.2f;

    /**
     * 计算每一圈的半径比例
     *
     * @param index
     * @return
     */
    private float calcuRadiusRatio(int index) {
        return 1f - (CIRCLE_NUM - index - 1) * RADIUS_RATIO_P;
    }

    private int getMinLength() {
        return Math.min(getWidth(), getHeight());
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
