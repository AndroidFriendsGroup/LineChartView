package razerdp.com.widget.linechat;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import razerdp.com.widget.util.ToolUtil;


/**
 * Created by 大灯泡 on 2018/1/11.
 * <p>
 * config
 */
public class LineChatConfig {
    private static final String TAG = "LineChatConfig";

    private static final String MEASURE_TEXT = "+99.99%";

    private static final DecimalFormat sFormateRate = new DecimalFormat("#.##");
    //坐标系
    private static final int DEFAULT_COORDINATE_LINE_WIDTH = 2;
    private static final int DEFAULT_COORDINATE_LINE_COLOR = Color.rgb(207, 207, 207);
    private static final int DEFAULT_COORDINATE_TEXT_SIZE = 28;
    private static final int DEFAULT_COORDINATE_TEXT_COLOR = DEFAULT_COORDINATE_LINE_COLOR;

    private static final int DEFAULT_Y_COORDINATE_ACCURACY_LEVEL = 6;//Y坐标精度：6个
    private static final float DEFAULT_Y_COORDINATE_ACCURACY_FLOAT = 0.1f;//y坐标10%浮动
    private static final float DEFAULT_ELEMENT_PADDING = 10;//元素之间的默认padding

    private static final long DEFAULT_ANIMATION_DURATION = 800;
    private static final boolean DEFAULT_START_WITH_ANIMATION = true;

    //-----------------------------------------static value end-----------------------------------------

    int coordinateLineWidth = DEFAULT_COORDINATE_LINE_WIDTH;
    int coordinateLineColor = DEFAULT_COORDINATE_LINE_COLOR;
    int coordinateTextSize = DEFAULT_COORDINATE_TEXT_SIZE;
    int coordinateTextColor = DEFAULT_COORDINATE_TEXT_COLOR;

    int yCoordinateAccuracyLevel = DEFAULT_Y_COORDINATE_ACCURACY_LEVEL;
    float yCoordinateAccuracyFloat = DEFAULT_Y_COORDINATE_ACCURACY_FLOAT;

    float elementPadding = DEFAULT_ELEMENT_PADDING;

    String startXcoordinateDesc;
    String endXcoordinateDesc;

    long duration = DEFAULT_ANIMATION_DURATION;
    boolean animation = DEFAULT_START_WITH_ANIMATION;


    //-----------------------------------------value config end-----------------------------------------


    LineChatHelper mChatHelper;
    LinkedHashMap<String, InternalChatInfo> mChatMap;
    volatile boolean reapply;
    boolean needPrepare = true;//如果重新设置过config，则需要重新prepare

    Paint coordinateTextPaint;
    Paint coordinateLinePaint;

    public LineChatConfig() {
        mChatHelper = new LineChatHelper();
        mChatMap = new LinkedHashMap<>();
        initPaint();

    }

    private void initPaint() {
        if (coordinateTextPaint == null) {
            coordinateTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        }
        if (coordinateLinePaint == null) {
            coordinateLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        }
        coordinateTextPaint.setStyle(Paint.Style.FILL);
        coordinateTextPaint.setTextSize(coordinateTextSize);
        coordinateTextPaint.setColor(coordinateTextColor);

        coordinateLinePaint.setStyle(Paint.Style.STROKE);
        coordinateLinePaint.setColor(coordinateLineColor);
        coordinateLinePaint.setStrokeWidth(coordinateLineWidth);
    }

