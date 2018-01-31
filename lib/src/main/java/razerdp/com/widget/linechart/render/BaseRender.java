package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.manager.ChartManager;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public abstract class BaseRender {
    protected String TAG = this.getClass().getSimpleName();

    ChartManager mChartManager;
    IChart chart;

    public BaseRender(IChart chart) {
        this.chart = chart;
        this.mChartManager = chart.getChartManager();
        if (mChartManager != null) {
            mChartManager.registerRender(this);
        }
    }

    public void draw(Canvas canvas) {
        onDraw(canvas);
    }

    public ChartManager getChartManager() {
        return mChartManager;
    }

    public void callInvalidate() {
        chart.onCallInvalidate();
    }

    public abstract void onDraw(Canvas canvas);

    public abstract void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom);

    public abstract void reset();

    public abstract void prepare();

}
