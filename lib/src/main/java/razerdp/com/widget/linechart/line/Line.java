package razerdp.com.widget.linechart.line;

import android.graphics.Color;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.linechart.ILineChatrInfo;

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

    private boolean cubic = true;

    List<PointInfo> mPoints;

    public Line() {
        this(null);
    }

    public Line(String lineTag) {
        this.lineTag = TextUtils.isEmpty(lineTag) ? Long.toString(System.currentTimeMillis()) : lineTag;
        mPoints = new ArrayList<>();
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

    public boolean isCubic() {
        return cubic;
    }

    public void setCubic(boolean cubic) {
        this.cubic = cubic;
    }

    public void addInfo(ILineChatrInfo info) {
        mPoints.add(new PointInfo(info));
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
