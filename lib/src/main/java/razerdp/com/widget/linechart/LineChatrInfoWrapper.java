package razerdp.com.widget.linechart;

import android.graphics.PointF;

/**
 * Created by 大灯泡 on 2018/1/12.
 */
class LineChatrInfoWrapper {
    ILineChatrInfo mInfo;
    PointF drawPoint;

    public LineChatrInfoWrapper(ILineChatrInfo info) {
        mInfo = info;
        drawPoint = new PointF(-1, -1);
    }

    public void setInfo(ILineChatrInfo info) {
        mInfo = info;
    }

    public void setPosition(float x, float y) {
        if (drawPoint.equals(-1, -1)) {
            drawPoint.set(x, y);
        }
    }

    public float getX() {
        return drawPoint.x;
    }

    public float getY() {
        return drawPoint.y;
    }

}
