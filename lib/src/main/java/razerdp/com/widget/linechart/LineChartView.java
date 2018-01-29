package razerdp.com.widget.linechart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import razerdp.com.widget.util.ToolUtil;


/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class LineChartView extends View {
    private static final String TAG = "LineChatView";

    private static final boolean DEBUG = false;

    private enum Mode {
        DRAW,
        TOUCH
    }

    private Mode mCurMode = Mode.DRAW;
    private LineChartConfig mConfig;
    private RectF mDrawRect;
    private Paint touchGuideLinePaint;
    private Paint touchGuidePointPaint;
    private boolean needResetDrawRect = false;
    private volatile boolean isInAnimating;
    private PathMeasure mPathMeasure;
    private volatile float mAnimationInterpolatedTime;

    private float lastTouchX;
    private float lastTouchY;
    private MotionEvent lastEvent;


    private volatile boolean mPaintFinish = false;
    private boolean callInvalidateByUser = false;

    private Bitmap mDrawBitmap;//使用bitmap缓存，防止onDraw里面有太多的绘制导致卡顿
    private Canvas mPainterCanvas;


    //debug
    Paint prePointP;
    Paint curPointP;
    Paint afterPointP;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mConfig == null) mConfig = new LineChartConfig();
        mDrawRect = new RectF();
        mPathMeasure = new PathMeasure();
    }

    void applyConfigInternal(LineChartConfig config) {
        if (config == null) {
            throw new NullPointerException("config为空");
        }
        mConfig.setConfig(config);
        if (touchGuideLinePaint == null) {
            touchGuideLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            touchGuideLinePaint.setStyle(Paint.Style.STROKE);
        }
        touchGuideLinePaint.setStrokeWidth(mConfig.touchGuideLineWidth);
        touchGuideLinePaint.setColor(mConfig.touchGuideLineColor);

        if (touchGuidePointPaint == null) {
            touchGuidePointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            touchGuidePointPaint.setStyle(Paint.Style.FILL);
        }
        touchGuidePointPaint.setColor(mConfig.touchGuidePointColor);

        if (mConfig.reapply) mConfig.setReapply(false);
    }

    public LineChartView applyConfig(LineChartConfig config) {
        if (config == null) return this;
        applyConfigInternal(config);
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mConfig.isReady()) return;
        initDrawRect();
        if (mDrawBitmap == null || mPainterCanvas == null) {
            mDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mPainterCanvas = new Canvas(mDrawBitmap);
        }
        handleDraw(canvas, mPaintFinish);
        if (!mConfig.animation && callInvalidateByUser) {
            //动画会在动画的最后重置标志位，没有动画的话则需要判断是否用户执行的invalided也就是start方法
            mPaintFinish = true;
            callInvalidateByUser = false;
        }
    }

    private void handleDraw(Canvas canvas, boolean drawCache) {
        //绘制坐标
        drawAxis(drawCache ? canvas : mPainterCanvas, drawCache);
        //绘制折线
        drawLineChat(drawCache ? canvas : mPainterCanvas, drawCache);
        if (!drawCache) {
            canvas.drawBitmap(mDrawBitmap, 0, 0, null);
        }
    }

    private void drawAxis(Canvas canvas, boolean drawCache) {
        if (drawCache) {
            canvas.drawBitmap(mDrawBitmap, 0, 0, null);
            return;
        }
        List<String> yCoordinateDesc = mConfig.mChatHelper.getYCoordinateDesc();
        if (ToolUtil.isListEmpty(yCoordinateDesc)) return;
        Rect textBounds = mConfig.mChatHelper.getCoordinateTextSize(null);
        float drawStartX = mConfig.mChatHelper.lineBounds.left;
        //底部横坐标文字高度也要计算
        final float drawStartY = mConfig.mChatHelper.lineBounds.bottom;

        for (int i = 0; i < yCoordinateDesc.size(); i++) {
            String s = yCoordinateDesc.get(i);
            float curY = drawStartY - (mConfig.mChatHelper.lineBounds.height() * i / mConfig.yCoordinateAccuracyLevel);

            canvas.drawText(s, drawStartX, curY, mConfig.coordinateTextPaint);
            //坐标轴在文字的中间
            canvas.drawLine(drawStartX + textBounds.width() + mConfig.elementPadding,
                    curY - (textBounds.height() >> 1),
                    mConfig.mChatHelper.lineBounds.right,
                    curY - (textBounds.height() >> 1),
                    mConfig.coordinateLinePaint);
        }
        canvas.drawRect(mConfig.mChatHelper.lineBounds, mConfig.coordinateLinePaint);

        if (!TextUtils.isEmpty(mConfig.startXcoordinateDesc)) {
            //横坐标文字
            float drawXcoorStartX = drawStartX + textBounds.width();
            float drawXcoorStartY = mDrawRect.bottom;
            canvas.drawText(mConfig.startXcoordinateDesc, drawXcoorStartX, drawXcoorStartY, mConfig.coordinateTextPaint);
        }

        if (!TextUtils.isEmpty(mConfig.endXcoordinateDesc)) {
            Rect xCoorTextRect = mConfig.mChatHelper.getCoordinateTextSize(mConfig.endXcoordinateDesc);
            float drawXcoorEndX = mDrawRect.right - xCoorTextRect.width();
            float drawXcoorEndY = mDrawRect.bottom;
            canvas.drawText(mConfig.endXcoordinateDesc, drawXcoorEndX, drawXcoorEndY, mConfig.coordinateTextPaint);
        }


    }

    private void drawLineChat(Canvas canvas, boolean drawCache) {
        List<InternalChartInfo> chatLineLists = mConfig.mChatHelper.getChatLists();
        if (ToolUtil.isListEmpty(chatLineLists)) return;
        Rect textBounds = mConfig.mChatHelper.getCoordinateTextSize(null);

        float xFreq = mDrawRect.width() / mConfig.mChatHelper.xCoordinateLength;

        final int size = mConfig.mChatHelper.xCoordinateLength;
        final float contentHeight = mDrawRect.height() - textBounds.height() - mConfig.elementPadding;

        List<InternalChartInfo> list = mConfig.mChatHelper.getChatLists();
        if (ToolUtil.isListEmpty(list)) return;

        float startX = 0;

        if (drawCache) {
            canvas.drawBitmap(mDrawBitmap, 0, 0, null);
        } else {
            for (int i = 0; i < size - 1; i++) {
                for (InternalChartInfo info : list) {
                    if (startX == 0) startX = info.startX;
                    Path p = info.linePath;
                    LineChatrInfoWrapper curInfoWrapper = info.getInfo(i);
                    LineChatrInfoWrapper nextInfoWrapper = info.getInfo(i + 1);

                    if (curInfoWrapper != null && nextInfoWrapper != null) {
                        //前控制点
                        float preControlX = 0;
                        float preControlY = 0;
                        //后控制点
                        float afterControlX = 0;
                        float afterControlY = 0;

                        //0的时候reset
                        if (i == 0) {
                            p.reset();
                            p.moveTo(curInfoWrapper.getX(), curInfoWrapper.getY());
                        }

                        preControlX = (curInfoWrapper.getX() + nextInfoWrapper.getX()) / 2;
                        preControlY = curInfoWrapper.getY();

                        afterControlX = (curInfoWrapper.getX() + nextInfoWrapper.getX()) / 2;
                        afterControlY = nextInfoWrapper.getY();

                        float endX = nextInfoWrapper.getX();
                        float endY = nextInfoWrapper.getY();
//                    p.lineTo(curInfoWrapper.getX(), curInfoWrapper.getY());
                        p.cubicTo(preControlX, preControlY, afterControlX, afterControlY, endX, endY);
                        if (isInAnimating) {
                            Path measurePath = info.measureLinePath;
                            measurePath.rewind();
                            mPathMeasure.nextContour();
                            mPathMeasure.setPath(p, false);
                            mPathMeasure.getSegment(0, mAnimationInterpolatedTime * mPathMeasure.getLength(), measurePath, true);
                            canvas.drawPath(measurePath, info.linePaint);
                        } else {
                            canvas.drawPath(p, info.linePaint);
                        }

                        //debug用
                        if (DEBUG) {
                            canvas.drawCircle(preControlX, preControlY, 4, prePointP);
                            canvas.drawCircle(endX, endY, 8, curPointP);
                            canvas.drawCircle(afterControlX, afterControlY, 4, afterPointP);

                            canvas.drawLine(curInfoWrapper.getX(), curInfoWrapper.getY(), preControlX, preControlY, mConfig.coordinateLinePaint);
                            canvas.drawLine(nextInfoWrapper.getX(), nextInfoWrapper.getY(), afterControlX, afterControlY, mConfig.coordinateLinePaint);
                        }
                    }
                }
            }
        }
        if (mCurMode == Mode.TOUCH && !isInAnimating) {
            //手势
            // FIXME: 2018/1/23 因为touch位置跟数据位置不一致，导致可能发生的触摸位置跟显示的位置不一致的问题
            int index = (int) ((lastTouchX - startX) / xFreq);
            for (InternalChartInfo info : list) {
                if (startX == 0) startX = info.startX;
                LineChatrInfoWrapper curInfoWrapper = info.getInfo(index);
                if (curInfoWrapper != null) {
                    OnLineChartSelectedListener listener = mConfig.mChatSelectedListenerHashMap.get(info.lineTag);
                    if (listener != null) {
                        listener.onSelected(lastEvent, info.lineTag, curInfoWrapper.mInfo);
                    }
                    canvas.drawLine(curInfoWrapper.getX(), mConfig.mChatHelper.lineBounds.top, curInfoWrapper.getX(), mConfig.mChatHelper.lineBounds.bottom, touchGuideLinePaint);
                    if (mConfig.highLineTag.contains(info.lineTag)) {
                        canvas.drawCircle(curInfoWrapper.getX(), curInfoWrapper.getY(), mConfig.touchGuidePointRadius, touchGuidePointPaint);
                    }
                    if (lastEvent != null && lastEvent.getAction() == MotionEvent.ACTION_UP) {
                        setMode(Mode.DRAW);
                    }
                }
            }
        }
    }


    public void start() {
        start(null);
    }

    public void start(LineChatPrepareConfig prepareConfig) {
        if (isInAnimating) return;
        if (mConfig.animation) {
            isInAnimating = true;
        }
        if (mConfig.reapply) {
            applyConfigInternal(mConfig);
        }
        if (mDrawBitmap != null) {
            mDrawBitmap.recycle();
            mDrawBitmap = null;
        }
        mPainterCanvas = null;
        lastEvent = null;
        final LineChatPrepareConfig config = prepareConfig == null ? new LineChatPrepareConfig() : prepareConfig;
        post(new Runnable() {
            @Override
            public void run() {
                initDrawRect();
                config.setDrawRect(mDrawRect);
                mConfig.mChatHelper.prepare(config);
                if (mConfig.animation) {
                    startWithAnima();
                } else {
                    callInvalidateByUser = true;
                    invalidate();
                }
            }
        });
    }

    private void startWithAnima() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(mConfig.duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationInterpolatedTime = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isInAnimating = true;
                mPaintFinish = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isInAnimating = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isInAnimating = false;
                mPaintFinish = true;
            }
        });
        animator.start();
    }

    //=============================================================touch

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInAnimating) return super.onTouchEvent(event);
        lastEvent = event;
        lastTouchX = event.getX();
        lastTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setMode(Mode.TOUCH);
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                setMode(Mode.TOUCH);
                break;
        }
        return super.onTouchEvent(event);
    }


    //=============================================================tools

    private void initDrawRect() {
        if (mDrawRect.isEmpty() || needResetDrawRect) {
            float drawWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            float drawHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            float left = getPaddingLeft();
            float top = getPaddingTop();
            float right = left + drawWidth;
            float bottom = top + drawHeight;
            mDrawRect.set(left, top, right, bottom);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        needResetDrawRect = true;
    }

    private void initDebug() {
        if (!DEBUG) return;
        if (prePointP == null) {
            prePointP = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        if (curPointP == null) {
            curPointP = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        if (afterPointP == null) {
            afterPointP = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        prePointP.setColor(Color.RED);
        curPointP.setColor(Color.GREEN);
        afterPointP.setColor(Color.BLUE);
    }

    void setMode(Mode curMode) {
        mCurMode = curMode;
        if (mCurMode == Mode.DRAW) {
            lastTouchX = lastTouchY = -1;
            lastEvent = null;
        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDrawBitmap != null) {
            mDrawBitmap.recycle();
            mDrawBitmap = null;
        }
        mPainterCanvas = null;
        super.onDetachedFromWindow();
    }
}
