package razerdp.com.widget.linechart;

import android.content.Context;

import razerdp.com.widget.linechart.config.LineChartConfig;
import razerdp.com.widget.linechart.manager.ChartManager;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public interface IChart {

    ChartManager getChartManager();

    Context getViewContext();

    public LineChartConfig getConfig();

}
