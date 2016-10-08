package com.intrusoft.scatter;

/**
 * Created by apple on 10/7/16.
 */

public class ChartData implements Comparable<ChartData> {

    private String displayText;
    private int backgroundColor;
    private int textColor;
    private float partInPercent;

    public ChartData(String displayText, float partInPercent) {
        this.displayText = displayText;
        this.partInPercent = partInPercent;
    }

    public ChartData(String displayText, float partInPercent, int textColor) {
        this.displayText = displayText;
        this.textColor = textColor;
        this.partInPercent = partInPercent;
    }

    public ChartData(String displayText, float partInPercent, int textColor, int backgroundColor) {
        this.displayText = displayText;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.partInPercent = partInPercent;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getPartInPercent() {
        return partInPercent;
    }

    @Override
    public int compareTo(ChartData another) {
        if (this.getPartInPercent() > another.getPartInPercent()) return -1;
        else return 1;

    }
}
