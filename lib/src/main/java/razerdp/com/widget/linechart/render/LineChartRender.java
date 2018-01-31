package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.ILineChatrInfo;
import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.line.Line;
import razerdp.com.widget.linechart.line.PointInfo;
import razerdp.com.widget.util.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class LineChartRender extends BaseRender {

    private List<Line> mLines = new ArrayList<>();

    public LineChartRender(IChart chart) {
        super(chart);
    }

    @Override
    public void onDraw(Canvas canvas) {
        LineChartConfig config = chart.getConfig();
        if (config == null) {
            Log.e(TAG, "no config found,abort prepare");
            return;
        }
        if (config.isCubic()) {
            onDrawCubic(canvas);
        } else {
            onDrawNormal(canvas);
        }

    }

    private void onDrawCubic(Canvas canvas) {
        for (Line line : mLines) {
            drawLineCubic(canvas, line);
        }
    }

    private void onDrawNormal(Canvas canvas) {
        for (Line line : mLines) {
            drawLineNormal(canvas, line);
        }
    }

    private void drawLineNormal(Canvas canvas, Line line) {
        int pointIndex = 0;
        float prePointY = 0;
        Path path = line.getLinePath();
        Paint paint = line.getLinePaint();
        for (PointInfo pointInfo : line.getPoints()) {
            float x = pointInfo.getX();
            float y = pointInfo.getY();
            if (pointIndex == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            prePointY = y;
            pointIndex++;
        }
        canvas.drawPath(path, paint);
        path.rewind();
    }

    /**
     * 以下代码截取自hellocharts,感谢他的开源-V-
     * https://github.com/lecho/hellocharts-android
     *
     * @param canvas
     * @param line
     */
    private void drawLineCubic(Canvas canvas, Line line) {
        List<PointInfo> points = line.getPoints();
        if (ToolUtil.isListEmpty(points)) return;
        final int pointSize = points.size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;
        Path path = line.getLinePath();
        Paint paint = line.getLinePaint();

        for (int i = 0; i < pointSize; i++) {
            if (Float.isNaN(currentPointX)) {
                PointInfo linePoint = points.get(i);
                currentPointX = linePoint.getX();
                currentPointY = linePoint.getY();
            }

            if (Float.isNaN(previousPointX)) {
                if (i > 0) {
                    PointInfo linePoint = points.get(i - 1);
                    previousPointX = linePoint.getX();
                    previousPointY = linePoint.getY();
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                if (i > 1) {
                    PointInfo linePoint = points.get(i - 2);
                    prePreviousPointX = linePoint.getX();
                    prePreviousPointY = linePoint.getY();
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }


            if (i < pointSize - 1) {
                PointInfo linePoint = points.get(i + 1);
                nextPointX = linePoint.getX();
                nextPointY = linePoint.getY();
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (i == 0) {
                path.moveTo(currentPointX, currentPointY);
            } else {
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (0.15f * firstDiffX);
                final float firstControlPointY = previousPointY + (0.15f * firstDiffY);
                final float secondControlPointX = currentPointX - (0.15f * secondDiffX);
                final float secondControlPointY = currentPointY - (0.15f * secondDiffY);
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;

        }
        canvas.drawPath(path, paint);
        path.rewind();

    }

    @Override
    public void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

    }

    @Override
    public void reset() {
        mLines.clear();
    }

    @Override
    public void prepare() {
        reset();
        LineChartConfig config = chart.getConfig();
        if (config == null) {
            Log.e(TAG, "no config found,abort prepare");
            return;
        }
        HashMap<String, Line> linesMap = config.getLinesMap();
        mLines.addAll(linesMap.values());
        int maxXAxesLength = 0;
        for (Line line : mLines) {
            maxXAxesLength = Math.max(line.getPoints().size(), maxXAxesLength);
        }
        double maxValue = mChartManager.getYAxisMaxValue();
        double minValue = mChartManager.getYAxisMinValue();

        float startX = mChartManager.getChartLineDrawBounds().left;
        float startY = mChartManager.getChartLineDrawBounds().bottom;
        float xMargin = mChartManager.getChartLineDrawBounds().width() / maxXAxesLength;
        float lineBoundsHeight = mChartManager.getChartLineDrawBounds().height();

        for (Line line : mLines) {
            line.preparePaint();
            int index = 0;
            for (PointInfo pointInfo : line.getPoints()) {
                ILineChatrInfo info = pointInfo.getInfo();
                if (info == null) continue;
                double value = info.getValue();

                float x = startX + xMargin * index;
                float y = (float) (startY - (value - minValue) / (maxValue - minValue) * lineBoundsHeight);
                pointInfo.set(x, y);
                index++;
            }
        }
    }

}
