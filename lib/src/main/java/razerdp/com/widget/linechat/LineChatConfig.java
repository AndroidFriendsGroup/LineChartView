package razerdp.com.widget.linechat;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final float DEFAULT_Y_COORDINATE_ACCURACY_FLOAT = 0.25f;//y坐标10%浮动
    private static final float DEFAULT_ELEMENT_PADDING = 10;//元素之间的默认padding

    private static final long DEFAULT_ANIMATION_DURATION = 2000;
    private static final boolean DEFAULT_START_WITH_ANIMATION = true;

    private static final int DEFAULT_TOUCH_GUIDE_LINE_WIDTH = 2;
    private static final int DEFAULT_TOUCH_GUIDE_LINE_COLOR = Color.BLACK;
    private static final int DEFAULT_TOUCH_GUIDE_POINT_RADIUS = 8;
    private static final int DEFAULT_TOUCH_GUIDE_POINT_COLOR = Color.BLACK;

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

    int touchGuideLineWidth = DEFAULT_TOUCH_GUIDE_LINE_WIDTH;
    int touchGuideLineColor = DEFAULT_TOUCH_GUIDE_LINE_COLOR;
    int touchGuidePointRadius = DEFAULT_TOUCH_GUIDE_POINT_RADIUS;
    int touchGuidePointColor = DEFAULT_TOUCH_GUIDE_POINT_COLOR;

    HashMap<String, OnLineChatSelectedListener> mChatSelectedListenerHashMap;
    List<String> highLineTag;

    //-----------------------------------------value config end-----------------------------------------


    LineChatHelper mChatHelper;
    LinkedHashMap<String, InternalChatInfo> mChatMap;
    volatile boolean reapply = true;
    boolean needPrepare = true;//如果重新设置过config，则需要重新prepare

    Paint coordinateTextPaint;
    Paint coordinateLinePaint;

    public LineChatConfig() {
        mChatHelper = new LineChatHelper();
        mChatMap = new LinkedHashMap<>();
        mChatSelectedListenerHashMap = new HashMap<>();
        highLineTag = new ArrayList<>();
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

    public LineChatConfig coordinateLineWidth(int coordinateLineWidth) {
        this.coordinateLineWidth = coordinateLineWidth;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig coordinateLineColor(int coordinateLineColor) {
        this.coordinateLineColor = coordinateLineColor;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig coordinateTextSize(int coordinateTextSize) {
        this.coordinateTextSize = coordinateTextSize;
        initPaint();
        return setReapply(true);
    }

    public LineChatConfig coordinateTextColor(int coordinateTextColor) {
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

    public LineChatConfig coordinateAccuracyLevel(int yCoordinateAccuracyLevel) {
        this.yCoordinateAccuracyLevel = yCoordinateAccuracyLevel;
        return setReapply(true);
    }

    public LineChatConfig coordinateAccuracyFloat(float yCoordinateAccuracyFloat) {
        this.yCoordinateAccuracyFloat = yCoordinateAccuracyFloat;
        return setReapply(true);
    }

    public LineChatConfig xCoordinateDescForStart(String startXcoordinateDesc) {
        this.startXcoordinateDesc = startXcoordinateDesc;
        return setReapply(true);
    }

    public LineChatConfig xCoordinateDescForEnd(String endXcoordinateDesc) {
        this.endXcoordinateDesc = endXcoordinateDesc;
        return setReapply(true);
    }

    public LineChatConfig elementPadding(float elementPadding) {
        this.elementPadding = elementPadding;
        return setReapply(true);
    }

    public LineChatConfig animationDuration(long duration) {
        this.duration = duration;
        return setReapply(true);
    }

    public LineChatConfig animationDraw(boolean animation) {
        this.animation = animation;
        return setReapply(true);
    }

    public LineChatConfig touchGuideLineWidth(int touchGuideLineWidth) {
        this.touchGuideLineWidth = touchGuideLineWidth;
        return setReapply(true);
    }

    public LineChatConfig touchGuideLineColor(int touchGuidLineColor) {
        this.touchGuideLineColor = touchGuidLineColor;
        return setReapply(true);
    }

    public LineChatConfig touchGuidePointRadius(int touchGuidePointRadius) {
        this.touchGuidePointRadius = touchGuidePointRadius;
        return setReapply(true);
    }

    public LineChatConfig touchGuidePointColor(int touchGuidePointColor) {
        this.touchGuidePointColor = touchGuidePointColor;
        return setReapply(true);
    }

    public <T extends ILineChatInfo> LineChatConfig addChatSelectedListener(String lineTag, OnLineChatSelectedListener<T> chatSelectedListener) {
        mChatSelectedListenerHashMap.put(lineTag, chatSelectedListener);
        return setReapply(true);
    }

    public LineChatConfig enableLineTouchPoint(String... lineTags) {
        if (lineTags == null) return this;
        Collections.addAll(highLineTag, lineTags);
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
            xCoordinateDescForStart(config.startXcoordinateDesc)
                    .xCoordinateDescForEnd(config.endXcoordinateDesc)
                    .coordinateLineColor(config.coordinateLineColor)
                    .coordinateLineWidth(config.coordinateLineWidth)
                    .coordinateTextColor(config.coordinateTextColor)
                    .coordinateTextSize(config.coordinateTextSize)
                    .elementPadding(config.elementPadding)
                    .coordinateAccuracyFloat(config.yCoordinateAccuracyFloat)
                    .coordinateAccuracyLevel(config.yCoordinateAccuracyLevel)
                    .animationDuration(config.duration)
                    .animationDraw(config.animation)
                    .touchGuideLineWidth(config.touchGuideLineWidth)
                    .touchGuideLineColor(config.touchGuideLineColor)
                    .touchGuidePointRadius(config.touchGuidePointRadius)
                    .touchGuidePointColor(config.touchGuidePointColor);
            copyConfigArrays(config);

        }
        return this;
    }

    private void copyConfigArrays(LineChatConfig config) {
        HashMap<String, InternalChatInfo> maps = new HashMap<>();
        config.getChatMap(maps);
        mChatMap.clear();
        Iterator iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, InternalChatInfo> entry = (Map.Entry<String, InternalChatInfo>) iterator.next();
            addDatas(entry.getKey(), entry.getValue().getRawInfoList());
        }
        HashMap<String, OnLineChatSelectedListener> listenerMaps = new HashMap<>();
        if (!config.mChatSelectedListenerHashMap.isEmpty()) {
            listenerMaps.putAll(config.mChatSelectedListenerHashMap);
            mChatSelectedListenerHashMap.clear();
            mChatSelectedListenerHashMap.putAll(listenerMaps);
        }

        List<String> saves = new ArrayList<>();
        if (!config.highLineTag.isEmpty()) {
            saves.addAll(config.highLineTag);
            highLineTag.clear();
            highLineTag.addAll(saves);
        }

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
                xCoordinateLength = Math.max(xCoordinateLength, mLastAddedInfo.lastAddedInternalChatInfo.getRawInfoListSize());
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
            xCoordinateLength = Math.max(xCoordinateLength, internalChatInfo.getRawInfoListSize());

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
                    mChatLists.add(info.calculatePosition(LineChatConfig.this));
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
