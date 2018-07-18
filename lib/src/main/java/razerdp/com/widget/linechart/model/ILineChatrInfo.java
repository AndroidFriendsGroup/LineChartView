package razerdp.com.widget.linechart.model;

/**
 * Created by 大灯泡 on 2018/1/11.
 */
public interface ILineChatrInfo {

    int getChartLineWidth();

    int getChatrLineColor();

    String getDesc();

    double getValue();

    boolean isHightLight();

    int getHightLightRadius();

    int getHightLightColor();

    boolean withShadow();
}
