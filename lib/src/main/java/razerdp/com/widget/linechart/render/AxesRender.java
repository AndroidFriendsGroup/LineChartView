package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.axis.Axis;
import razerdp.com.widget.util.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
class AxesRender extends BaseRender {
    private static final int DEFAULT_AXIS_LINE_WIDTH = 1;
    private static final int DEFAULT_AXIS_LINE_COLOR = Color.argb(125, 207, 207, 207);
    private static final int DEFAULT_AXIS_TEXT_SIZE = 14;
    private static final int DEFAULT_AXIS_TEXT_COLOR = Color.rgb(207, 207, 207);
    private static final int DEFAULT_AXIS_LABEL_PADDING = 4;

    int axisLineWidth = DEFAULT_AXIS_LINE_WIDTH;
    int axisLineColor = DEFAULT_AXIS_LINE_COLOR;
    int axisTextSize = DEFAULT_AXIS_TEXT_SIZE;
    int axisTextColor = DEFAULT_AXIS_TEXT_COLOR;
    int axisLabelPadding = DEFAULT_AXIS_LABEL_PADDING;

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
        preparePaint();
        preMeasure();
        float xAxesMargin = (mChartManager.getDrawWidth() / mXAxes.size()) - maxXAxesLabelWidth;
        float yAxesMargin = mChartManager.getDrawHeight() / mYAxes.size();

        //x轴
        float xStartX = maxYAxesLabelWidth + axisLabelPadding;
        float xStartY = mChartManager.getDrawHeight() - axisLabelPadding;
        float lastX = xStartX;
        for (Axis xAx : mXAxes) {
            xAx.drawText(this, canvas, labelPaint, lastX, xStartY);
            lastX += xAxesMargin;
        }

        //y轴
        float yStartX = mChartManager.getDrawBounds().left + axisLabelPadding;
        float yStartY = xStartY - maxXAxesLabelHeight - axisLabelPadding;
        float lastY = yStartY;
        for (Axis yAx : mYAxes) {
            yAx.drawText(this, canvas, labelPaint, yStartX, lastY);
            yAx.drawLine(this, canvas, labelPaint, yStartX, lastY, yStartX + mChartManager.getDrawWidth(), lastY);
            lastY -= yAxesMargin;
        }

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
