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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        testView = (LineChatView) findViewById(R.id.testView);
        desc = findViewById(R.id.tv_desc);
        final Random random = new Random();

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineChatConfig config = new LineChatConfig();
                config.addDatas("line1", createData(random.nextDouble(), random, 20, Color.parseColor("#FD9726")))
                        .addDatas("line2", createData(random.nextDouble(), random, 30, Color.parseColor("#41A1EA")))
                        .xCoordinateDescForStart("2017-06-29")
                        .xCoordinateDescForEnd("2018-01-12")
                        .enableLineTouchPoint("line1")
                        .addChatSelectedListener("line1", new OnLineChatSelectedListener<SimpleLineChatInfo>() {
                            @Override
                            public void onSelected(MotionEvent event, String lineTag, SimpleLineChatInfo data) {
                                desc.setText("line >>  " + lineTag + "  value  >>  " + data.getValue());

                            }
                        });
                testView.applyConfig(config).start(new LineChatPrepareConfig().setYcoordinateFormated("%s%%"));
            }
        });
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
        return result;
    }
}
