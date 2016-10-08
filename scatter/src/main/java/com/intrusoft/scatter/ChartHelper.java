package com.intrusoft.scatter;

import java.util.ArrayList;
import java.util.List;

class ChartHelper {

    static List<ChartUtils> generateSimpleRadius(List<ChartData> chartData, int maxDiameter) {
        int size = chartData.size();
        List<ChartUtils> chartUtils = new ArrayList<>();
        int lastR = maxDiameter / 2;
        int firstR = lastR / 3;
        int equalR = 0;
        float textSize = 0;
        float offset = 0;
        if (size - 1 > 1) {
            equalR = (lastR - firstR) / (size - 1);
            textSize = equalR * 0.4f;
            offset = textSize + ((equalR * 20) / 100);
        }
        chartUtils.add(0, new ChartUtils(firstR, textSize, offset, chartData.get(0).getPartInPercent()));
        for (int i = 1; i < size; i++) {
            chartUtils.add(i, new ChartUtils(chartUtils.get(i - 1).getRadius() + equalR, textSize, offset, chartData.get(i).getPartInPercent()));
        }
        return chartUtils;
    }

    static List<ChartUtils> generateSimpleRadiusWithPercent(List<ChartData> chartData, int maxDiameter) {
        List<ChartUtils> chartUtils = new ArrayList<>();
        float unit = maxDiameter / 200;
        float radius = chartData.get(0).getPartInPercent() * unit;
        float textSize = (radius * 2) / 10;
        float offset = textSize + ((radius * 10) / 100);
        chartUtils.add(0, new ChartUtils(radius, textSize, offset, chartData.get(0).getPartInPercent()));
        for (int i = 1; i < chartData.size(); i++) {
            radius = chartUtils.get(i - 1).getRadius() + (chartData.get(i).getPartInPercent() * unit);
            textSize = (radius - chartUtils.get(i - 1).getRadius()) / 2;
            offset = textSize + ((radius - chartUtils.get(i - 1).getRadius()) / 5);
            chartUtils.add(i, new ChartUtils(radius, textSize, offset, chartData.get(i).getPartInPercent()));
        }
        return chartUtils;
    }

    static List<ChartUtils> generateArcWithPercent(List<ChartData> chartData) {
        List<ChartUtils> chartUtils = new ArrayList<>();
        float unit = 3.6f;
        float radius = chartData.get(0).getPartInPercent() * unit;
        chartUtils.add(0, new ChartUtils(radius, 0, 0, chartData.get(0).getPartInPercent()));
        for (int i = 1; i < chartData.size(); i++) {
            radius = chartUtils.get(i - 1).getRadius() + (chartData.get(i).getPartInPercent() * unit);
            chartUtils.add(i, new ChartUtils(radius, 0, 0, chartData.get(i).getPartInPercent()));
        }
        return chartUtils;
    }

    public static List<ChartUtils> generateArc(List<ChartData> chartData) {
        List<ChartUtils> chartUtils = new ArrayList<>();
        float unit = 360 / chartData.size();
        float radius;
        chartUtils.add(0, new ChartUtils(unit, 0, 0, chartData.get(0).getPartInPercent()));
        for (int i = 1; i < chartData.size(); i++) {
            radius = chartUtils.get(i - 1).getRadius() + unit;
            chartUtils.add(i, new ChartUtils(radius, 0, 0, chartData.get(i).getPartInPercent()));
        }
        return chartUtils;
    }
}
