package com.example.tic_tac_toe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyCanvasView extends View {

    private Paint crossPaint;
    private Paint circlePaint1,circlePaint2;
    private int size;
    private Point center;
    private boolean isAnimating;
    private int x1, y1, x2, y2, x3, y3, x4, y4;
    private int circleX, circleY, circleRadius;
    private int directionX = 1, directionY = 1;
    private int circleDirectionX = 1, circleDirectionY = 1;
    private Random random;

    public MyCanvasView(Context context) {
        super(context);
        init();
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        crossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crossPaint.setColor(Color.WHITE);
        crossPaint.setStrokeWidth(50);

        
        /*circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.WHITE);*/

        // Créer un Bitmap de taille 100x100
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        // Créer un Canvas à partir du Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Dessiner un cercle blanc rempli au centre du Canvas
        circlePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint1.setColor(Color.TRANSPARENT);
        circlePaint1.setStyle(Paint.Style.FILL);
       // canvas.drawCircle(50, 50, 40, circlePaint);

        // Dessiner un cercle noir autour du cercle blanc pour créer le contour
        circlePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint2.setColor(Color.WHITE);
        circlePaint2.setStyle(Paint.Style.STROKE);
        circlePaint2.setStrokeWidth(30);
      //  canvas.drawCircle(50, 50, 40, circlePaint);

        size = 200;
        center = new Point(0, 0);
        isAnimating = false;

        x1 = -size / 2;
        y1 = -size / 2;
        x2 = size / 2;
        y2 = size / 2;
        x3 = size / 2;
        y3 = -size / 2;
        x4 = -size / 2;
        y4 = size / 2;

        circleX = 0;
        circleY = 0;
        circleRadius = size / 2;

        random = new Random();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(center.x, center.y);
        canvas.drawLine(x1, y1, x2, y2, crossPaint);
        canvas.drawLine(x3, y3, x4, y4, crossPaint);
        //canvas.drawCircle(circleX, circleY, circleRadius, circlePaint);
        canvas.drawCircle(circleX, circleY, circleRadius, circlePaint1);
        canvas.drawCircle(circleX, circleY, circleRadius, circlePaint2);
    }

    public void startAnimation() {
        if (!isAnimating) {
            isAnimating = true;
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int width = getWidth();
                    int height = getHeight();
                    if (x4 + directionX * 10 > width  || x2 + directionX * 10 > width  ||
                            x1 + directionX * 10 < -width / 6 || x3 + directionX * 10 < -width / 6) {
                        directionX *= -1;
                    }
                    if (y2 + directionY * 10 > height  || y3 + directionY * 10 > height ||
                            y3 + directionY * 10 < -height / 2 + size || y4 + directionY * 10 < -height / 2 + size) {
                        directionY *= -1;
                    }

                    /*if (x1 + directionX * 10 > width - size || x2 + directionX * 10 > width - size ||
                            x3 + directionX * 10 < -width / 5 || x4 + directionX * 10 < -width / 5) {
                        directionX *= -1;
                    }
                    if (y1 + directionY * 10 > height / 2 || y2 + directionY * 10 > height / 2 ||
                            y3 + directionY * 10 < -height / 2 + size || y4 + directionY * 10 < -height / 2 + size) {
                        directionY *= -1;
                    }*/

                    x1 += directionX * 10;
                    y1 += directionY * 10;
                    x2 += directionX * 10;
                    y2 += directionY * 10;
                    x3 += directionX * 10;
                    y3 += directionY * 10;
                    x4 += directionX * 10;
                    y4 += directionY * 10;
                    // Mouvement du cercle
                    if (circleX + circleDirectionX * 15 > width - circleRadius) {
                        circleDirectionX = -1;
                    }
                    if (circleX + circleDirectionX * 15 < circleRadius) {
                        circleDirectionX = 1;
                    }
                    if (circleY + circleDirectionY * 15 > height - circleRadius) {
                        circleDirectionY = -1;
                    }
                    if (circleY + circleDirectionY * 15 < circleRadius) {
                        circleDirectionY = 1;
                    }
                    circleX += circleDirectionX * 15;
                    circleY += circleDirectionY * 15;

                    invalidate();
                    if (isAnimating) {
                        handler.postDelayed(this, 50);
                    }
                }
            };
            handler.postDelayed(runnable, 50);
        }
    }
}