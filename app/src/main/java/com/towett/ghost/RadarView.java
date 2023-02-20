package com.towett.ghost;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class RadarView extends View {

    private Paint radarPaint;
    private Paint radiusPaint;
    private Paint dotPaint;
    private int centerX, centerY, radius;
    private int numDots = 10;
    private Random random = new Random();
    private float radiusAngle = 0f;






    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        radarPaint = new Paint();
        radarPaint.setColor(Color.GREEN);
        radarPaint.setStyle(Paint.Style.STROKE);
        radarPaint.setStrokeWidth(5f);

        radiusPaint = new Paint();
        radiusPaint.setColor(Color.RED);
        radiusPaint.setStyle(Paint.Style.STROKE);
        radiusPaint.setStrokeWidth(5f);

        dotPaint = new Paint();
        dotPaint.setColor(Color.WHITE);
        dotPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Calculate the radius of the largest circle
        radius = Math.min(getWidth(), getHeight()) / 2 - 20;

        // Calculate the center coordinates of the view
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        // Draw the four concentric circles
        canvas.drawCircle(centerX, centerY, radius, radarPaint);
        canvas.drawCircle(centerX, centerY, radius * 2 / 3, radarPaint);
        canvas.drawCircle(centerX, centerY, radius / 3, radarPaint);
        canvas.drawCircle(centerX, centerY, radius / 6, radarPaint);

        // Draw the ten diameter lines
        for (int i = 0; i < 10; i++) {
            int angle = i * 36;
            float startX = (float) (centerX + (radius * Math.cos(Math.toRadians(angle))));
            float startY = (float) (centerY + (radius * Math.sin(Math.toRadians(angle))));
            float stopX = (float) (centerX + (radius * Math.cos(Math.toRadians(angle + 180))));
            float stopY = (float) (centerY + (radius * Math.sin(Math.toRadians(angle + 180))));
            canvas.drawLine(startX, startY, stopX, stopY, radarPaint);
        }

        // Draw the rotating radius line
        float endX = (float) (centerX + (radius * Math.cos(Math.toRadians(radiusAngle))));
        float endY = (float) (centerY + (radius * Math.sin(Math.toRadians(radiusAngle))));
        canvas.drawLine(centerX, centerY, endX, endY, radiusPaint);
        radiusAngle += 1f;
        if (radiusAngle > 360) {
            radiusAngle = 0f;
        }

        // Draw the random dots
        for (int i = 0; i < numDots; i++) {
            int dotX = random.nextInt(getWidth() - 50) + 25;
            int dotY = random.nextInt(getHeight() - 50) + 25;
            canvas.drawCircle(dotX, dotY, 5, dotPaint);
        }

        // Schedule a redraw
        postInvalidateDelayed(20);
    }





}
