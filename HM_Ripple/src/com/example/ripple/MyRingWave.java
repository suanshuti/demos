package com.example.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by soyes on 6/19/15.
 */
public class MyRingWave extends View {

    private static final float DIS_SOLP = 13;
    private final ArrayList<Wave> waves;
    private boolean isRunning = false;
    private int[] colors = new int[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 刷新数据
            flushData();
            // 刷新页面
            invalidate();
            // 循环动画：每50 毫秒刷新一次，直到动画结束
            if (isRunning) {
                sendEmptyMessageDelayed(0, 50);
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < waves.size(); i++) {
            Wave wave = waves.get(i);
            canvas.drawCircle(wave.cx,wave.cy,wave.r,wave.p);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                float evX = ev.getX();
                float evY = ev.getY();

                addPoint(evX, evY);

                break;
        }
        return true;
    }

    /**
     * 添加新的波浪中心点
     * @param evX
     * @param evY
     */
    private void addPoint(float evX, float evY) {
        if (waves.size() == 0) {
            addPoint2List(evX, evY);
            /**
             * 第一次启动动画
             */
            isRunning = true;
            handler.sendEmptyMessage(0);
        }else {
            Wave wave = waves.get(waves.size() - 1);
            if (Math.abs(wave.cx - evX) > DIS_SOLP || Math.abs(wave.cy - evY) > DIS_SOLP) {
                addPoint2List(evX, evY);
            }
        }
    }

    /**
     * 添加新的波浪
     * @param evX
     * @param evY
     */
    private void addPoint2List(float evX, float evY) {
        Wave wave = new Wave();
        Paint paint = new Paint();
        paint.setColor(colors[(int)(Math.random()*4)]);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        wave.cx = evX;
        wave.cy = evY;
        wave.p = paint;

        waves.add(wave);
    }

    private void flushData() {
        for (int i = 0; i < waves.size(); i++) {
            Wave wave = waves.get(i);

            // 如果透明度为0，从集合中删除
            int alpha = wave.p.getAlpha();
            if (alpha == 0) {
                waves.remove(i);    // 删除i 以后，i 的值应该再减1，否则会漏掉一个对象，不过，此处影响不大，效果上看不出来
                continue;
            }

            alpha-=5;
            if (alpha < 5) {
                alpha=0;
            }

            // 降低透明度
            wave.p.setAlpha(alpha);
            // 扩大半径
            wave.r = wave.r+3;
            // 设置半径厚度
            wave.p.setStrokeWidth(wave.r / 3);
        }

        // 如果集合被清空，就停止刷新动画
        if (waves.size() == 0) {
            isRunning=false;
        }
    }

    public MyRingWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        waves = new ArrayList<Wave>();
    }

    private class Wave {
        public Paint p;
        public float r;
        public float cy;
        public float cx;
    }
}
