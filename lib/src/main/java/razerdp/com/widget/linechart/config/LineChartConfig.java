package razerdp.com.widget.linechart.config;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razerdp.com.widget.linechart.ILineChatrInfo;
import razerdp.com.widget.linechart.line.Line;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class LineChartConfig {
    private static final int DEFAULT_AXES_LABEL_MARGIN_DP = 8;
    private static final int DEFAULT_Y_AXES_COUNT = 6;
    private static final DecimalFormat DEFAULT_Y_AXIS_FORMAT = new DecimalFormat("0.00");

    //-----------------------------------------配置-----------------------------------------
    private int mAxesLabelMargin = DEFAULT_AXES_LABEL_MARGIN_DP;
    private int mYAxesCount = DEFAULT_Y_AXES_COUNT;
    private boolean isCube = true;

    //-----------------------------------------数据-----------------------------------------
    private List<String> xAxesLabels = new ArrayList<>();
    private HashMap<String, Line> linesMap = new HashMap<>();

    private double maxYAxisValue = Double.NaN;
    private double minYAxisValue = Double.NaN;
    private DecimalFormat yAxisValueFormat = DEFAULT_Y_AXIS_FORMAT;
    private String yAxisStringFormat = "";

    public LineChartConfig reset() {
        mAxesLabelMargin = DEFAULT_AXES_LABEL_MARGIN_DP;
        xAxesLabels.clear();
        linesMap.clear();
        return this;
    }

    public LineChartConfig setAxesLabelMargin(int axesLabelMarginDp) {
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
        line.setCubic(isCube);
        line.setLineWidth(info.getChartLineWidth());
        line.setLineColor(info.getChatrLineColor());
        line.setPointRadius(info.getHightLightRadius());
        line.addInfo(info);
        return this;
    }

    public LineChartConfig setYAxesCount(int YAxesCount) {
        mYAxesCount = YAxesCount;
        return this;
    }

    public LineChartConfig setyAxisValueFormat(DecimalFormat yAxisValueFormat) {
        this.yAxisValueFormat = yAxisValueFormat;
        return this;
    }

    public LineChartConfig setyAxisStringFormat(String yAxisStringFormat) {
        this.yAxisStringFormat = yAxisStringFormat;
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
}
