package razerdp.com.widget.linechart.render;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razerdp.com.widget.linechart.IChart;
import razerdp.com.widget.linechart.axis.Axis;
import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.line.Line;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class RenderManager {
    IChart chart;
    List<BaseRender> renderLists;

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


    public void prepare() {
        if (chart.getConfig() == null) {
            throw new NullPointerException("config must be not null");
        }
        // TODO: 2018/1/30 可以考虑子线程
        prepareAxesRender(chart.getConfig());
        preapareLineChartRender(chart.getConfig());
    }

    private void prepareAxesRender(LineChartConfig config) {
        AxesRender render = (AxesRender) renderLists.get(0);
        render.reset();
        render.axisLabelPadding = config.getAxesLabelMargin() == 0 ? render.axisLabelPadding : config.getAxesLabelMargin();

        //x轴
        List<String> xLabels = config.getxAxesLabels();
        for (String xLabel : xLabels) {
            render.addXAxesData(new Axis(xLabel));
        }

        //y轴
        HashMap<String, Line> lines = config.getLinesMap();
        double maxValue = config.getMaxYAxisValue();
        double minValue = config.getMinYAxisValue();

        final int yAxesCount = config.getYAxesCount();
        double yPercent = (maxValue - minValue) / yAxesCount;
        for (int i = 0; i < yAxesCount; i++) {
            render.addYAxesData(new Axis());
        }


    }

    private void preapareLineChartRender(LineChartConfig config) {

    }

    public void dispatchDraw(Canvas canvas) {

    }

}
