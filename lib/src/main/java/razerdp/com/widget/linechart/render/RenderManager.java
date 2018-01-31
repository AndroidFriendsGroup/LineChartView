package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.axis.Axis;
import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.line.Line;
import razerdp.com.widget.linechart.utils.ThreadPoolUtils;
import razerdp.com.widget.linechart.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class RenderManager implements ITouchRender {
    IChart chart;
    List<BaseRender> renderLists;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public RenderManager(IChart chart) {
        this.chart = chart;
        if (renderLists == null) {
            renderLists = new ArrayList<>();
        } else {
            renderLists.clear();
        }
        renderLists.add(new AxesRender(chart));
        renderLists.add(new LineChartRender(chart));
    }


    public void prepare(@Nullable final OnPrepareFinishListener l) {
        if (chart.getConfig() == null) {
            throw new NullPointerException("config must be not null");
        }
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                prepareAsync(l);
            }
        });

    }

    private void prepareAsync(final OnPrepareFinishListener l) {
        prepareAxesRender(chart.getConfig());
        prepareLineChartRender(chart.getConfig());
        if (l != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    l.onFinish();
                }
            });
        }
    }

    private void prepareAxesRender(LineChartConfig config) {
        AxesRender render = (AxesRender) renderLists.get(0);
        render.reset();

        //x轴
        List<String> xLabels = config.getxAxesLabels();
        for (String xLabel : xLabels) {
            render.addXAxesData(new Axis(xLabel));
        }

        //y轴
        HashMap<String, Line> lines = config.getLinesMap();
        double maxValue = config.getMaxYAxisValue();
        double minValue = config.getMinYAxisValue();

        double exRange = (maxValue - minValue) / 5;
        if (exRange == 0) {
            exRange = maxValue / 5;
        }

        maxValue += exRange;
        minValue -= exRange;

        chart.getChartManager().setYAxisMinValue(minValue);
        chart.getChartManager().setYAxisMaxValue(maxValue);

        final int yAxesCount = config.getYAxesCount();
        double yPercent = (maxValue - minValue) / yAxesCount;
        String format = config.getyAxisStringFormat();
        DecimalFormat valueFormat = config.getyAxisValueFormat();
        for (int i = 0; i < yAxesCount; i++) {
            Axis axis = new Axis();
            double value = minValue + yPercent * i;
            if (!TextUtils.isEmpty(format)) {
                axis.setLabel(String.format(Locale.getDefault(), format, valueFormat == null ? Double.toString(value) : valueFormat.format(value)));
            } else {
                axis.setLabel(valueFormat == null ? Double.toString(value) : valueFormat.format(value));
            }
            render.addYAxesData(axis);
        }

        render.prepare();

    }

    private void prepareLineChartRender(LineChartConfig config) {
        LineChartRender render = (LineChartRender) renderLists.get(1);
        render.prepare();
    }

    public void dispatchDraw(Canvas canvas) {
        for (BaseRender renderList : renderLists) {
            renderList.draw(canvas);
        }
    }

    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        if (!ToolUtil.isListEmpty(renderLists)) {
            renderLists.clear();
        }
        renderLists = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (BaseRender render : renderLists) {
            if (render instanceof ITouchRender) {
                boolean handled = ((ITouchRender) render).onTouchEvent(event);
                if (handled) return true;
            }
        }
        return false;
    }

    @Override
    public void forceAbortTouch() {
        for (BaseRender render : renderLists) {
            if (render instanceof ITouchRender) {
                ((ITouchRender) render).forceAbortTouch();
            }
        }
    }


    public interface OnPrepareFinishListener {
        void onFinish();
    }

}
