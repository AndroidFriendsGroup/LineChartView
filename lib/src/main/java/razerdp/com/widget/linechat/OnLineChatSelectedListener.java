package razerdp.com.widget.linechat;

import android.view.MotionEvent;

/**
 * Created by 大灯泡 on 2018/1/24.
 */
public interface OnLineChatSelectedListener<T extends ILineChatInfo> {

    void onSelected(MotionEvent event, String lineTag, T data);
}
