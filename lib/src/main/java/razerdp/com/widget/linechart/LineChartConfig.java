package razerdp.com.widget.linechart;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

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
public class LineChartConfig {
    private static final String TAG = "LineChartConfig";

    private static final String MEASURE_TEXT = "+99.99%";

    private static final DecimalFormat sFormateRate = new DecimalFormat("#.##");
    //坐标系
    private static final int DEFAULT_AXIS_LINE_WIDTH = 1;
    private static final int DEFAULT_AXIS_LINE_COLOR = Color.argb(125, 207, 207, 207);
    private static final int DEFAULT_AXIS_TEXT_SIZE = 24;
    private static final int DEFAULT_AXIS_TEXT_COLOR = Color.rgb(207, 207, 207);

    private static final int DEFAULT_Y_AXIS_COUNT = 6;//Y坐标精度：6个
    private static final float DEFAULT_ELEMENT_PADDING = 10;//元素之间的默认padding

    private static final long DEFAULT_ANIMATION_DURATION = 2000;
    private static final boolean DEFAULT_START_WITH_ANIMATION = true;

    private static final int DEFAULT_TOUCH_GUIDE_LINE_WIDTH = 2;
    private static final int DEFAULT_TOUCH_GUIDE_LINE_COLOR = Color.BLACK;
    private static final int DEFAULT_TOUCH_GUIDE_POINT_RADIUS = 4;
    private static final int DEFAULT_TOUCH_GUIDE_POINT_COLOR = Color.BLACK;

    //-----------------------------------------static value end-----------------------------------------

    int axisLineWidth = DEFAULT_AXIS_LINE_WIDTH;
    int axisLineColor = DEFAULT_AXIS_LINE_COLOR;
    int axisTextSize = DEFAULT_AXIS_TEXT_SIZE;
    int axisTextColor = DEFAULT_AXIS_TEXT_COLOR;

    int yCoordinateAccuracyLevel = DEFAULT_Y_AXIS_COUNT;

    float elementPadding = DEFAULT_ELEMENT_PADDING;

    String startXcoordinateDesc;
    String endXcoordinateDesc;

    long duration = DEFAULT_ANIMATION_DURATION;
    boolean animation = DEFAULT_START_WITH_ANIMATION;

    int touchGuideLineWidth = DEFAULT_TOUCH_GUIDE_LINE_WIDTH;
    int touchGuideLineColor = DEFAULT_TOUCH_GUIDE_LINE_COLOR;
    int touchGuidePointRadius = DEFAULT_TOUCH_GUIDE_POINT_RADIUS;
    int touchGuidePointColor = DEFAULT_TOUCH_GUIDE_POINT_COLOR;

    HashMap<String, OnLineChartSelectedListener> mChatSelectedListenerHashMap;
    List<String> highLineTag;

    //-----------------------------------------value config end-----------------------------------------


    LineChatHelper mChatHelper;
    LinkedHashMap<String, InternalChartInfo> mChatMap;
    volatile boolean reapply = true;
    boolean needPrepare = true;//如果重新设置过config，则需要重新prepare

    Paint coordinateTextPaint;
    Paint coordinateLinePaint;

    public LineChartConfig() {
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
        coordinateTextPaint.setTextSize(axisTextSize);
        coordinateTextPaint.setColor(axisTextColor);

        coordinateLinePaint.setStyle(Paint.Style.STROKE);
        coordinateLinePaint.setColor(axisLineColor);
        coordinateLinePaint.setStrokeWidth(axisLineWidth);
        coordinateLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
    }

    public LineChartConfig coordinateLineWidth(int coordinateLineWidth) {
        this.axisLineWidth = coordinateLineWidth;
        initPaint();
        return setReapply(true);
    }

    public LineChartConfig coordinateLineColor(int coordinateLineColor) {
        this.axisLineColor = coordinateLineColor;
        initPaint();
        return setReapply(true);
    }

    public LineChartConfig coordinateTextSize(int coordinateTextSize) {
        this.axisTextSize = coordinateTextSize;
        initPaint();
        return setReapply(true);
    }

    public LineChartConfig coordinateTextColor(int coordinateTextColor) {
        this.axisTextColor = coordinateTextColor;
        initPaint();
        return setReapply(true);
    }

    public LineChartConfig addDatas(String lineTag, List<? extends ILineChatrInfo> infos) {
        if (ToolUtil.isListEmpty(infos)) return this;
        for (ILineChatrInfo info : infos) {
            addData(lineTag, info);
        }
        return setReapply(true);
    }

    public LineChartConfig addData(String lineTag, ILineChatrInfo info) {
        return mChatHelper.addData(lineTag, info);
    }

    public LineChartConfig coordinateAccuracyLevel(int yCoordinateAccuracyLevel) {
        this.yCoordinateAccuracyLevel = yCoordinateAccuracyLevel;
        return setReapply(true);
    }

