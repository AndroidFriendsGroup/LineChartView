package razerdp.com.widget.linechat;

/**
 * Created by 大灯泡 on 2018/1/24.
 */
public interface OnLineChatSelectedListener<T extends ILineChatInfo> {

    void onSelected(String lineTag, T data);
}
