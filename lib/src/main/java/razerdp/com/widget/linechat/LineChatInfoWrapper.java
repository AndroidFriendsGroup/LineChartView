package razerdp.com.widget.linechat;

import android.graphics.PointF;

/**
 * Created by 大灯泡 on 2018/1/12.
 */
class LineChatInfoWrapper {
    ILineChatInfo mInfo;
    PointF drawPoint;

    public LineChatInfoWrapper(ILineChatInfo info) {
        mInfo = info;
        drawPoint = new PointF(-1, -1);
    }

    public void setInfo(ILineChatInfo info) {
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
