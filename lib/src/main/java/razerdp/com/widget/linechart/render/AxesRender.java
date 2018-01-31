package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.axis.Axis;
import razerdp.com.widget.linechart.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
class AxesRender extends BaseRender {
    private static final int DEFAULT_AXIS_LINE_WIDTH = 1;
    private static final int DEFAULT_AXIS_LINE_COLOR = Color.argb(150, 207, 207, 207);
    private static final int DEFAULT_AXIS_TEXT_SIZE = 14;
    private static final int DEFAULT_AXIS_TEXT_COLOR = Color.rgb(207, 207, 207);

    int axisLineWidth = DEFAULT_AXIS_LINE_WIDTH;
    int axisLineColor = DEFAULT_AXIS_LINE_COLOR;
    int axisTextSize = DEFAULT_AXIS_TEXT_SIZE;
    int axisTextColor = DEFAULT_AXIS_TEXT_COLOR;

    private List<Axis> mXAxes = new ArrayList<>();
    private List<Axis> mYAxes = new ArrayList<>();

    private Paint linePaint;
    private Paint labelPaint;

    private boolean hasPreMeasure = false;

    private int maxXAxesLabelWidth;
    private int maxXAxesLabelHeight;
    private int maxYAxesLabelWidth;
    private int maxYAxesLabelHeight;


    public AxesRender(IChart chart) {
        super(chart);
    }

    private void preparePaint() {
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        }
        if (labelPaint == null) {
            labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        }

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(axisLineWidth);
        linePaint.setColor(axisLineColor);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setTextSize(ToolUtil.sp2px(chart.getViewContext(), axisTextSize));
        labelPaint.setColor(axisTextColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (ToolUtil.isListEmpty(mXAxes) && ToolUtil.isListEmpty(mYAxes)) return;
        float xAxesMargin = (mChartManager.getDrawWidth() - maxXAxesLabelWidth) / mXAxes.size();

        int axisLabelMargin = chart.getConfig().getAxesLabelMargin();
        //x轴
        float xStartX = maxYAxesLabelWidth + (axisLabelMargin << 1);
        float xStartY = mChartManager.getDrawHeight() - axisLabelMargin;
        float lastX = xStartX;
        for (Axis xAx : mXAxes) {
            xAx.drawText(this, canvas, labelPaint, lastX, xStartY);
            lastX += xAxesMargin;
        }
        //y轴
        float yStartX = mChartManager.getDrawBounds().left + axisLabelMargin;
        float yStartY = xStartY - maxXAxesLabelHeight - axisLabelMargin;
        float yAxesMargin = (yStartY - mChartManager.getDrawBounds().top - maxYAxesLabelHeight - axisLabelMargin) / (mYAxes.size() - 1);
        float lastY = yStartY;
        for (Axis yAx : mYAxes) {
            yAx.drawText(this, canvas, labelPaint, yStartX, lastY);
            yAx.drawLine(this, canvas, linePaint, mChartManager.getChartLineDrawBounds().left, lastY, mChartManager.getChartLineDrawBounds().right, lastY);
            lastY -= yAxesMargin;
        }
    }

    @Override
    public void prepare() {
        preparePaint();
        preMeasure();
    }

    private void preMeasure() {
        if (hasPreMeasure) return;
        for (Axis xAx : mXAxes) {
            String label = xAx.getLabel();
            maxXAxesLabelWidth = Math.max(maxXAxesLabelWidth, mChartManager.measureTextBounds(label, labelPaint).width());
            maxXAxesLabelHeight = Math.max(maxXAxesLabelHeight, mChartManager.measureTextBounds(label, labelPaint).height());
        }

        for (Axis yAx : mYAxes) {
            String label = yAx.getLabel();
            maxYAxesLabelWidth = Math.max(maxYAxesLabelWidth, mChartManager.measureTextBounds(label, labelPaint).width());
            maxYAxesLabelHeight = Math.max(maxYAxesLabelHeight, mChartManager.measureTextBounds(label, labelPaint).height());
        }

        //计算折线图的内容区域
        int axisLabelMargin = chart.getConfig().getAxesLabelMargin();
        //y轴label左右都有labelMargin

        float left = mChartManager.getDrawBounds().left + maxYAxesLabelWidth + (axisLabelMargin << 1);
        float bottom = mChartManager.getDrawHeight() - maxXAxesLabelHeight - (axisLabelMargin << 1);
        float yAxesMargin = (bottom - mChartManager.getDrawBounds().top - maxYAxesLabelHeight - axisLabelMargin) / (mYAxes.size() - 1);
        float top = bottom - yAxesMargin * mYAxes.size();
        float right = left + mChartManager.getDrawWidth();

        mChartManager.setChartLineDrawBounds(left, top, right, bottom);

        hasPreMeasure = true;

    }

    @Override
    public void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

    }

    public void addXAxesData(Axis axis) {
        mXAxes.add(axis);
    }

    public void addYAxesData(Axis axis) {
        mYAxes.add(axis);
    }

    @Override
    public void reset() {
        mXAxes.clear();
        mYAxes.clear();
        hasPreMeasure = false;

        maxXAxesLabelWidth = 0;
        maxXAxesLabelHeight = 0;
        maxYAxesLabelWidth = 0;
        maxYAxesLabelHeight = 0;
    }


}
