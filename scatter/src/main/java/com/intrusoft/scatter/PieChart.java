package com.intrusoft.scatter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;


/**
 * {@link PieChart} is a circular statistical graphic, which is divided into slices to illustrate numerical proportion
 */
public class PieChart extends View {

    private List<ChartData> chartData;
    private List<ChartUtils> chartUtils;
    private Paint paint;
    private Paint textPaint;
    private float cx, cy;
    private int color;
    private int textColor;
    private int centerColor;
    private float textSize;
    private float side;
    private int alpha = 255;
    private int aboutTextColor;
    private float animateValue = -1;
    private boolean withPercent;
    private String aboutChart;
    private float aboutTextSize;

    public PieChart(Context context) {
        super(context);
        init(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
            try {
                textColor = array.getColor(R.styleable.PieChart_textColor, Color.DKGRAY);
                color = array.getColor(R.styleable.PieChart_chartColor, Color.LTGRAY);
                centerColor = array.getColor(R.styleable.PieChart_centerColor, Color.WHITE);
                aboutChart = array.getString(R.styleable.PieChart_aboutChart);
                textSize = array.getFloat(R.styleable.PieChart_textSize, 25f);
                aboutTextSize = array.getFloat(R.styleable.PieChart_aboutTextSize, 20f);
                withPercent = !array.getBoolean(R.styleable.PieChart_equalPartition, false);
                aboutTextColor = array.getInt(R.styleable.PieChart_aboutTextColor, Color.DKGRAY);
            } catch (Exception e) {
                e.printStackTrace();
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
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        side = Math.min(width, height);
        cx = width / 2;
        cy = height / 2;
        String LOGCAT = "Scatter";
        paint.setColor(centerColor);
        if (chartData != null) {
            if (withPercent) {
                float value = 0;
                int i;
                for (i = 0; i < chartData.size(); i++) {
                    value += chartData.get(i).getPartInPercent();
                    if (value > 100)
                        break;
                }
                if (i < chartData.size())
                    Log.e(LOGCAT, "Invalid Chart Data. Sum of data percent must be less than or equal to 100");
                else {
                    chartUtils = ChartHelper.generateArcWithPercent(chartData);
                }
            } else {
                chartUtils = ChartHelper.generateArc(chartData);
            }

            alpha = 255 / chartData.size();
            textPaint.setTextSize(textSize);
        } else Log.e(LOGCAT, "Pie chart must have chart Data");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (chartUtils != null && chartUtils.size() != 0) {
            int color;
            int textColor;
            float r = side / 2;
            RectF rectF = new RectF(cx - r, cy - r, cx + r, cy + r);
            RectF rectC = new RectF(cx - r * 0.4f, cy - r * 0.4f, cx + r * 0.4f, cy + r * 0.4f);
            RectF rectC1 = new RectF(cx - r * 0.5f, cy - r * 0.5f, cx + r * 0.5f, cy + r * 0.5f);
            if (animateValue >= 0) {
                for (int i = chartData.size() - 1; i >= 0; i--) {
                    color = Color.argb(alpha, Color.red(this.color), Color.green(this.color), Color.blue(this.color));
                    textColor = this.textColor;
                    if (chartData.get(i).getBackgroundColor() != 0)
                        paint.setColor(chartData.get(i).getBackgroundColor());
                    else
                        paint.setColor(color);
                    if (chartData.get(i).getTextColor() != 0)
                        textPaint.setColor(chartData.get(i).getTextColor());
                    else
                        textPaint.setColor(textColor);


                    if (animateValue <= chartUtils.get(i).getRadius()) {
                        canvas.drawArc(rectF, 0, animateValue, true, paint);
                        if (i == chartUtils.size() - 1 && animateValue == chartUtils.get(i).getRadius()) {
                            double per;
                            if (withPercent)
                                per = chartUtils.get(i).getRadius() - (chartData.get(i).getPartInPercent() * 1.8f);
                            else
                                per = chartUtils.get(i).getRadius() - (100 / chartData.size() * 1.8f);
                            double rad = per * Math.PI / 180d;
                            int x = (int) (cx + (r * 0.7 * Math.cos(rad)));
                            int y = (int) (cy + (r * 0.7 * Math.sin(rad)));
                            canvas.drawText(chartData.get(i).getDisplayText(), x, y, textPaint);
                        }
                    } else {
                        canvas.drawArc(rectF, 0, chartUtils.get(i).getRadius(), true, paint);
                        double per;
                        if (withPercent)
                            per = chartUtils.get(i).getRadius() - (chartData.get(i).getPartInPercent() * 1.8f);
                        else
                            per = chartUtils.get(i).getRadius() - (100 / chartData.size() * 1.8f);
                        double rad = per * Math.PI / 180d;
                        int x = (int) (cx + (r * 0.7 * Math.cos(rad)));
                        int y = (int) (cy + (r * 0.7 * Math.sin(rad)));
                        canvas.drawText(chartData.get(i).getDisplayText(), x, y, textPaint);
                    }
                    paint.setColor(Color.argb(88, Color.red(centerColor), Color.green(centerColor), Color.blue(centerColor)));
                    canvas.drawArc(rectC1, 0, animateValue, true, paint);
                    paint.setColor(Color.rgb(Color.red(centerColor), Color.green(centerColor), Color.blue(centerColor)));
                    canvas.drawArc(rectC, 0, animateValue, true, paint);
                    if (aboutChart != null) {
                        textPaint.setTextSize(aboutTextSize);
                        textPaint.setColor(aboutTextColor);
                        canvas.drawText(aboutChart, cx, cy, textPaint);
                    }
                }
            } else
                animateChart();
        }
    }

    /**
     * To animate the chart
     */
    public void animateChart() {
        animateValue = 0;
        if (chartUtils != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, chartUtils.get(chartUtils.size() - 1).getRadius());
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
    }

    /**
     * To set the data on chart, if chart data is null than chart will not be displayed
     * @param chartData Array list of {@link ChartData}
     */
    public void setChartData(@NonNull List<ChartData> chartData) {
        this.chartData = chartData;
        animateValue = -1;
        invalidate();
    }

    /**
     *
     * @param color if background color is not provided in {@link ChartData} than the chart will be drawn with the shade of given color
     */
    public void setChartColor(int color) {
        this.color = color;
        invalidate();
    }

    /**
     *
     * @param color if text color is not provided in {@link ChartData} than the text will be drawn with the given color
     */
    public void setTextColor(int color) {
        this.textColor = color;
        invalidate();
    }

    /**
     *
     * @param typeFace to set the typeface on the displayText
     */
    public void setTextTypeFace(Typeface typeFace) {
        textPaint.setTypeface(typeFace);
        invalidate();
    }

    /**
     * This property will define the partitions width (equal or acc. to percent)
     * @param withPercent
     */
    public void partitionWithPercent(boolean withPercent) {
        this.withPercent = withPercent;
        if (withPercent) chartUtils = ChartHelper.generateArcWithPercent(chartData);
        else chartUtils = ChartHelper.generateArc(chartData);
        animateValue = -1;
        invalidate();
    }

    /**
     *
     * @param color is the color of center circle
     */
    public void setCenterCircleColor(int color) {
        this.centerColor = color;
    }

    /**
     *
     * @param aboutChart about the chart will display on centre of the chart
     *                   (This should be in single word for better representation)
     */
    public void setAboutChart(String aboutChart) {
        this.aboutChart = aboutChart;
    }

    /**
     *
     * @param aboutTextSize textsize for aboutChart
     */
    public void setAboutTextSize(float aboutTextSize) {
        this.aboutTextSize = aboutTextSize;
    }

    /**
     *
     * @param aboutTextColor textcolor for aboutChart
     */
    public void setAboutTextColor(int aboutTextColor) {
        this.aboutTextColor = aboutTextColor;
    }
}
