package razerdp.com.widget.linechart;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.util.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/11.
 */
final class InternalChartInfo {
    final String lineTag;

    List<LineChatrInfoWrapper> mInfos;
    Paint linePaint;
    Paint hightLightCirclePaint;
    Path linePath;
    Path measureLinePath;
    float startX;
    float xFreq;

    public InternalChartInfo(String lineTag) {
        this(lineTag, null);
    }

    public InternalChartInfo(String lineTag, ILineChatrInfo info) {
        this.lineTag = lineTag;
        if (mInfos == null) {
            mInfos = new ArrayList<>();
        }
        add(info);
        initPaint(info);
    }

    private void initPaint(ILineChatrInfo info) {
        if (info == null) return;
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(info.getChatLineWidth());
            linePaint.setColor(info.getChatLineColor());
        }
        if (hightLightCirclePaint == null) {
            hightLightCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            hightLightCirclePaint.setStyle(Paint.Style.FILL);
            hightLightCirclePaint.setColor(info.getHightLightColor());
        }
        if (linePath == null) {
            linePath = new Path();
        }

        if (measureLinePath == null) {
            measureLinePath = new Path();
        }
    }

    InternalChartInfo add(ILineChatrInfo info) {
        if (info == null) return this;
        if (mInfos == null) {
            mInfos = new ArrayList<>();
        }
        mInfos.add(new LineChatrInfoWrapper(info));
        initPaint(info);
        return this;
    }

    List<? extends ILineChatrInfo> getRawInfoList() {
        List<ILineChatrInfo> result = new ArrayList<>();
        for (LineChatrInfoWrapper info : mInfos) {
            result.add(info.mInfo);
        }
        return result;
    }

    LineChatrInfoWrapper getInfo(int index) {
        if (index < 0 || index >= mInfos.size()) return null;
        return mInfos.get(index);
    }

    InternalChartInfo calculatePosition(LineChartConfig chatConfig) {
//        double yAccuracy = (chatConfig.mChatHelper.maxValue - chatConfig.mChatHelper.minValue) / 5;
        double yAccuracy = 0;

        double tMax = chatConfig.mChatHelper.maxValue + yAccuracy;
        double tMin = chatConfig.mChatHelper.minValue - yAccuracy;
        final double range = tMax - tMin;

        Rect textBounds = chatConfig.mChatHelper.getCoordinateTextSize(null);
        RectF lineBounds = chatConfig.mChatHelper.lineBounds;

        final float xCoorWidth = lineBounds.width() - textBounds.width() - chatConfig.elementPadding;

        float startX = lineBounds.left + textBounds.width();
        final int size = chatConfig.mChatHelper.xCoordinateLength;

        final float yFreq = lineBounds.height() / (chatConfig.yCoordinateAccuracyLevel - 1);
        final float contentHeight = lineBounds.height() - (textBounds.height() >> 1);

        float xFreq = xCoorWidth / size;

        this.startX = startX;
        this.xFreq = xFreq;

        for (int i = 0; i < mInfos.size(); i++) {
            LineChatrInfoWrapper info = mInfos.get(i);
            double yPercent = 1 - (Math.abs(info.mInfo.getValue()) / tMax);
            int y = findIndex(chatConfig, info.mInfo.getValue());
            Log.i("percent", "perycent: " + yPercent + "   value  >>  " + info.mInfo.getValue());
            info.setPosition(startX + xFreq * i, (float) (yFreq * y + lineBounds.top + (yFreq * yPercent)));
        }
        return this;
    }

    int getRawInfoListSize() {
        return mInfos.size();
    }

    int findIndex(LineChartConfig config, double value) {
        if (config == null || ToolUtil.isListEmpty(config.mChatHelper.mYCoordinateValue)) {
            return 1;
        }
        final int tSize = config.mChatHelper.mYCoordinateValue.size();
        for (int i = 0; i < tSize; i++) {
            Pair<Double, Double> element = config.mChatHelper.mYCoordinateValue.get(i);
            if (value >= element.first && value <= element.second) return tSize - i;
        }
        return 1;
    }
}
