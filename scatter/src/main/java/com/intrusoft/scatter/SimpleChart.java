package com.intrusoft.scatter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;

/**
 * Created by apple on 10/5/16.
 */

public class SimpleChart extends View {

    private final String LOGTAG = "Scatter";
    private List<ChartData> chartData;
    private float width;
    private float height;
    private List<ChartUtils> chartUtils;
    private Paint paint;
    private Paint textPaint;
    private float cx, cy;
    private int color;
    private int textColor;
    private float side;
    private int alpha = 255;
    private float animateValue = -1;
    private boolean withPercent;
    private String aboutChart;
    private int aboutTextColor;


    public SimpleChart(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
            try {
                textColor = array.getColor(R.styleable.PieChart_textColor, Color.DKGRAY);
                color = array.getColor(R.styleable.PieChart_chartColor, Color.LTGRAY);
                aboutChart = array.getString(R.styleable.PieChart_aboutChart);
                withPercent = !array.getBoolean(R.styleable.PieChart_equalPartition, false);
                aboutTextColor = array.getInt(R.styleable.PieChart_aboutTextColor, Color.DKGRAY);
            } finally {
                array.recycle();
            }
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        side = Math.min(width, height);
        cx = width / 2;
        cy = height / 2;
        if (chartData != null) {
            if (withPercent)
                chartUtils = ChartHelper.generateSimpleRadiusWithPercent(chartData, (int) side);
            else
                chartUtils = ChartHelper.generateSimpleRadius(chartData, (int) side);
            alpha = 255 / chartData.size();
            color = Color.argb(alpha, Color.red(this.color), Color.green(this.color), Color.blue(this.color));
        } else Log.e(LOGTAG, "Simple Pie chart must have chart Data");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (chartData != null && chartData.size() != 0) {
            if (animateValue >= 0) {
                for (int i = chartData.size() - 1; i >= 0; i--) {
                    textPaint.setTextSize(chartUtils.get(i).getTextSize());
                    if (chartData.get(i).getBackgroundColor() != 0)
                        paint.setColor(chartData.get(i).getBackgroundColor());
                    else
                        paint.setColor(color);
                    if (chartData.get(i).getTextColor() != 0)
                        textPaint.setColor(chartData.get(i).getTextColor());
                    else
                        textPaint.setColor(textColor);

                    // Log.d("COLOR",textPaint.getColor()+"");

                    if (animateValue <= chartUtils.get(i).getRadius()) {
                        canvas.drawCircle(cx, cy, animateValue, paint);
                        if (i == chartData.size() - 1 && animateValue == chartUtils.get(i).getRadius())
                            canvas.drawTextOnPath(chartData.get(i).getDisplayText(), getPath(chartUtils.get(i).getRadius()), 0, chartUtils.get(i).getOffset(), textPaint);
                    } else {
                        canvas.drawCircle(cx, cy, chartUtils.get(i).getRadius(), paint);
                        canvas.drawTextOnPath(chartData.get(i).getDisplayText(), getPath(chartUtils.get(i).getRadius()), 0, chartUtils.get(i).getOffset(), textPaint);
                    }
                    if (aboutChart != null) {
                        textPaint.setColor(aboutTextColor);
                        textPaint.setTextSize(chartUtils.get(0).getTextSize());
                        canvas.drawText(aboutChart, cx, cy, textPaint);
                    }
                }
            } else
                animateChart();
        }
    }

    private Path getPath(float radius) {
        RectF oval = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        Path path = new Path();
        path.addArc(oval, 90, 350);
        return path;
    }

    public void animateChart() {
        animateValue = 0;
        ValueAnimator animator = ValueAnimator.ofFloat(0, chartUtils.get(chartData.size() - 1).getRadius());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animateValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public List<ChartData> getChartData() {
        return chartData;
    }

    public int getColor() {
        return color;
    }

    public void setChartData(List<ChartData> chartData) {
        this.chartData = chartData;
        animateValue = -1;
        invalidate();
    }

    public void setChartColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    public void setTextTypeFace(Typeface typeFace) {
        textPaint.setTypeface(typeFace);
        invalidate();
    }

    public void partitionWithPercent(boolean withPercent) {
        this.withPercent = withPercent;
        if (withPercent)
            chartUtils = ChartHelper.generateSimpleRadiusWithPercent(chartData, (int) side);
        else chartUtils = ChartHelper.generateSimpleRadius(chartData, (int) side);
        animateValue = -1;
        invalidate();
    }
}
