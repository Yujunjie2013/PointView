package com.yujunjie.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yujunjie.myapplication.widget.PointView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PointView pointView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        pointView = findViewById(R.id.pointView);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                pointView.startAnim(30000);
                break;
            case R.id.button2:
                pointView.stopAnim();
                break;
            case R.id.button3:
                pointView.animate().cancel();
                float scaleX2 = pointView.getScaleX();
                float scaleY2 = pointView.getScaleY();
                setScale(scaleX2, 1.2f, scaleY2, 1.2f);
                break;
            case R.id.button4:
                pointView.animate().cancel();
                float scaleX = pointView.getScaleX();
                float scaleY = pointView.getScaleY();
                setScale(scaleX, 1, scaleY, 1);
                break;
            default:
                break;
        }
    }

    private void setScale(float startX, float endX, float startY, float endY) {
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(pointView, "scaleX", startX, endX);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(pointView, "scaleY", startY, endY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX1).with(scaleY1);
        animatorSet.setDuration(1000).start();
    }
}
