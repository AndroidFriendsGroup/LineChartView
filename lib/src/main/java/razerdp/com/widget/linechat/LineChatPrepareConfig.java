package razerdp.com.widget.linechat;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2018/1/12.
 */
public class LineChatPrepareConfig implements Serializable {
    String mYcoordinateFormated;

    public LineChatPrepareConfig setYcoordinateFormated(String ycoordinateFormated) {
        mYcoordinateFormated = ycoordinateFormated;
        return this;
    }
}
