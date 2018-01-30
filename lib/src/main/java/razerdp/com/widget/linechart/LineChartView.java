package razerdp.com.widget.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.manager.ChartManager;
import razerdp.com.widget.linechart.render.RenderManager;


/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class LineChartView extends View implements IChart {
    private static final String TAG = "LineChatView";

    private static final boolean DEBUG = false;

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

    private void init() {
        mChartManager = new ChartManager(this);
        mRenderManager = new RenderManager(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //-----------------------------------------view-----------------------------------------


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChartManager.setChartContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
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
        return null;
    }
}
