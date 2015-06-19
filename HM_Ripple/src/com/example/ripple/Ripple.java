package com.example.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by soyes on 6/18/15.
 */
public class Ripple extends View {
    /**
     * 圆心的 x坐标
     */
    private float cx;

    /**
     * 圆心的 y坐标
     */
    private float cy;

    /**
     * 圆环的半径
     */
    private float radius = 0;

    /**
     * 默认画笔
     */
    private Paint paint;

    protected boolean isRunning = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 设置透明度
            int alpha = paint.getAlpha();
            if (alpha == 0) {
                isRunning = false;
            }

            alpha = Math.max(0, alpha - 10);
            paint.setAlpha(alpha);
            Log.i("leo", "" + alpha);

            //设置半径
            radius+=5;
            paint.setStrokeWidth(radius / 3);

            invalidate();
            if (isRunning) {
                sendEmptyMessageDelayed(0, 50);
            }
        }
    };

    public Ripple(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radius=0;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius / 4);
        paint.setColor(Color.GREEN);
        paint.setAlpha(255);
    }

    /**
     * 执行动画
     */
    private void startAnim(){
        isRunning = true;
        handler.sendEmptyMessageDelayed(0, 50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            cx = getWidth() / 2;
            cy = getHeight() / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cx = event.getX();
            cy = event.getY();
            initView();
            startAnim();
        }
        return true;
    }
}