    public LineChartConfig xCoordinateDescForStart(String startXcoordinateDesc) {
        this.startXcoordinateDesc = startXcoordinateDesc;
        return setReapply(true);
    }

    public LineChartConfig xCoordinateDescForEnd(String endXcoordinateDesc) {
        this.endXcoordinateDesc = endXcoordinateDesc;
        return setReapply(true);
    }

    public LineChartConfig elementPadding(float elementPadding) {
        this.elementPadding = elementPadding;
        return setReapply(true);
    }

    public LineChartConfig animationDuration(long duration) {
        this.duration = duration;
        return setReapply(true);
    }

    public LineChartConfig animationDraw(boolean animation) {
        this.animation = animation;
        return setReapply(true);
    }

    public LineChartConfig touchGuideLineWidth(int touchGuideLineWidth) {
        this.touchGuideLineWidth = touchGuideLineWidth;
        return setReapply(true);
    }

    public LineChartConfig touchGuideLineColor(int touchGuidLineColor) {
        this.touchGuideLineColor = touchGuidLineColor;
        return setReapply(true);
    }

    public LineChartConfig touchGuidePointRadius(int touchGuidePointRadius) {
        this.touchGuidePointRadius = touchGuidePointRadius;
        return setReapply(true);
    }

    public LineChartConfig touchGuidePointColor(int touchGuidePointColor) {
        this.touchGuidePointColor = touchGuidePointColor;
        return setReapply(true);
    }

    public <T extends ILineChatrInfo> LineChartConfig addChatSelectedListener(String lineTag, OnLineChartSelectedListener<T> chatSelectedListener) {
        mChatSelectedListenerHashMap.put(lineTag, chatSelectedListener);
        return setReapply(true);
    }

    public LineChartConfig enableLineTouchPoint(String... lineTags) {
        if (lineTags == null) return this;
        Collections.addAll(highLineTag, lineTags);
        return setReapply(true);
    }

    HashMap<String, InternalChartInfo> getChatMap() {
        return getChatMap(null);
    }

    HashMap<String, InternalChartInfo> getChatMap(HashMap<String, InternalChartInfo> map) {
        if (map != null) {
            map.putAll(mChatMap);
            return map;
        }
        return mChatMap;
    }


    protected boolean isReady() {
        return mChatHelper.isReady;
    }

    LineChartConfig setReapply(boolean reapply) {
        this.reapply = reapply;
        if (reapply) {
            needPrepare = true;
        }
        return this;
    }

