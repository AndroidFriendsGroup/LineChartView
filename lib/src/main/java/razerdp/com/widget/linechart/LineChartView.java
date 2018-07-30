package razerdp.com.widget.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ScrollView;

import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.manager.ChartManager;
import razerdp.com.widget.linechart.render.RenderManager;
import razerdp.com.widget.linechart.utils.ToolUtil;


/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class LineChartView extends View implements IChart {
    private static final String TAG = "LineChatView";

    private static final boolean DEBUG = false;

    private LineChartConfig mConfig;
    private ScrollView mScrollViewParent;
    private int maxFlingVertical;

    private float lastTouchY;

    private enum Mode {
        DRAW,
        TOUCH
    }

    private Mode mCurMode = Mode.DRAW;
    private RenderManager mRenderManager;
    private ChartManager mChartManager;

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        while (parent instanceof View) {
            if (parent instanceof ScrollView) {
                mScrollViewParent = (ScrollView) parent;
                break;
            }
            parent = parent.getParent();
        }
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mChartManager = new ChartManager(this);
        mRenderManager = new RenderManager(this);
        maxFlingVertical = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mConfig == null) {
            Log.e(TAG, "onDraw: config is null,abort draw");
            return;
        }
        mRenderManager.dispatchDraw(canvas);
    }

    public LineChartView applyConfig(LineChartConfig config) {
        this.mConfig = config;
        return this;
    }

    public void start() {
        start(this.mConfig);
    }

    public void start(LineChartConfig chartConfig) {
        applyConfig(chartConfig);
        post(new Runnable() {
            @Override
            public void run() {
                mRenderManager.prepare(new RenderManager.OnPrepareFinishListener() {
                    @Override
                    public void onFinish() {
                        invalidate();
                    }
                });
            }
        });
    }

    //-----------------------------------------view-----------------------------------------


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChartManager.setChartContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mScrollViewParent != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouchY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float curTouchY = event.getY();
                    if (lastTouchY == 0) {
                        lastTouchY = curTouchY;
                    }
                    if (Math.abs(curTouchY - lastTouchY) >= maxFlingVertical) {
                        mRenderManager.forceAbortTouch();
                        mScrollViewParent.requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                    lastTouchY = curTouchY;
                    break;
                case MotionEvent.ACTION_UP:
                    mScrollViewParent.requestDisallowInterceptTouchEvent(false);
                    lastTouchY = 0;
                    break;
                default:
                    mScrollViewParent.requestDisallowInterceptTouchEvent(true);
                    break;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
            } else {
                mScrollViewParent.requestDisallowInterceptTouchEvent(true);
            }
        }
        boolean handled = mRenderManager.onTouchEvent(event);
        if (handled) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    //-----------------------------------------IChart实现-----------------------------------------
    @Override
    public ChartManager getChartManager() {
        return mChartManager;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public LineChartConfig getConfig() {
        return mConfig;
    }

    @Override
    public View getChartView() {
        return this;
    }

    @Override
    public void onCallInvalidate() {
        if (ToolUtil.isMainThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
