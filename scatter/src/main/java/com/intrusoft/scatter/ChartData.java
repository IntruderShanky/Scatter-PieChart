package com.intrusoft.scatter;

/**
 * {@link ChartData} will provide the data to chart
 */

public class ChartData implements Comparable<ChartData> {

    private String displayText;
    private int backgroundColor;
    private int textColor;
    private float partInPercent;

    /**
     *
     * @param displayText text will display on chart
     * @param partInPercent partition of chart in percent
     */
    public ChartData(String displayText, float partInPercent) {
        this.displayText = displayText;
        this.partInPercent = partInPercent;
    }

    /**
     *
     * @param displayText text will display on chart
     * @param partInPercent partition of chart in percent
     * @param textColor color of displayText
     */
    public ChartData(String displayText, float partInPercent, int textColor) {
        this.displayText = displayText;
        this.textColor = textColor;
        this.partInPercent = partInPercent;
    }

    /**
     *
     * @param displayText text will display on chart
     * @param partInPercent partition of chart in percent
     * @param textColor color of displayText
     * @param backgroundColor background color of that particular partition
     */
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
