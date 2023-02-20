package com.towett.ghost;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class CircularProgressBar extends View {
    private int progress = 0;
    private Paint paint = new Paint();
    private int maxProgress = 100;
    private int minProgress = 0;
    private int progressColor = Color.RED;
    private Random random = new Random();
    private Handler handler = new Handler();
    private Runnable updateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            // Generate a random delta between -5 and 5
            int delta = random.nextInt(11) - 5;
            setProgress(progress + delta);
            // Post another update at a random interval between 100 and 1000 ms
            handler.postDelayed(this, random.nextInt(901) + 100);
        }
    };

    public CircularProgressBar(Context context) {
        super(context);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        handler.post(updateProgressRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2;

        // Draw the progress bar
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius / 10);
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the progress indicator
        paint.setColor(progressColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(radius / 10);
        float angle = 360.0f * (progress - minProgress) / (maxProgress - minProgress);
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, -90, angle, false, paint);

        // Draw the progress text
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(radius / 2);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Integer.toString(progress), centerX, centerY + radius / 4, paint);
    }

    public void setProgress(int progress) {
        this.progress = Math.max(minProgress, Math.min(maxProgress, progress));
        invalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setMinProgress(int minProgress) {
        this.minProgress = minProgress;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }
}
