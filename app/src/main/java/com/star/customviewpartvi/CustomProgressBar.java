package com.star.customviewpartvi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;

    private int mCircleWidth;

    private int mSpeed;

    private Paint mPaint;
    private int mProgress;

    private boolean mIsSecond;

    private static final int CIRCLE = 360;

    private RectF mRectF;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomProgressBar, defStyleAttr, 0
        );

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = typedArray.getColor(attr, Color.GREEN);
                    break;

                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = typedArray.getColor(attr, Color.RED);
                    break;

                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                                    getResources().getDisplayMetrics()));
                    break;

                case R.styleable.CustomProgressBar_speed:
                    mSpeed = typedArray.getInt(attr, 20);
                    break;
            }
        }

        typedArray.recycle();

        mPaint = new Paint();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mProgress++;
                    if (mProgress == CIRCLE) {
                        mProgress = 0;

                        mIsSecond = !mIsSecond;
                    }

                    postInvalidate();

                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        mRectF = new RectF();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        int center = getMeasuredWidth() / 2;
        int radius = center - mCircleWidth / 2;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);

        mRectF.set(center - radius, center - radius, center + radius, center + radius);

        if (mIsSecond) {
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, radius, mPaint);

            mPaint.setColor(mSecondColor);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);

            mPaint.setColor(mFirstColor);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        }
    }

}
