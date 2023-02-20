package com.towett.ghost;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDotsView extends View {

    private static final int MAX_DOTS = 50;
    private static final int MAX_DOT_ALPHA = 150;
    private static final int MIN_DOT_ALPHA = 50;
    private static final int MAX_DOT_DURATION = 500;
    private static final int MIN_DOT_DURATION = 100;

    private Paint mPaint;
    private Random mRandom;
    private List<Dot> mDots;
    private Handler mHandler;

    public RandomDotsView(Context context) {
        super(context);
        init();
    }

    public RandomDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RandomDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mRandom = new Random();
        mDots = new ArrayList<>();
        mHandler = new Handler();
        startAddingDots();
    }

    private void startAddingDots() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3 && mDots.size() < MAX_DOTS; i++) {
                    addDot();
                }
                startAddingDots();
            }
        }, mRandom.nextInt(MAX_DOT_DURATION - MIN_DOT_DURATION) + MIN_DOT_DURATION);
    }

    private void addDot() {
        int alpha = mRandom.nextInt(MAX_DOT_ALPHA - MIN_DOT_ALPHA) + MIN_DOT_ALPHA;
        //int x = mRandom.nextInt(Math.abs(getWidth()));
        int x =mRandom.nextInt(Math.abs(MAX_DOT_DURATION - MIN_DOT_DURATION)) + MIN_DOT_DURATION;

       // int y = mRandom.nextInt(Math.abs(getHeight()));
        int y =mRandom.nextInt(Math.abs(MAX_DOT_DURATION - MIN_DOT_DURATION)) + MIN_DOT_DURATION;
        //int duration = mRandom.nextInt(MAX_DOT_DURATION - MIN_DOT_DURATION) + MIN_DOT_DURATION;
        int duration = mRandom.nextInt(Math.abs(MAX_DOT_DURATION - MIN_DOT_DURATION)) + MIN_DOT_DURATION;

        Dot dot = new Dot(x, y, alpha, duration);
        mDots.add(dot);
        invalidate();
        startRemovingDot(dot);
    }

    private void startRemovingDot(final Dot dot) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDots.remove(dot);
                invalidate();
            }
        }, dot.getDuration());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Dot dot : mDots) {
            mPaint.setAlpha(dot.getAlpha());
            canvas.drawCircle(dot.getX(), dot.getY(), 10, mPaint);
        }
    }

    private class Dot {
        private int mX;
        private int mY;
        private int mAlpha;
        private int mDuration;

        public Dot(int x, int y, int alpha, int duration) {
            mX = x;
            mY = y;
            mAlpha = alpha;
            mDuration = duration;
        }

        public int getX() {
            return mX;
        }

        public int getY() {
            return mY;
        }

        public int getAlpha() {
            return mAlpha;
        }

        public int getDuration() {
            return mDuration;
        }
    }
}
