package com.example.HM_WaterFallListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by soyes on 6/18/15.
 */
public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int childCount = getChildCount();
        int width = getWidth() / childCount;
        int height = getHeight();

        float evX = ev.getX();
        if (evX < width) {   // first listView
            ev.setLocation(width / 2, ev.getY());
            getChildAt(0).dispatchTouchEvent(ev);
            return true;

        } else if (evX > width && evX < 2 * width) { // middle listView
            float evY = ev.getY();
            if (evY < height / 2) { // 在中间的listView 上半部
                ev.setLocation(width / 2, ev.getY());
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    try {
                        child.dispatchTouchEvent(ev);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
//
            } else if (evY > height / 2) {   // 在中间的listView 下半部
                ev.setLocation(width / 2, ev.getY());
                try {
                    getChildAt(1).dispatchTouchEvent(ev);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else if (evX > 2 * width) {
            ev.setLocation(width / 2, ev.getY());
            getChildAt(2).dispatchTouchEvent(ev);
            return true;
        }
        return true;
    }
/*

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int width = getWidth() / getChildCount();
        int height = getHeight();
        int count = getChildCount();

        float eventX = event.getX();

        if (eventX < width) {    // ������ߵ� listView
            event.setLocation(width / 2, event.getY());
            getChildAt(0).dispatchTouchEvent(event);
            return true;

        } else if (eventX > width && eventX < 2 * width) { //�����м�� listView
            float eventY = event.getY();
            if (eventY < height / 2) {
                event.setLocation(width / 2, event.getY());
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    try {
                        child.dispatchTouchEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return true;
            } else if (eventY > height / 2) {
                event.setLocation(width / 2, event.getY());
                try {
                    getChildAt(1).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else if (eventX > 2 * width) {
            event.setLocation(width / 2, event.getY());
            getChildAt(2).dispatchTouchEvent(event);
            return true;
        }

        return true;
    }
*/
}
