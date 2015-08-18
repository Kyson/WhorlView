package com.tt.whorlviewlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 类似螺纹的加载view<br>
 * 可以自定义的属性：颜色、旋转速度（X弧度/s）<br>
 * Created by Kyson on 2015/8/9.<br>
 * www.hikyson.cn<br>
 */
public class WhorlView extends View {
    private static final String COLOR_SPLIT = "_";

    public static final int FAST = 1;
    public static final int MEDIUM = 0;
    public static final int SLOW = 2;

    private static final int PARALLAX_FAST = 60;
    private static final int PARALLAX_MEDIUM = 72;
    private static final int PARALLAX_SLOW = 90;

    private static final long REFRESH_DURATION = 16L;

    //当前动画时间
    private long mCircleTime;
    //每层颜色
    private int[] mLayerColors;
    //旋转速度
    private int mCircleSpeed;
    //视差差速
    private int mParallaxSpeed;
    //弧长
    private float mSweepAngle;
    //弧宽
    private float mStrokeWidth;

    public WhorlView(Context context) {
        this(context, null, 0);
    }

    public WhorlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhorlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //默认外层最慢270度/s
        final int defaultCircleSpeed = 270;
        final float defaultSweepAngle = 90f;
        final float defaultStrokeWidth = 5f;
        final String defaultColors = "#F44336_#4CAF50_#5677fc";
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(
                    attrs, R.styleable.whorlview_style);
            String colors = typedArray.getString(R.styleable.whorlview_style_whorlview_circle_colors);
            if (TextUtils.isEmpty(colors)) {
                colors = defaultColors;
            }
            parseStringToLayerColors(colors);
            mCircleSpeed = typedArray.getInt(R.styleable.whorlview_style_whorlview_circle_speed, defaultCircleSpeed);
            int index = typedArray.getInt(R.styleable.whorlview_style_whorlview_parallax, 0);
            setParallax(index);
            mSweepAngle = typedArray.getFloat(R.styleable.whorlview_style_whorlview_sweepAngle, defaultSweepAngle);
            if (mSweepAngle <= 0 || mSweepAngle >= 360) {
                throw new IllegalArgumentException("sweep angle out of bound");
            }
            mStrokeWidth = typedArray.getFloat(R.styleable.whorlview_style_whorlview_strokeWidth, defaultStrokeWidth);
            typedArray.recycle();
        } else {
            parseStringToLayerColors(defaultColors);
            mCircleSpeed = defaultCircleSpeed;
            mParallaxSpeed = PARALLAX_MEDIUM;
            mSweepAngle = defaultSweepAngle;
            mStrokeWidth = defaultStrokeWidth;
        }
    }

    /**
     * string类型的颜色分割并转换为色值
     *
     * @param colors
     */
    private void parseStringToLayerColors(String colors) {
        String[] colorArray = colors.split(COLOR_SPLIT);
        mLayerColors = new int[colorArray.length];
        for (int i = 0; i < colorArray.length; i++) {
            try {
                mLayerColors[i] = Color.parseColor(colorArray[i]);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("whorlview_circle_colors can not be parsed | " + ex.getLocalizedMessage());
            }
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
        for (int i = 0; i < mLayerColors.length; i++) {
            float angle = (mCircleSpeed + mParallaxSpeed * i) * mCircleTime * 0.001f;
            drawArc(canvas, i, angle);
        }
    }

    private boolean mIsCircling = false;

    /**
     * start anim
     */
    public void start() {
        mIsCircling = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                mCircleTime = 0L;
                while (mIsCircling) {
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
        mIsCircling = false;
        mCircleTime = 0L;
        invalidateWrap();
    }

    public boolean isCircling() {
        return mIsCircling;
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
        RectF oval = checkRectF(index);
        canvas.drawArc(oval, startAngle, mSweepAngle, false, paint);
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
        mArcPaint.setStrokeWidth(mStrokeWidth);
        mArcPaint.setAntiAlias(true);
        return mArcPaint;
    }

    private RectF mOval;

    private RectF checkRectF(int index) {
        if (mOval == null) {
            mOval = new RectF();
        }
        float start = index * (mStrokeWidth + mIntervalWidth) + mStrokeWidth / 2;
        float end = getMinLength() - start;
        mOval.set(start, start, end, end);
        return mOval;
    }

    private int getMinLength() {
        return Math.min(getWidth(), getHeight());
    }

    private float mIntervalWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minSize = (int) (mStrokeWidth * 4 * mLayerColors.length + mStrokeWidth);
        int wantSize = (int) (mStrokeWidth * 8 * mLayerColors.length + mStrokeWidth);
        int size = measureSize(widthMeasureSpec, wantSize, minSize);
        calculateIntervalWidth(size);
        setMeasuredDimension(size, size);
    }

    /**
     * 计算间隔大小
     *
     * @param size
     */
    private void calculateIntervalWidth(int size) {
        float wantIntervalWidth = (size / (mLayerColors.length * 2)) - mStrokeWidth;
        //防止间隔太大，最大为弧宽的3倍
        float maxIntervalWidth = mStrokeWidth * 4;
        mIntervalWidth = Math.min(wantIntervalWidth, maxIntervalWidth);
    }

    /**
     * 测量view的宽高
     *
     * @param measureSpec
     * @param wantSize
     * @param minSize
     * @return
     */
    public static int measureSize(int measureSpec, int wantSize, int minSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // 父布局想要view的大小
            result = specSize;
        } else {
            result = wantSize;
            if (specMode == MeasureSpec.AT_MOST) {
                // wrap_content
                result = Math.min(result, specSize);
            }
        }
        //测量的尺寸和最小尺寸取大
        return Math.max(result, minSize);
    }
}
