
光点始终围绕边缘运动，使用属性动画将控件放大缩小时不会对其造成影响；

其中小圆点有一个光晕效果，该效果使用**BlurMaskFilter**可实现以下不同的样式
## style:发光样式;

* Blur.INNER 内发光；

* Blur.SOLID 外发光；

* Blur.NORMAL 内外发光；

* Blur.OUTER 仅发光部分可见；

        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        //设置小圆点有光晕效果
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(12, BlurMaskFilter.Blur.NORMAL);
        mPointPaint.setMaskFilter(blurMaskFilter);
        mPointPaint.setColor(Color.WHITE);

 

通过上面的代码可以看出，使用**mPointPaint.setMaskFilter(blurMaskFilter);**给paint设置一个**BlurMaskFilter**对象就可以实现光晕效果。

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

从上面的代码可以看出，**mPathMeasure.getLength()** 会将路径拉直，看成是所以线段的距离之和，另外有一个方法**mPathMeasure.getPosTan(value, coords, null);** 该方法的作用就是获取当前路径上任意一点坐标，通过改方法会将当前运动到的值赋值给coords；而我们在coords中保存的就是x/y坐标,调用**postInvalidate()** 方法去画小圆

在onDraw方法中画小圆

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(coords[0], coords[1], 20, mPointPaint);
    }
从以上代码可以看出，重点用到的是**mPathMeasure.getPosTan(value, coords, null);** 、**mPathMeasure.getLength()**、**mPointPaint.setMaskFilter(blurMaskFilter);** 三个方法
