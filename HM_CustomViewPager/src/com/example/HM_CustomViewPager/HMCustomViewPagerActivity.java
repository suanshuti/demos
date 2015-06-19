package com.example.HM_CustomViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

public class HMCustomViewPagerActivity extends Activity {

    private MyScrollView myScrollView;
    //图片资源ID 数组
    private int[] ids = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};
    private RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        myScrollView = (MyScrollView) findViewById(R.id.myScrollView);

        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            myScrollView.addView(imageView);
        }

        myScrollView.setPageChangedListener(new MyScrollView.MyPageChangedListener() {
            @Override
            public void moveToDest(int currId) {
                ((RadioButton)radioGroup.getChildAt(currId)).setChecked(true);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myScrollView.moveToDest(checkedId);
            }
        });

        // 给自定义viewGroup添加测试的布局
        View temp = getLayoutInflater().inflate(R.layout.temp, null);
        myScrollView.addView(temp,2);

        for (int i = 0; i < myScrollView.getChildCount(); i++) {
            // 添加radioButton
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);

            radioGroup.addView(radioButton);
            if (i == 0) {
                radioButton.setChecked(true);
            }
        }
    }
}
