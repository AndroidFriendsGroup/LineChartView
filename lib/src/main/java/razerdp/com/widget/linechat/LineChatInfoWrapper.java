package razerdp.com.widget.linechat;

/**
 * Created by 大灯泡 on 2018/1/12.
 */
class LineChatInfoWrapper {
    ILineChatInfo mInfo;
    double yPercent;

    public LineChatInfoWrapper(ILineChatInfo info) {
        mInfo = info;
    }

    public void setInfo(ILineChatInfo info) {
        mInfo = info;
    }

    public void setyPercent(double yPercent) {
        this.yPercent = yPercent;
    }
}
