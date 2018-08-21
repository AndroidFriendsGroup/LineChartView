package razerdp.com.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import razerdp.com.linechatview.R;
import razerdp.com.widget.linechart.LineChartView;
import razerdp.com.widget.linechart.callback.OnChartTouchListener;
import razerdp.com.widget.linechart.model.ILineChartInfo;
import razerdp.com.widget.linechart.model.SimpleLineChartInfo;
import razerdp.com.widget.linechart.config.LineChartConfig;

public class MainActivity extends AppCompatActivity {

    private LineChartView testView;
    TextView desc;
    TextView desc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        testView = (LineChartView) findViewById(R.id.testView);
        desc = findViewById(R.id.tv_desc);
        desc2 = findViewById(R.id.tv_desc2);

        final Random random = new Random();
        applyConfig(random);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyConfig(random);
            }
        });
        desc.setTextColor(Color.parseColor("#FD9726"));
        desc2.setTextColor(Color.parseColor("#41A1EA"));
    }

    private void applyConfig(Random random) {
        LineChartConfig config = new LineChartConfig();
        List<String> xAxes = new ArrayList<>();
        xAxes.add("12-18");
        xAxes.add("12-19");
        xAxes.add("12-20");
        xAxes.add("12-21");
        xAxes.add("12-21");
        xAxes.add("12-21");
        xAxes.add("12-21");
        xAxes.add("12-21");
        config.xAxisLabels(xAxes)
                .addDatas("line1", createData(random.nextDouble(), random, 20, Color.parseColor("#FD9726")))
                .addDatas("line2", createData(random.nextDouble(), random, 30, Color.parseColor("#41A1EA")))
                .enableTouchLine("line1", "line2")
                .axisStringFormat("%1$s%%")
                .axisValueFormat(new DecimalFormat("0.00"))
                .chartTouchListener("line1", new OnChartTouchListener() {
                    @Override
                    public void onChartSelected(String lineTag, int touchAction, ILineChartInfo data) {
                        desc.setText(String.format("%s  %s  value  >>  %s", lineTag, touchActionToString(touchAction), data == null ? "null" : data.getValue()));

                    }
                })
                .chartTouchListener("line2", new OnChartTouchListener() {
                    @Override
                    public void onChartSelected(String lineTag, int touchAction, ILineChartInfo data) {
                        desc2.setText(String.format("%s  %s  value  >>  %s", lineTag, touchActionToString(touchAction), data == null ? "null" : data.getValue()));
                    }
                });

        testView.start(config);
    }

    private List<SimpleLineChartInfo> createData(double start, Random random, int count, int color) {
        List<SimpleLineChartInfo> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SimpleLineChartInfo info = new SimpleLineChartInfo();
            info.setChatLineColor(color);
            float r = random.nextFloat();
            info.setValue(start + r / 10).setDesc(Double.toString(info.getValue()));
            result.add(info);
        }
   /*     result.add(createInfo(0.2, color));
        result.add(createInfo(0.4, color));
        result.add(createInfo(0.3, color));
        result.add(createInfo(0.6, color));
        result.add(createInfo(0.9, color));
        result.add(createInfo(0.1, color));
        result.add(createInfo(0.3, color));
        result.add(createInfo(0.5, color));
        result.add(createInfo(0.7, color));
        result.add(createInfo(0.6, color));
        result.add(createInfo(0.8, color));
        result.add(createInfo(0.2, color));
        result.add(createInfo(0.3, color));
        result.add(createInfo(0.5, color));
        result.add(createInfo(0.6, color));
        result.add(createInfo(1.2, color));*/

        return result;
    }

    private SimpleLineChartInfo createInfo(double value, int color) {
        SimpleLineChartInfo info = new SimpleLineChartInfo();
        info.setChatLineColor(color);
        info.setValue(value).setDesc(Double.toString(info.getValue()));
        return info;
    }

    String touchActionToString(int action) {
        switch (action) {
            case OnChartTouchListener.ACTION_DOWN:
                return "Action_down";
            case OnChartTouchListener.ACTION_MOVE:
                return "Action_move";
            case OnChartTouchListener.ACTION_UP:
                return "Action_up";
            default:
                return "Invalided";
        }

    }

}
