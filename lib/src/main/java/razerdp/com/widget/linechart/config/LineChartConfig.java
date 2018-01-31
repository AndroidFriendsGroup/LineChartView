package razerdp.com.widget.linechart.config;

import android.graphics.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import razerdp.com.widget.linechart.callback.OnChartTouchListener;
import razerdp.com.widget.linechart.line.Line;
import razerdp.com.widget.linechart.model.ILineChatrInfo;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class LineChartConfig {
    private static final int DEFAULT_AXES_LABEL_MARGIN = 8;
    private static final int DEFAULT_Y_AXES_COUNT = 6;
    private static final DecimalFormat DEFAULT_Y_AXIS_FORMAT = new DecimalFormat("0.00");

    private static final int DEFAULT_TOUCH_LINE_WIDTH = 2;
    private static final int DEFAULT_TOUCH_LINE_COLOR = Color.parseColor("#959DA5");
    private static final int DEFAULT_TOUCH_POINT_RADIUS = 8;
    private static final int DEFAULT_TOUCH_POINT_COLOR = Color.BLACK;

    //-----------------------------------------配置-----------------------------------------
    private int mAxesLabelMargin = DEFAULT_AXES_LABEL_MARGIN;
    private int mYAxesCount = DEFAULT_Y_AXES_COUNT;
    private boolean isCubic = true;

    private int touchLineWidth = DEFAULT_TOUCH_LINE_WIDTH;
    private int touchLineColor = DEFAULT_TOUCH_LINE_COLOR;
    private int touchPointRadius = DEFAULT_TOUCH_POINT_RADIUS;
    private int touchPointColor = DEFAULT_TOUCH_POINT_COLOR;


    //-----------------------------------------数据-----------------------------------------
    private List<String> xAxesLabels = new ArrayList<>();
    private HashMap<String, Line> linesMap = new HashMap<>();
    private List<String> touchLineTag = new ArrayList<>();
    private HashMap<String, OnChartTouchListener> mOnChartTouchListenerHashMap = new HashMap<>();


    private double maxYAxisValue = Double.NaN;
    private double minYAxisValue = Double.NaN;
    private DecimalFormat yAxisValueFormat = DEFAULT_Y_AXIS_FORMAT;
    private String yAxisStringFormat = "";

    public LineChartConfig reset() {
        mAxesLabelMargin = DEFAULT_AXES_LABEL_MARGIN;
        xAxesLabels.clear();
        linesMap.clear();
        return this;
    }

    public LineChartConfig axesLabelMargin(int axesLabelMarginDp) {
        mAxesLabelMargin = axesLabelMarginDp;
        return this;
    }

    public LineChartConfig addXAxisLabel(String label) {
        xAxesLabels.add(label);
        return this;
    }

    public LineChartConfig addXAxisLabels(List<String> labels) {
        if (labels == null) return this;
        if (labels.size() == 0) {
            xAxesLabels.clear();
        } else {
            xAxesLabels.addAll(labels);
        }
        return this;
    }

    public LineChartConfig addDatas(String lineTag, List<? extends ILineChatrInfo> infos) {
        if (infos == null) return this;
        if (infos.size() <= 0) {
            linesMap.clear();
        } else {
            for (ILineChatrInfo info : infos) {
                addData(lineTag, info);
            }
        }
        return this;
    }

    public LineChartConfig addData(String lineTag, ILineChatrInfo info) {
        if (info == null) return this;
        double value = info.getValue();
        maxYAxisValue = Double.isNaN(maxYAxisValue) ? value : Math.max(maxYAxisValue, value);
        minYAxisValue = Double.isNaN(minYAxisValue) ? value : Math.min(minYAxisValue, value);
        Line line = linesMap.get(lineTag);
        if (line == null) {
            line = new Line(lineTag);
            linesMap.put(line.getLineTag(), line);
        }
        line.setCubic(isCubic);
        line.setLineWidth(info.getChartLineWidth());
        line.setLineColor(info.getChatrLineColor());
        line.setPointRadius(info.getHightLightRadius());
        line.addInfo(info);
        return this;
    }

    public LineChartConfig yAxesCount(int YAxesCount) {
        mYAxesCount = YAxesCount;
        return this;
    }

    public LineChartConfig axisValueFormat(DecimalFormat yAxisValueFormat) {
        this.yAxisValueFormat = yAxisValueFormat;
        return this;
    }

    public LineChartConfig axisStringFormat(String yAxisStringFormat) {
        this.yAxisStringFormat = yAxisStringFormat;
        return this;
    }

    public LineChartConfig cube(boolean cube) {
        isCubic = cube;
        return this;
    }

    public LineChartConfig touchLineWidth(int touchLineWidth) {
        this.touchLineWidth = touchLineWidth;
        return this;
    }

    public LineChartConfig touchLineColor(int touchLineColor) {
        this.touchLineColor = touchLineColor;
        return this;
    }

    public LineChartConfig touchPointRadius(int touchPointRadius) {
        this.touchPointRadius = touchPointRadius;
        return this;
    }

    public LineChartConfig touchPointColor(int touchPointColor) {
        this.touchPointColor = touchPointColor;
        return this;
    }

    public LineChartConfig enableTouchLine(String... lineTags) {
        Collections.addAll(touchLineTag, lineTags);
        return this;
    }

    public LineChartConfig chartTouchListener(String lineTag, OnChartTouchListener mOnChartTouchListener) {
        mOnChartTouchListenerHashMap.put(lineTag, mOnChartTouchListener);
        return this;
    }

    //-----------------------------------------getter-----------------------------------------
    public List<String> getxAxesLabels() {
        return xAxesLabels;
    }

    public HashMap<String, Line> getLinesMap() {
        return linesMap;
    }

    public int getAxesLabelMargin() {
        return mAxesLabelMargin;
    }

    public int getYAxesCount() {
        return mYAxesCount;
    }

    public double getMaxYAxisValue() {
        return maxYAxisValue;
    }

    public double getMinYAxisValue() {
        return minYAxisValue;
    }

    public DecimalFormat getyAxisValueFormat() {
        return yAxisValueFormat;
    }

    public String getyAxisStringFormat() {
        return yAxisStringFormat;
    }

    public boolean isCubic() {
        return isCubic;
    }

    public int getTouchLineWidth() {
        return touchLineWidth;
    }

    public int getTouchLineColor() {
        return touchLineColor;
    }

    public int getTouchPointRadius() {
        return touchPointRadius;
    }

    public int getTouchPointColor() {
        return touchPointColor;
    }

    public List<String> getTouchLineTag() {
        return touchLineTag;
    }

    public HashMap<String, OnChartTouchListener> getOnChartTouchListenerHashMap() {
        return mOnChartTouchListenerHashMap;
    }
}
