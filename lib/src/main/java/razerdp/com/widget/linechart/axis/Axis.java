package razerdp.com.widget.linechart.axis;

/**
 * Created by 大灯泡 on 2018/1/30.
 */
public class Axis {

    private double value;
    private String label;

    public Axis() {
    }

    public Axis(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }


}
