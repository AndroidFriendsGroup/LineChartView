package razerdp.com.widget.linechart.line;

import android.graphics.PointF;

import razerdp.com.widget.linechart.model.ILineChatrInfo;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public final class PointInfo {
    private ILineChatrInfo mInfo;
    private PointF point;

    public PointInfo(ILineChatrInfo info) {
        mInfo = info;
        point = new PointF();
    }

    public void set(float x, float y) {
        point.set(x, y);
    }

    public float getX() {
        return point.x;
    }

    public float getY() {
        return point.y;
    }

    public boolean inTouch(float x, float touchToTouchRange) {
        return x >= point.x - touchToTouchRange && x <= point.x + touchToTouchRange;
    }

    public ILineChatrInfo getInfo() {
        return mInfo;
    }
}
