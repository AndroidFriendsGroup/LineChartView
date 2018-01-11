package razerdp.com.widget.linechat;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class SimpleLineChatInfo implements ILineChatInfo, Serializable {
    private int chatLineWidth = 4;
    private int chatLineColor = Color.parseColor("#ff7113");
    private String desc;
    private double value;
    private boolean isHightLight;
    private int hightLightRadius = 5;
    private int highLightColor = Color.parseColor("#49d1fe");

    public SimpleLineChatInfo() {
    }

    public SimpleLineChatInfo setChatLineWidth(int chatLineWidth) {
        this.chatLineWidth = chatLineWidth;
        return this;
    }

    public SimpleLineChatInfo setChatLineColor(int chatLineColor) {
        this.chatLineColor = chatLineColor;
        return this;
    }

    public SimpleLineChatInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public SimpleLineChatInfo setValue(double value) {
        this.value = value;
        return this;
    }

    public SimpleLineChatInfo setHightLight(boolean hightLight) {
        isHightLight = hightLight;
        return this;
    }

    public SimpleLineChatInfo setHightLightRadius(int hightLightRadius) {
        this.hightLightRadius = hightLightRadius;
        return this;
    }

    public SimpleLineChatInfo setHighLightColor(int highLightColor) {
        this.highLightColor = highLightColor;
        return this;
    }

    @Override
    public int getChatLineWidth() {
        return chatLineWidth;
    }

    @Override
    public int getChatLineColor() {
        return chatLineColor;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public boolean isHightLight() {
        return isHightLight;
    }

    @Override
    public int getHightLightRadius() {
        return hightLightRadius;
    }

    @Override
    public int getHightLightColor() {
        return highLightColor;
    }
}
