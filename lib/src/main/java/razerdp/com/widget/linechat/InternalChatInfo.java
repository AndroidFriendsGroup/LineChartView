package razerdp.com.widget.linechat;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razerdp.com.widget.util.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/11.
 */
final class InternalChatInfo {
    final String lineTag;

    List<LineChatInfoWrapper> mInfos;
    Paint linePaint;
    Paint hightLightCirclePaint;
    Path linePath;
    Path measureLinePath;
    float startX;
    float xFreq;

    public InternalChatInfo(String lineTag) {
        this(lineTag, null);
    }

    public InternalChatInfo(String lineTag, ILineChatInfo info) {
        this.lineTag = lineTag;
        if (mInfos == null) {
            mInfos = new ArrayList<>();
        }
        add(info);
        initPaint(info);
    }

    private void initPaint(ILineChatInfo info) {
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

    InternalChatInfo add(ILineChatInfo info) {
        if (info == null) return this;
        if (mInfos == null) {
            mInfos = new ArrayList<>();
        }
        mInfos.add(new LineChatInfoWrapper(info));
        initPaint(info);
        return this;
    }

    List<? extends ILineChatInfo> getRawInfoList() {
        List<ILineChatInfo> result = new ArrayList<>();
        for (LineChatInfoWrapper info : mInfos) {
            result.add(info.mInfo);
        }
        return result;
    }

    LineChatInfoWrapper getInfo(int index) {
        if (index < 0 || index >= mInfos.size()) return null;
        return mInfos.get(index);
    }

    InternalChatInfo calculatePosition(LineChatConfig chatConfig) {
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

        final float yFreq = lineBounds.height() / chatConfig.yCoordinateAccuracyLevel;
        final float contentHeight = lineBounds.height() - (textBounds.height() >> 1);

        float xFreq = xCoorWidth / size;

        this.startX = startX;
        this.xFreq = xFreq;

        for (int i = 0; i < mInfos.size(); i++) {
            LineChatInfoWrapper info = mInfos.get(i);
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

    int findIndex(LineChatConfig config, double value) {
        if (config == null || ToolUtil.isListEmpty(config.mChatHelper.mYCoordinateValue)) {
            return 1;
        }
        final int tSize = config.mChatHelper.mYCoordinateValue.size()-1;
        for (int i = 0; i < tSize; i++) {
            double firstValue = config.mChatHelper.mYCoordinateValue.get(i);
            double secondValue = config.mChatHelper.mYCoordinateValue.get(i + 1);
            if (value>=firstValue&&value<=secondValue){
                return tSize-i;
            }
           /* if (value == firstValue) {
                return tSize - i;
            } else if (value == secondValue) {
                return tSize - i ;
            } else if (value > firstValue && value < secondValue) {
                return tSize - i;
            }*/
        }
        return 1;
    }
}
