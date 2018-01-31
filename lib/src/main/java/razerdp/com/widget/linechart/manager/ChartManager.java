package razerdp.com.widget.linechart.manager;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.render.BaseRender;
import razerdp.com.widget.linechart.utils.CLog;
import razerdp.com.widget.util.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class ChartManager {
    RectF chartDrawRect;
    RectF chartLineDrawRect;
    Rect textBounds;
    Paint textMeasurePaint;
    IChart mIChart;
    List<BaseRender> mBaseRenders;

    double[] yRangeValue = new double[2];

    public ChartManager(IChart chart) {
        this.mIChart = chart;
        mBaseRenders = new ArrayList<>();
        chartDrawRect = new RectF();
        chartLineDrawRect = new RectF();
        textBounds = new Rect();
        textMeasurePaint = new TextPaint();
        textMeasurePaint.setStyle(Paint.Style.FILL);
    }

    public void setChartContentRect(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        CLog.i("sizechange");
        chartDrawRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        for (BaseRender baseRender : mBaseRenders) {
            baseRender.onSizeChanged(width, height, paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    public float getDrawWidth() {
        return chartDrawRect.width();
    }

    public float getDrawHeight() {
        return chartDrawRect.height();
    }

    public RectF getDrawBounds() {
        return chartDrawRect;
    }

    public Rect measureTextBounds(String text, int textSize) {
        if (TextUtils.isEmpty(text)) {
            textBounds.setEmpty();
            return textBounds;
        }
        textMeasurePaint.setTextSize(textSize);
        textMeasurePaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }

    public Rect measureTextBounds(String text, Paint paint) {
        if (TextUtils.isEmpty(text) || paint == null) {
            textBounds.setEmpty();
            return textBounds;
        }
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }

    public RectF getChartLineDrawBounds() {
        return chartLineDrawRect;
    }

    public void setChartLineDrawBounds(float left, float top, float right, float bottom) {
        chartLineDrawRect.set(left, top, right, bottom);
    }

    public void setYAxisMinValue(double value) {
        yRangeValue[0] = value;
    }

    public void setYAxisMaxValue(double value) {
        yRangeValue[1] = value;
    }

    public double getYAxisMinValue() {
        return yRangeValue[0];
    }

    public double getYAxisMaxValue() {
        return yRangeValue[1];
    }

    //-----------------------------------------render option-----------------------------------------
    public void registerRender(BaseRender render) {
        if (render == null) return;
        if (!mBaseRenders.contains(render)) {
            mBaseRenders.add(render);
        }
    }

    public void unRegisterRender(BaseRender render) {
        if (ToolUtil.isListEmpty(mBaseRenders) || !mBaseRenders.contains(render)) return;
        mBaseRenders.remove(render);
    }
}
