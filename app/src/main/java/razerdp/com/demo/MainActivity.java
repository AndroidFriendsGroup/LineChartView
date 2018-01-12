package razerdp.com.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import razerdp.com.linechatview.R;
import razerdp.com.widget.linechat.LineChatConfig;
import razerdp.com.widget.linechat.LineChatPrepareConfig;
import razerdp.com.widget.linechat.LineChatView;
import razerdp.com.widget.linechat.SimpleLineChatInfo;

public class MainActivity extends AppCompatActivity {

    private LineChatView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        testView = (LineChatView) findViewById(R.id.testView);
        LineChatConfig config = new LineChatConfig();
        List<SimpleLineChatInfo> simpleLineChatInfos = new ArrayList<>();
        double start = 0.18f;
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            SimpleLineChatInfo info = new SimpleLineChatInfo();
            float r = random.nextFloat();
            info.setValue(start + r);
            simpleLineChatInfos.add(info);
        }
        config.addDatas("line1", simpleLineChatInfos)
                .setStartXcoordinateDesc("2017-06-29")
                .setEndXcoordinateDesc("2018-01-12");

        testView.applyConfig(config).start(new LineChatPrepareConfig().setYcoordinateFormated("%s%%"));
    }
}
