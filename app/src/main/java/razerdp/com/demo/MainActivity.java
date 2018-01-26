package razerdp.com.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import razerdp.com.linechatview.R;
import razerdp.com.widget.linechat.LineChatConfig;
import razerdp.com.widget.linechat.LineChatPrepareConfig;
import razerdp.com.widget.linechat.LineChatView;
import razerdp.com.widget.linechat.OnLineChatSelectedListener;
import razerdp.com.widget.linechat.SimpleLineChatInfo;

public class MainActivity extends AppCompatActivity {

    private LineChatView testView;
    TextView desc;
    TextView desc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        testView = (LineChatView) findViewById(R.id.testView);
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
        LineChatConfig config = new LineChatConfig();
        config.addDatas("line1", createData(random.nextDouble(), random, 20, Color.parseColor("#FD9726")))
                        .addDatas("line2", createData(random.nextDouble(), random, 30, Color.parseColor("#41A1EA")))
                .xCoordinateDescForStart("2017-06-29")
                .xCoordinateDescForEnd("2018-01-12")
                .enableLineTouchPoint("line1", "line2")
                .addChatSelectedListener("line1", new OnLineChatSelectedListener<SimpleLineChatInfo>() {
                    @Override
                    public void onSelected(MotionEvent event, String lineTag, SimpleLineChatInfo data) {
                        desc.setText("line >>  " + lineTag + "  value  >>  " + data.getValue());

                    }
                })
                .addChatSelectedListener("line2", new OnLineChatSelectedListener<SimpleLineChatInfo>() {
                    @Override
                    public void onSelected(MotionEvent event, String lineTag, SimpleLineChatInfo data) {
                        desc2.setText("line2 >>  " + lineTag + "  value  >>  " + data.getValue());

                    }
                });
        testView.applyConfig(config).start(new LineChatPrepareConfig().setYcoordinateFormated("%s%%"));
    }

    private List<SimpleLineChatInfo> createData(double start, Random random, int count, int color) {
        List<SimpleLineChatInfo> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SimpleLineChatInfo info = new SimpleLineChatInfo();
            info.setChatLineColor(color);
            float r = random.nextFloat();
            info.setValue(start + r / 10).setDesc(Double.toString(info.getValue()));
            result.add(info);
        }
  /*      result.add(createInfo(0.2, color));
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
        result.add(createInfo(1.5, color));*/

        return result;
    }

    private SimpleLineChatInfo createInfo(double value, int color) {
        SimpleLineChatInfo info = new SimpleLineChatInfo();
        info.setChatLineColor(color);
        info.setValue(value).setDesc(Double.toString(info.getValue()));
        return info;
    }
}
