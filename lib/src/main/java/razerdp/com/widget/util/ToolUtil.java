package razerdp.com.widget.util;

import java.util.List;

/**
 * Created by 大灯泡 on 2016/10/27.
 */

public class ToolUtil {

    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }
}
