package razerdp.com.widget.linechart;

import android.view.MotionEvent;

/**
 * Created by 大灯泡 on 2018/1/24.
 */
public interface OnLineChartSelectedListener<T extends ILineChatrInfo> {

    void onSelected(MotionEvent event, String lineTag, T data);
}