    LineChartConfig setConfig(LineChartConfig config) {
        if (config != null) {
            mChatHelper.reset();
            xCoordinateDescForStart(config.startXcoordinateDesc)
                    .xCoordinateDescForEnd(config.endXcoordinateDesc)
                    .coordinateLineColor(config.axisLineColor)
                    .coordinateLineWidth(config.axisLineWidth)
                    .coordinateTextColor(config.axisTextColor)
                    .coordinateTextSize(config.axisTextSize)
                    .elementPadding(config.elementPadding)
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


    private void copyConfigArrays(LineChartConfig config) {
        HashMap<String, InternalChartInfo> maps = new HashMap<>();
        config.getChatMap(maps);
        mChatMap.clear();
        Iterator iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, InternalChartInfo> entry = (Map.Entry<String, InternalChartInfo>) iterator.next();
            addDatas(entry.getKey(), entry.getValue().getRawInfoList());
        }
        HashMap<String, OnLineChartSelectedListener> listenerMaps = new HashMap<>();
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
        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;

        LastAddedInfo mLastAddedInfo;
        List<String> mYCoordinateDesc;
        List<Pair<Double, Double>> mYCoordinateValue;
        Paint textPaint;
        Rect textBounds;
        RectF lineBounds;
        LineChatPrepareConfig mPrepareConfig;
        List<InternalChartInfo> mChatLists;
        int xCoordinateLength;


        public LineChatHelper() {
            reset();
        }

        void reset() {
            textPaint = new Paint();
            textBounds = new Rect();
            lineBounds = new RectF();
            maxValue = Double.MIN_VALUE;
            minValue = Double.MAX_VALUE;
            isReady = false;
            mPrepareConfig = null;
            if (mChatLists != null) {
                mChatLists.clear();
            } else {
                mChatLists = new ArrayList<>();
            }
            xCoordinateLength = 0;
            if (mYCoordinateDesc != null) {
                mYCoordinateDesc.clear();
            } else {
                mYCoordinateDesc = new ArrayList<>();
            }

            if (mYCoordinateValue != null) {
                mYCoordinateValue.clear();
            } else {
                mYCoordinateValue = new ArrayList<>();
            }
            mLastAddedInfo = null;
        }

        LineChartConfig addData(String lineTag, ILineChatrInfo info) {
            if (info == null) return setReapply(true);
            //减少每次add的时候都要去map那里寻找的操作
            if (mLastAddedInfo != null && mLastAddedInfo.equals(lineTag)) {
                mLastAddedInfo.mLastAddedInternalChartInfo.add(info);
                xCoordinateLength = Math.max(xCoordinateLength, mLastAddedInfo.mLastAddedInternalChartInfo.getRawInfoListSize());
                record(info);
                return LineChartConfig.this;
            } else {
                mLastAddedInfo = null;
            }

            InternalChartInfo internalChartInfo = mChatMap.get(lineTag);
            if (internalChartInfo == null) {
                internalChartInfo = new InternalChartInfo(lineTag);
                mChatMap.put(lineTag, internalChartInfo);
            }
            internalChartInfo.add(info);
            xCoordinateLength = Math.max(xCoordinateLength, internalChartInfo.getRawInfoListSize());

            if (mLastAddedInfo == null) {
                mLastAddedInfo = new LastAddedInfo(lineTag, internalChartInfo);
            }
            record(info);
            return setReapply(true);
        }

        private void record(ILineChatrInfo info) {
            final double value = info.getValue();
            minValue = Math.min(value, minValue);
            maxValue = Math.max(value, maxValue);
        }

        List<String> getYCoordinateDesc() {
            return getYCoordinateDesc(null);
        }

        List<String> getYCoordinateDesc(String formated) {
            if (yCoordinateAccuracyLevel <= 1) {
                throw new IllegalArgumentException("y轴精度值不能小于2");
            }

//            double yCoordinateAccuracyFloat = (maxValue - minValue) / 5;
            double yCoordinateAccuracyFloat = 0;

            if (ToolUtil.isListEmpty(mYCoordinateDesc)) {
                boolean needFormated = !TextUtils.isEmpty(formated);

                final double tMinValue = minValue - yCoordinateAccuracyFloat;
                final double tMaxValue = maxValue + yCoordinateAccuracyFloat;

                Log.i(TAG, "min: " + tMinValue + "  max:  " + tMaxValue);

                double freq = (tMaxValue - tMinValue) / (yCoordinateAccuracyLevel - 1);
                double curValue = 0;
                double lastValue = 0;
                for (int i = 0; i < yCoordinateAccuracyLevel; i++) {
                    curValue = tMinValue + freq * i;
                    String desc = sFormateRate.format(curValue);
                    desc = (curValue < 0 ? "-" : "+") + desc;
                    desc = needFormated ? String.format(Locale.getDefault(), formated, desc) : desc;
                    mYCoordinateDesc.add(desc);
                    if (i > 0) {
                        mYCoordinateValue.add(Pair.create(lastValue, curValue));
                    }
                    lastValue = curValue;
                }
            }

            return mYCoordinateDesc;
        }

        Rect getCoordinateTextSize(String text) {
            text = TextUtils.isEmpty(text) ? MEASURE_TEXT : text;
            textPaint.setTextSize(axisTextSize);
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            return textBounds;
        }

        List<InternalChartInfo> getChatLists() {
            if (ToolUtil.isListEmpty(mChatLists)) {
                Iterator iterator = mChatMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, InternalChartInfo> entry = (Map.Entry<String, InternalChartInfo>) iterator.next();
                    InternalChartInfo info = entry.getValue();
                    mChatLists.add(info.calculatePosition(LineChartConfig.this));
                }
            }
            return mChatLists;
        }


        void prepare(@NonNull LineChatPrepareConfig prepareConfig) {
            if (!needPrepare || prepareConfig == null) return;
            isReady = false;
            mPrepareConfig = prepareConfig;
            calculateLineDrawBounds(prepareConfig.drawRectf);
            //清除旧有数据
            clearData();
            getYCoordinateDesc(prepareConfig.mYcoordinateFormated);
            getChatLists();
            needPrepare = false;
            isReady = true;
        }

        private void calculateLineDrawBounds(RectF drawRectf) {
            if (lineBounds != null) lineBounds.setEmpty();
            float drawStartY = drawRectf.bottom - getCoordinateTextSize(null).height() - elementPadding;
            lineBounds.set(drawRectf.left, drawRectf.top, drawRectf.right, drawStartY);
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
            InternalChartInfo mLastAddedInternalChartInfo;

            public LastAddedInfo(String lastLineTag, InternalChartInfo lastAddedInternalChartInfos) {
                this.lastLineTag = lastLineTag;
                this.mLastAddedInternalChartInfo = lastAddedInternalChartInfos;
            }

            boolean equals(String lineTag) {
                return TextUtils.equals(lineTag, lastLineTag) && mLastAddedInternalChartInfo != null;
            }
        }
    }


}
