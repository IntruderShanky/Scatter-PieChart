package com.intrusoft.scatter;


/**
 * @author Intruder Shanky
 * @since october 2016
 */

public class ChartUtils implements Comparable<ChartUtils> {
    private float radius, textSize, offset, percent;

    public ChartUtils(float radius, float textSize, float offset, float percent) {
        this.radius = radius;
        this.textSize = textSize;
        this.offset = offset;
    }

    public float getRadius() {
        return radius;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getOffset() {
        return offset;
    }

    public float getPercent() {
        return percent;
    }

    @Override
    public int compareTo(ChartUtils another) {
        if (this.getPercent() > another.getPercent()) return -1;
        else return 1;
    }
}
