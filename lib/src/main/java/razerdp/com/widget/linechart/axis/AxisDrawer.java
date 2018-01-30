package razerdp.com.widget.linechart.axis;

import android.graphics.Canvas;
import android.graphics.Paint;

import razerdp.com.widget.linechart.render.BaseRender;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public final class AxisDrawer {
    Axis mAxis;

    public AxisDrawer(Axis axis) {
        mAxis = axis;
    }

    public Axis getAxis() {
        return mAxis;
    }

    public void setAxis(Axis axis) {
        mAxis = axis;
    }

    public void drawLine(BaseRender render, Canvas canvas, Paint paint, float startX, float startY, float endX, float endY) {
        if (mAxis == null) return;
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void drawText(BaseRender render, Canvas canvas, Paint paint, float x, float y) {
        if (mAxis == null) return;
        String label = mAxis == null ? "" : mAxis.getLabel();
        int height = render.getChartManager().measureTextBounds(label, paint).height();
        y = y - (height >> 1);
        canvas.drawText(label, x, y, paint);
    }
}
