package com.yujunjie.myapplication.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class PointView extends View {
    ValueAnimator valueAnimator;

    private PathMeasure mPathMeasure;
    //    private Paint paint;
    private Paint mPointPaint;
    private Path path;
    private float[] coords = new float[2];
    private int XDraw, YDraw;
    private int space = 20;

    public PointView(Context context) {
        super(context);
        init();
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        //设置小圆点有光晕效果
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(12, BlurMaskFilter.Blur.NORMAL);
        mPointPaint.setMaskFilter(blurMaskFilter);
        mPointPaint.setColor(Color.WHITE);

        path = new Path();
        path.moveTo(100, 100);
        path.lineTo(500, 100);
        path.lineTo(500, 500);
        path.lineTo(100, 500);
        path.lineTo(100, 100);

        mPathMeasure = new PathMeasure(path, true);

        coords = new float[2];
        coords[0] = space;
        coords[1] = space;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        XDraw = measureHandler(widthMeasureSpec);
        YDraw = measureHandler(heightMeasureSpec);
        Log.e("yujj", "x:" + XDraw + "--y:" + YDraw);
        setMeasuredDimension(XDraw, YDraw);
        path.reset();
        path.moveTo(space, space);
        path.lineTo(XDraw - space, space);
        path.lineTo(XDraw - space, YDraw - space);
        path.lineTo(space, YDraw - space);
        path.close();
        mPathMeasure.setPath(path, true);
    }

    //尺寸测绘
    private int measureHandler(int measureSpec) {
        int vale = 520;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            vale = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            vale = Math.min(vale, specSize);
        } else {
            vale = 520;
        }
        return vale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(coords[0], coords[1], 20, mPointPaint);
    }

    // 开启动画
    public void startAnim(long duration) {
        if (valueAnimator != null && (valueAnimator.isPaused() || valueAnimator.isRunning())) {
            valueAnimator.resume();
            return;
        }
        valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        Log.d("-->measure length", "measure length = " + mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        // 减速插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到coords
                mPathMeasure.getPosTan(value, coords, null);
                postInvalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    //停止动画
    public void stopAnim() {
        valueAnimator.pause();
    }
}
