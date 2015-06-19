package com.example.HM_CustomViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by soyes on 6/12/15.
 */
public class MyScrollView extends ViewGroup {

    private static final String TAG = "***MyScrollView***";
    private Context context;
    //    private MyScroller myScroller;
    private Scroller myScroller;
    private GestureDetector gestureDetector;
    private MyPageChangedListener pageChangedListener;

    /**
     * 当前的ID 值
     * 显示在屏幕上的子view 的下标
     */
    private int currId = 0;

    /**
     * 判断是否发生快速滑动
     */
    protected boolean isFling;
    private int downY;
    private int downX;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView();
    }

    private void initView() {
//        myScroller = new MyScroller(context);
        myScroller = new Scroller(context);
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                /**
                 * 移动当前view 的内容
                 * disX x方向移动距离
                 * disY y方向移动距离
                 * 注意：scroll 的坐标系与普通的坐标系不同：
                 *      distanceX 为正时，图片向左移动，为负时，图片向右移动
                 *
                 * 此方法调用了view.scrollTo方法,将当前视图的基准点移动到某个点
                 * scrollTo 方法中又调用了view.onScrollChanged
                 */
                scrollBy((int) distanceX, 0);

                /**
                 * 将当前视图的基准点移动到某个点
                 * x 水平方向x 坐标
                 * y 垂直方向y 坐标
                 * scrollTo(x, y)
                 */
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                isFling = true;
                if (velocityX > 0 && currId > 0) { // 快速向右滑动
                    currId--;
                } else if (velocityX < 0 && currId < getChildCount() - 1) {   // 快速向左滑动
                    currId++;
                }

                moveToDest(currId);

                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);

//            v.getMeasuredWidth();   // 得到测量的大小
        }
    }

    @Override
    /**
     * 对子view 进行布局，确定view的位置
     * changed 若为true，则说明布局发生了变化
     * l\t\r\b 是当前ViewGroup 对象在其父view 中的位置
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            Log.i(TAG, "index:" + i);
            //指定子view 的位置//指定子view 在当前ViewGroup 中的位置
            view.layout(0 + getWidth() * i, 0, getWidth() + getWidth() * i, getHeight());
        }
    }

    /**
     * 是否中断事件的传递
     * 返回true 的时候，中断事件，执行自己的 onTouchEvent方法
     * 返回false 的时候，默认处理，不中断，也不会执行自己的 onTouchEvent方法onTouchEvent方法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gestureDetector.onTouchEvent(ev);

                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指在屏幕上水平移的绝对值
                int disX = (int) Math.abs(ev.getX() - downX);
                // 手指在屏幕上垂直移的绝对值
                int disY = (int) Math.abs(ev.getY() - downY);

                if (disX > disY && disX > 10) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        Log.i(TAG, "action:" + ev.getAction() + " " + "result:" + result + " downX:" + downX + " ev.getX():" + ev.getX());
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        gestureDetector.onTouchEvent(event);

        // 添加自己的事件解析
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (!isFling) { // 在没有发生快速滑动的时候，才执行按位置判断currId
                    int nextId = 0;
                    if (event.getX() - downX > getWidth() / 2) {   //手指向右滑动，超过屏幕的1/2，当前的currId - 1;
                        nextId = currId - 1;
                    } else if (downX - event.getX() > getWidth() / 2) {   //手指向右滑动，超过屏幕的1/2，当前的currId - 1;
                        nextId = currId + 1;
                    } else {
                        nextId = currId;
                    }
                    moveToDest(nextId);
                }
                isFling = false;
                break;
        }

        Log.i(TAG, "onTouchEvent(); ation:" + event.getAction());
        return true;
    }


    /**
     * 移动到指定的屏幕上
     *
     * @param nextId 屏幕的下标
     */
    public void moveToDest(int nextId) {
        /*
         * 对nextId 进行判断，确保是在合理的范围，即：nextId >= 0 && nextId <= getChildCount()-1
         */
        currId = (nextId >= 0) ? nextId : 0;
        currId = (nextId <= getChildCount() - 1) ? nextId : (getChildCount() - 1);

        // 瞬间移动
//        scrollTo(currId * getWidth(), 0);

        // 触发 listener事件
        if (pageChangedListener != null) {
            pageChangedListener.moveToDest(currId);
        }
        int distance = currId * getWidth() - getScrollX();    // 最终的位置 － 现在的位置 ＝ 要移动的距离

//        myScroller.startScroll(getScrollX(), 0, distance, 0);

        // 设置运行时间
        myScroller.startScroll(getScrollX(), 0, distance, 0, Math.abs(distance));

        /**
         * 刷新当前view，onDraw()方法执行
         */
        invalidate();
    }

    /**
     * invalidate();会导致此方法的执行
     */
    @Override
    public void computeScroll() {
        if (myScroller.computeScrollOffset()) {
            int newX = (int) myScroller.getCurrX();
            scrollTo(newX, 0);

            // 刷新后会再次执行此computeScroll() 方法
            invalidate();
        }
    }

    public MyPageChangedListener getPageChangedListener() {
        return pageChangedListener;
    }

    public void setPageChangedListener(MyPageChangedListener pageChangedListener) {
        this.pageChangedListener = pageChangedListener;
    }

    public interface MyPageChangedListener {
        void moveToDest(int currId);
    }
}
