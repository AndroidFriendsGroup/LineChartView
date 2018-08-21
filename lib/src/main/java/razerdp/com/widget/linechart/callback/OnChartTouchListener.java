package razerdp.com.widget.linechart.callback;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.com.widget.linechart.model.ILineChartInfo;

/**
 * Created by 大灯泡 on 2018/1/31.
 */
public interface OnChartTouchListener<T extends ILineChartInfo> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({INVALIDED, ACTION_DOWN, ACTION_MOVE, ACTION_UP})
    public @interface TouchAction {
    }

    int INVALIDED = -1;
    int ACTION_DOWN = 0x10;
    int ACTION_MOVE = 0x11;
    int ACTION_UP = 0x12;


    void onChartSelected(@Nullable String lineTag, @TouchAction int touchAction, @Nullable T data);

}
