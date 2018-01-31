package razerdp.com.widget.linechart.line;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.linechart.callback.OnChartTouchListener;
import razerdp.com.widget.linechart.model.ILineChatrInfo;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class Line {
    private String lineTag;
    private static final int DEFAULT_LINE_WIDTH = 4;
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#ff7113");
    private static final int DEFAULT_POINT_RADIUS = 6;

    private int lineWidth = DEFAULT_LINE_WIDTH;
    private int lineColor = DEFAULT_LINE_COLOR;
    private int pointRadius = DEFAULT_POINT_RADIUS;

    private Paint linePaint;
    private Path linePath;

    private OnChartTouchListener mChartTouchListener;

    List<PointInfo> mPoints;

    public Line() {
        this(null);
    }

    public Line(String lineTag) {
        this.lineTag = TextUtils.isEmpty(lineTag) ? Long.toString(System.currentTimeMillis()) : lineTag;
        mPoints = new ArrayList<>();
        linePath = new Path();
    }

    public String getLineTag() {
        return lineTag;
    }

    public void setLineTag(String lineTag) {
        this.lineTag = lineTag;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }

    public void addInfo(ILineChatrInfo info) {
        mPoints.add(new PointInfo(info));
        if (info != null) {
            setLineWidth(info.getChartLineWidth());
            setLineColor(info.getChatrLineColor());
            setPointRadius(info.getHightLightRadius());
        }
    }

    public List<PointInfo> getPoints() {
        return mPoints;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public Path getLinePath() {
        return linePath;
    }

    public void preparePaint() {
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
    }

    public OnChartTouchListener getChartTouchListener() {
        return mChartTouchListener;
    }

    public void setChartTouchListener(OnChartTouchListener chartTouchListener) {
        mChartTouchListener = chartTouchListener;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Line)) {
            return false;
        } else {
            Line from = (Line) obj;
            if (obj == this) {
                return true;
            } else {
                return TextUtils.equals(from.getLineTag(), lineTag);
            }
        }
    }
}
