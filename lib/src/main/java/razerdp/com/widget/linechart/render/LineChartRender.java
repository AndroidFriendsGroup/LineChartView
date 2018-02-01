package razerdp.com.widget.linechart.render;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.callback.OnChartTouchListener;
import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.line.Line;
import razerdp.com.widget.linechart.line.PointInfo;
import razerdp.com.widget.linechart.model.ILineChatrInfo;
import razerdp.com.widget.linechart.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class LineChartRender extends BaseRender implements ITouchRender {

    private List<Line> mLines = new ArrayList<>();
    private Bitmap cachedBitmap;
    private Canvas cachedCanvas = new Canvas();
    private boolean isOnTouch = false;
    private float lastTouchX = -1;
    private float lastTouchY = -1;
    @OnChartTouchListener.TouchAction
    private int touchAction = OnChartTouchListener.INVALIDED;

    private Paint touchLinePaint;
    private Paint touchPointPaint;

    private ValueAnimator mLineAnimator;
    private boolean isAnimating;
    private boolean hasAnimated;
    private float animProcess;
    private PathMeasure mPathMeasure = new PathMeasure();
    private Path measurePath = new Path();

    public LineChartRender(IChart chart) {
        super(chart);
    }

    @Override
    public void onDraw(Canvas canvas) {
        LineChartConfig config = chart.getConfig();
        if (config == null) {
            Log.e(TAG, "no config found,abort prepare");
            return;
        }
        Canvas drawCanvas = canvas;
        if (cachedBitmap != null) {
            drawCanvas = cachedCanvas;
            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }

        if (config.isAnimLine()) {
            if (!isAnimating && !hasAnimated && mLineAnimator != null) {
                hasAnimated = true;
                mLineAnimator.start();
            }
        } else {
            if (mLineAnimator != null) {
                mLineAnimator.cancel();
            }
            isAnimating = false;
            animProcess = 1f;
        }
        //draw line
        if (config.isCubic()) {
            onDrawCubic(drawCanvas);
        } else {
            onDrawNormal(drawCanvas);
        }
        if (cachedBitmap != null) {
            canvas.drawBitmap(cachedBitmap, 0, 0, null);
        }
        if (isOnTouch && !isAnimating) {
            if (touchAction == OnChartTouchListener.ACTION_UP && lastTouchY == -1 && lastTouchX == -1) {
                for (Line line : mLines) {
                    if (line.getChartTouchListener() != null) {
                        line.getChartTouchListener().onChartSelected(null, touchAction, null);
                    }
                }
                touchAction = OnChartTouchListener.INVALIDED;
                isOnTouch = false;
                return;
            }

            if (!mChartManager.getChartLineDrawBounds().contains(lastTouchX, lastTouchY)) {
                isOnTouch = false;
                return;
            }
            prepareTouchPaint();
            onDrawOnTouch(canvas);
        }
    }

    private void onDrawCubic(Canvas canvas) {
        for (Line line : mLines) {
            drawLineCubic(canvas, line);
        }
    }

    private void onDrawNormal(Canvas canvas) {
        for (Line line : mLines) {
            drawLineNormal(canvas, line);
        }
    }

    private void drawLineNormal(Canvas canvas, Line line) {
        int pointIndex = 0;
        float prePointY = 0;
        Path path = line.getLinePath();
        Paint paint = line.getLinePaint();
        for (PointInfo pointInfo : line.getPoints()) {
            float x = pointInfo.getX();
            float y = pointInfo.getY();
            if (pointIndex == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            prePointY = y;
            pointIndex++;
        }
        if (isAnimating) {
            measurePath.rewind();
            mPathMeasure.nextContour();
            mPathMeasure.setPath(path, false);
            mPathMeasure.getSegment(0, animProcess * mPathMeasure.getLength(), measurePath, true);
            canvas.drawPath(measurePath, paint);
        } else {
            canvas.drawPath(path, paint);
        }
        path.rewind();
    }

    private void onDrawOnTouch(Canvas canvas) {
        List<String> lineTags = chart.getConfig().getTouchLineTag();
        if (ToolUtil.isListEmpty(lineTags)) {
            canvas.drawLine(lastTouchX, mChartManager.getChartLineDrawBounds().top, lastTouchX, mChartManager.getChartLineDrawBounds().bottom, touchLinePaint);
        } else {
            for (Line line : mLines) {
                String tag = line.getLineTag();
                canvas.drawLine(lastTouchX, mChartManager.getChartLineDrawBounds().top, lastTouchX, mChartManager.getChartLineDrawBounds().bottom, touchLinePaint);
                if (lineTags.contains(tag)) {
                    for (PointInfo pointInfo : line.getPoints()) {
                        if (pointInfo.inTouch(lastTouchX, chart.getConfig().getTouchPointRadius() / 2)) {
//                            canvas.drawLine(pointInfo.getX(), mChartManager.getChartLineDrawBounds().top, pointInfo.getX(), mChartManager.getChartLineDrawBounds().bottom, touchLinePaint);
                            canvas.drawCircle(pointInfo.getX(), pointInfo.getY(), chart.getConfig().getTouchPointRadius(), touchPointPaint);
                            if (line.getChartTouchListener() != null) {
                                line.getChartTouchListener().onChartSelected(tag, touchAction, pointInfo.getInfo());
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    private void drawLineCubic(Canvas canvas, Line line) {
        List<PointInfo> points = line.getPoints();
        if (ToolUtil.isListEmpty(points)) return;
        final int pointSize = points.size();
        //前1个点
        PointInfo prePoint = null;
        //当前点
        PointInfo curPoint = null;
        //下一个点
        PointInfo nextPoint = null;

        Path path = line.getLinePath();
        Paint paint = line.getLinePaint();

        for (int i = 0; i < pointSize; i++) {
            //当前点
            if (curPoint == null) {
                curPoint = points.get(i);
            }

            if (prePoint == null) {
                if (i > 0) {
                    prePoint = points.get(i - 1);
                } else {
                    //第0个则指向当前点
                    prePoint = curPoint;
                }
            }

            if (i < pointSize - 1) {
                nextPoint = points.get(i + 1);
            } else {
                nextPoint = curPoint;
            }

            if (i == 0) {
                path.moveTo(curPoint.getX(), curPoint.getY());
            } else {
                final float firstControlPointX = (curPoint.getX() + nextPoint.getX()) / 2;
                final float firstControlPointY = curPoint.getY();
                final float secondControlPointX = (curPoint.getX() + nextPoint.getX()) / 2;
                final float secondControlPointY = nextPoint.getY();
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        nextPoint.getX(), nextPoint.getY());
            }

            prePoint = curPoint;
            curPoint = nextPoint;

        }
        if (isAnimating) {
            measurePath.rewind();
            mPathMeasure.nextContour();
            mPathMeasure.setPath(path, false);
            mPathMeasure.getSegment(0, animProcess * mPathMeasure.getLength(), measurePath, true);
            canvas.drawPath(measurePath, paint);
        } else {
            canvas.drawPath(path, paint);
        }
        path.rewind();

    }

    @Override
    public void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        if (mChartManager.getDrawWidth() > 0 && mChartManager.getDrawHeight() > 0) {
            cachedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            cachedCanvas.setBitmap(cachedBitmap);
        }
    }

    @Override
    public void reset() {
        mLines.clear();
        mLineAnimator = null;
        isAnimating = false;
        hasAnimated = false;
        animProcess = 0f;
        mPathMeasure = new PathMeasure();
        measurePath = new Path();
        releaseCachedDraw();
    }

    @Override
    public void prepare() {
        reset();
        LineChartConfig config = chart.getConfig();
        if (config == null) {
            Log.e(TAG, "no config found,abort prepare");
            return;
        }
        if (config.isAnimLine()) {
            prepareLineAnima();
        }
        HashMap<String, Line> linesMap = config.getLinesMap();
        mLines.addAll(linesMap.values());
        int maxXAxesLength = 0;
        for (Line line : mLines) {
            String lineTag = line.getLineTag();
            line.setChartTouchListener(config.getOnChartTouchListenerHashMap().get(lineTag));
            maxXAxesLength = Math.max(line.getPoints().size(), maxXAxesLength);
        }
        double maxValue = mChartManager.getYAxisMaxValue();
        double minValue = mChartManager.getYAxisMinValue();

        float startX = mChartManager.getChartLineDrawBounds().left;
        float startY = mChartManager.getChartLineDrawBounds().bottom;
        float xMargin = mChartManager.getChartLineDrawBounds().width() / (maxXAxesLength - 1);
        float lineBoundsHeight = mChartManager.getChartLineDrawBounds().height();

        for (Line line : mLines) {
            line.preparePaint();
            int index = 0;
            for (PointInfo pointInfo : line.getPoints()) {
                ILineChatrInfo info = pointInfo.getInfo();
                if (info == null) continue;
                double value = info.getValue();

                float x = startX + xMargin * index;
                float y = (float) (startY - (value - minValue) / (maxValue - minValue) * lineBoundsHeight);
                pointInfo.set(x, y);
                index++;
            }
        }
    }

    private void prepareTouchPaint() {
        LineChartConfig config = chart.getConfig();
        if (touchLinePaint == null) {
            touchLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        if (touchPointPaint == null) {
            touchPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        touchLinePaint.setStyle(Paint.Style.STROKE);
        touchLinePaint.setStrokeWidth(config.getTouchLineWidth());
        touchLinePaint.setColor(config.getTouchLineColor());

        touchPointPaint.setStyle(Paint.Style.FILL);
        touchPointPaint.setColor(config.getTouchPointColor());

    }

    private void prepareLineAnima() {
        mLineAnimator = ValueAnimator.ofFloat(0f, 1f);
        mLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animProcess = (float) animation.getAnimatedValue();
                callInvalidate();
            }
        });
        mLineAnimator.setDuration(chart.getConfig() == null ? 2000 : chart.getConfig().getAnimaLineDuration());
        mLineAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
    }


    private void releaseCachedDraw() {
        if (cachedBitmap != null) {
            cachedBitmap.recycle();
        }
        cachedBitmap = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isOnTouch = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchAction = OnChartTouchListener.ACTION_DOWN;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                callInvalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                touchAction = OnChartTouchListener.ACTION_MOVE;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                callInvalidate();
                return false;
            case MotionEvent.ACTION_UP:
                touchAction = OnChartTouchListener.ACTION_UP;
                lastTouchX = -1;
                lastTouchY = -1;
                callInvalidate();
                return true;
        }
        return false;
    }

    @Override
    public void forceAbortTouch() {
        touchAction = OnChartTouchListener.ACTION_UP;
        lastTouchX = -1;
        lastTouchY = -1;
        callInvalidate();
    }
}
