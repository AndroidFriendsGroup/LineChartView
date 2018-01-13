package razerdp.com.widget.linechat;

import android.graphics.RectF;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2018/1/12.
 */
public class LineChatPrepareConfig implements Serializable {
    String mYcoordinateFormated;
    RectF drawRectf;

    public LineChatPrepareConfig() {
        drawRectf = new RectF();
    }

    public LineChatPrepareConfig setYcoordinateFormated(String ycoordinateFormated) {
        mYcoordinateFormated = ycoordinateFormated;
        return this;
    }

    LineChatPrepareConfig setDrawRect(RectF rect) {
        if (rect != null) {
            drawRectf.set(rect);
        }
        return this;
    }
}
