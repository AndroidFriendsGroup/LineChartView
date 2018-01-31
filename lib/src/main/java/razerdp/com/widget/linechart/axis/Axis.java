package razerdp.com.widget.linechart.axis;

import android.graphics.Canvas;
import android.graphics.Paint;

import razerdp.com.widget.linechart.render.BaseRender;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class Axis {

    private String label = "";

    public Axis() {
    }

    public Axis(String label) {
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


    public void drawLine(BaseRender render, Canvas canvas, Paint paint, float startX, float startY, float endX, float endY) {
        int height = render.getChartManager().measureTextBounds(label, paint).height();
        float y = startY - (height >> 1);
        canvas.drawLine(startX, y, endX, y, paint);
    }

    public void drawText(BaseRender render, Canvas canvas, Paint paint, float x, float y) {
        canvas.drawText(label, x, y, paint);
    }
}
