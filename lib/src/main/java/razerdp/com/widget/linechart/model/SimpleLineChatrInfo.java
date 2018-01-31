package razerdp.com.widget.linechart.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class SimpleLineChatrInfo implements ILineChatrInfo, Serializable {
    private int chatLineWidth = 4;
    private int chatLineColor = Color.parseColor("#ff7113");
    private String desc;
    private double value;
    private boolean isHightLight;
    private int hightLightRadius = 5;
    private int highLightColor = Color.parseColor("#49d1fe");

    public SimpleLineChatrInfo() {
    }

    public SimpleLineChatrInfo setChatLineWidth(int chatLineWidth) {
        this.chatLineWidth = chatLineWidth;
        return this;
    }

    public SimpleLineChatrInfo setChatLineColor(int chatLineColor) {
        this.chatLineColor = chatLineColor;
        return this;
    }

    public SimpleLineChatrInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public SimpleLineChatrInfo setValue(double value) {
        this.value = value;
        return this;
    }

    public SimpleLineChatrInfo setHightLight(boolean hightLight) {
        isHightLight = hightLight;
        return this;
    }

    public SimpleLineChatrInfo setHightLightRadius(int hightLightRadius) {
        this.hightLightRadius = hightLightRadius;
        return this;
    }

    public SimpleLineChatrInfo setHighLightColor(int highLightColor) {
        this.highLightColor = highLightColor;
        return this;
    }

    @Override
    public int getChartLineWidth() {
        return chatLineWidth;
    }

    @Override
    public int getChatrLineColor() {
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
