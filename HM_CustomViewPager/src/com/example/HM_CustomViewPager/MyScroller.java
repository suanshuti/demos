package com.example.HM_CustomViewPager;

import android.content.Context;
import android.os.SystemClock;

/**
 * 计算位移工具的类
 */
public class MyScroller {

    private int startX;
    private int startY;
    private int distanceX;
    private int distanceY;
    private long startTime;
    /**
     * 判断是否还在执行动画
     */
    private boolean isFinish;
    private long duration;
    private long currX;
    private long currY;

    public MyScroller(Context context) {
    }

    /**
     * 开始移动
     *  @param startX    开始时的 x坐标
     * @param startY    开始时的 y坐标
     * @param distanceX x方向要移动的距离
     * @param distanceY y方向要移动的距离
     * @param abs
     */
    public void startScroll(int startX, int startY, int distanceX, int distanceY, int abs) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        duration = abs;

        this.startTime = SystemClock.uptimeMillis();
        this.isFinish = false;
    }

    /**
     * 计算当前view 的运行状况
     * 并返回是否还要继续运行的判断
     * @return true: 还在运行；false: 运行结束
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }

        // 获得所用的时间
        long passTime = SystemClock.uptimeMillis() -startTime;

        // 如果时间还在允许的范围内
        if (passTime < duration) {

            //当前的位置 = 开始的位置 + 移动的距离（距离 ＝ 速度 * 时间）
            currX = startX + distanceX / duration * passTime;
            currY = startY + distanceY / duration * passTime;
        } else {
            currX = startX + distanceX;
            currY = startY + distanceY;
            isFinish = true;
        }

        return true;
    }

    public long getCurrX() {
        return currX;
    }

    public void setCurrX(long currX) {
        this.currX = currX;
    }
}