    public LineChatConfig setCoordinateLineWidth(int coordinateLineWidth) {
        this.coordinateLineWidth = coordinateLineWidth;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig setCoordinateLineColor(int coordinateLineColor) {
        this.coordinateLineColor = coordinateLineColor;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig setCoordinateTextSize(int coordinateTextSize) {
        this.coordinateTextSize = coordinateTextSize;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig setCoordinateTextColor(int coordinateTextColor) {
        this.coordinateTextColor = coordinateTextColor;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig addDatas(String lineTag, List<? extends ILineChatInfo> infos) {
        if (ToolUtil.isListEmpty(infos)) return this;
        for (ILineChatInfo info : infos) {
            addData(lineTag, info);
        }
        return setReapply(true);
    }

    public LineChatConfig addData(String lineTag, ILineChatInfo info) {
        return mChatHelper.addData(lineTag, info);
    }

    public LineChatConfig setyCoordinateAccuracyLevel(int yCoordinateAccuracyLevel) {
        this.yCoordinateAccuracyLevel = yCoordinateAccuracyLevel;
        return setReapply(true);
    }

    public LineChatConfig setyCoordinateAccuracyFloat(float yCoordinateAccuracyFloat) {
        this.yCoordinateAccuracyFloat = yCoordinateAccuracyFloat;
        return setReapply(true);
    }

    public LineChatConfig setStartXcoordinateDesc(String startXcoordinateDesc) {
        this.startXcoordinateDesc = startXcoordinateDesc;
        return setReapply(true);
    }

    public LineChatConfig setEndXcoordinateDesc(String endXcoordinateDesc) {
        this.endXcoordinateDesc = endXcoordinateDesc;
        return setReapply(true);
    }

    public LineChatConfig setElementPadding(float elementPadding) {
        this.elementPadding = elementPadding;
        return setReapply(true);
    }

    public LineChatConfig setDuration(long duration) {
        this.duration = duration;
        return setReapply(true);
    }

    public LineChatConfig setAnimation(boolean animation) {
        this.animation = animation;
        return setReapply(true);
    }

    HashMap<String, InternalChatInfo> getChatMap() {
        return getChatMap(null);
    }

    HashMap<String, InternalChatInfo> getChatMap(HashMap<String, InternalChatInfo> map) {
        if (map != null) {
            map.putAll(mChatMap);
            return map;
        }
        return mChatMap;
    }

    protected boolean isReady() {
        return mChatHelper.isReady;
    }

    LineChatConfig setReapply(boolean reapply) {
        this.reapply = reapply;
        if (needPrepare) {
            needPrepare = true;
        }
        return this;
    }

    LineChatConfig setConfig(LineChatConfig config) {
        if (config != null) {
            setStartXcoordinateDesc(config.startXcoordinateDesc)
                    .setEndXcoordinateDesc(config.endXcoordinateDesc)
                    .setCoordinateLineColor(config.coordinateLineColor)
                    .setCoordinateLineWidth(config.coordinateLineWidth)
                    .setCoordinateTextColor(config.coordinateTextColor)
                    .setCoordinateTextSize(config.coordinateTextSize)
                    .setElementPadding(config.elementPadding)
                    .setyCoordinateAccuracyFloat(config.yCoordinateAccuracyFloat)
                    .setyCoordinateAccuracyLevel(config.yCoordinateAccuracyLevel)
                    .setDuration(config.duration)
                    .setAnimation(config.animation);
            HashMap<String, InternalChatInfo> maps = new HashMap<>();
            config.getChatMap(maps);
            mChatMap.clear();
            Iterator iterator = maps.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, InternalChatInfo> entry = (Map.Entry<String, InternalChatInfo>) iterator.next();
                addDatas(entry.getKey(), entry.getValue().getRawInfoList());
            }
        }
        return this;
    }


    class LineChatHelper {
        private boolean isReady;
        double maxValue;
        double minValue;

        LastAddedInfo mLastAddedInfo;
        private List<String> mYCoordinateDesc;
        Paint textPaint;
        Rect textBounds;
        LineChatPrepareConfig mPrepareConfig;
        List<InternalChatInfo> mChatLists;
        int xCoordinateLength;


        public LineChatHelper() {
            textPaint = new Paint();
            textBounds = new Rect();
        }

        LineChatConfig addData(String lineTag, ILineChatInfo info) {
            if (info == null) return setReapply(true);
            //减少每次add的时候都要去map那里寻找的操作
            if (mLastAddedInfo != null && mLastAddedInfo.equals(lineTag)) {
                mLastAddedInfo.lastAddedInternalChatInfo.add(info);
                record(info);
                return LineChatConfig.this;
            } else {
                mLastAddedInfo = null;
            }

            InternalChatInfo internalChatInfo = mChatMap.get(lineTag);
            if (internalChatInfo == null) {
                internalChatInfo = new InternalChatInfo(lineTag);
                mChatMap.put(lineTag, internalChatInfo);
            }
            internalChatInfo.add(info);

            if (mLastAddedInfo == null) {
                mLastAddedInfo = new LastAddedInfo(lineTag, internalChatInfo);
            }
            record(info);
            return setReapply(true);
        }

        private void record(ILineChatInfo info) {
            final double value = info.getValue();
            minValue = Math.min(value, maxValue);
            maxValue = Math.max(value, maxValue);
        }

        List<String> getYCoordinateDesc() {
            return getYCoordinateDesc(null);
        }

        List<String> getYCoordinateDesc(String formated) {
            if (mYCoordinateDesc == null) {
                mYCoordinateDesc = new ArrayList<>();
            }

            if (ToolUtil.isListEmpty(mYCoordinateDesc)) {
                boolean needFormated = !TextUtils.isEmpty(formated);

                double tMinValue = minValue - yCoordinateAccuracyFloat;
                double tMaxValue = maxValue + yCoordinateAccuracyFloat;

                double freq = Math.abs(tMaxValue - tMinValue) / yCoordinateAccuracyLevel;

                double curValue = tMinValue;

                while (curValue <= tMaxValue) {
                    curValue += freq;
                    String desc = sFormateRate.format(curValue);
                    desc = curValue < 0 ? desc : "+" + desc;
                    desc = needFormated ? String.format(Locale.getDefault(), formated, desc) : desc;
                    mYCoordinateDesc.add(desc);
                }
            }

            return mYCoordinateDesc;
        }

        Rect getCoordinateTextSize(String text) {
            text = TextUtils.isEmpty(text) ? MEASURE_TEXT : text;
            textPaint.setTextSize(coordinateTextSize);
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            return textBounds;
        }

        List<InternalChatInfo> getChatLists() {
            if (mChatLists == null) {
                mChatLists = new ArrayList<>();
            }
            if (ToolUtil.isListEmpty(mChatLists)) {
                Iterator iterator = mChatMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, InternalChatInfo> entry = (Map.Entry<String, InternalChatInfo>) iterator.next();
                    InternalChatInfo info = entry.getValue();
                    xCoordinateLength = Math.max(xCoordinateLength, info.getRawInfoListSize());
                    mChatLists.add(info.calculateYpercent(minValue - yCoordinateAccuracyFloat, maxValue + yCoordinateAccuracyFloat));
                }
            }
            return mChatLists;
        }


        void prepare(@Nullable LineChatPrepareConfig prepareConfig) {
            if (!needPrepare) return;
            isReady = false;
            mPrepareConfig = prepareConfig;
            //清除旧有数据
            clearData();
            if (prepareConfig != null) {
                getYCoordinateDesc(prepareConfig.mYcoordinateFormated);
            } else {
                getYCoordinateDesc(null);
            }
            getChatLists();
            needPrepare = false;
            isReady = true;
        }

        private void clearData() {
            if (!ToolUtil.isListEmpty(mYCoordinateDesc)) {
                mYCoordinateDesc.clear();
            }

            if (!ToolUtil.isListEmpty(mChatLists)) {
                mChatLists.clear();
            }

        }

        class LastAddedInfo {
            String lastLineTag;
            InternalChatInfo lastAddedInternalChatInfo;

            public LastAddedInfo(String lastLineTag, InternalChatInfo lastAddedInternalChatInfos) {
                this.lastLineTag = lastLineTag;
                this.lastAddedInternalChatInfo = lastAddedInternalChatInfos;
            }

            boolean equals(String lineTag) {
                return TextUtils.equals(lineTag, lastLineTag) && lastAddedInternalChatInfo != null;
            }
        }
    }


}
