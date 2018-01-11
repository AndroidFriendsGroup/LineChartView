package razerdp.com.widget.linechat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import razerdp.com.widget.util.ToolUtil;


/**
 * Created by 大灯泡 on 2018/1/11.
 */
public class LineChatView extends View {
    private static final String TAG = "LineChatView";

    private enum Mode {
        DRAW,
        TOUCH
    }

    private Mode mCurMode = Mode.DRAW;
    private LineChatConfig mConfig;
    private RectF mDrawRect;
    private boolean needResetDrawRect = false;


    public LineChatView(Context context) {
        this(context, null);
    }

    public LineChatView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mConfig == null) mConfig = new LineChatConfig();
        mDrawRect = new RectF();

    }

    void applyConfigInternal(LineChatConfig config) {
        if (config == null) {
            throw new NullPointerException("config为空");
        }
        mConfig.setConfig(config);
        if (mConfig.reapply) mConfig.setReapply(false);
    }

    public void applyConfig(LineChatConfig config) {
        if (config == null) return;
        applyConfigInternal(config);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mConfig.isReady()) return;
        initDrawRect();

        switch (mCurMode) {
            case DRAW:
                handleDrawMode(canvas);
                break;
            case TOUCH:
                break;
        }
    }

    private void handleDrawMode(Canvas canvas) {
        //绘制坐标
        drawCoordinate(canvas);

    }

    private void drawCoordinate(Canvas canvas) {
        List<String> yCoordinateDesc = mConfig.mChatHelper.getYCoordinateDesc("%1$s%%");
        if (ToolUtil.isListEmpty(yCoordinateDesc)) return;
        Rect textBounds = mConfig.mChatHelper.getCoordinateTextSize(null);
        float drawStartX = mDrawRect.left;
        //底部横坐标文字高度也要计算
        float drawStartY = mDrawRect.bottom - mConfig.mChatHelper.getCoordinateTextSize(null).height() - mConfig.elementPadding;
        //步幅
        float yFreq = mDrawRect.height() / yCoordinateDesc.size();

        float curY = drawStartY;
        for (String s : yCoordinateDesc) {
            canvas.drawText(s, drawStartX, curY, mConfig.coordinateTextPaint);
            float lineY = curY - (textBounds.height() >> 1);
            canvas.drawLine(drawStartX + textBounds.width() + mConfig.elementPadding,
                    lineY,
                    mDrawRect.right,
                    lineY,
                    mConfig.coordinateLinePaint);
            curY -= yFreq;
            if (curY <= mDrawRect.top) curY = mDrawRect.top + mConfig.elementPadding;
        }

    }

    //=============================================================tools

    private void initDrawRect() {
        if (mDrawRect.isEmpty() || needResetDrawRect) {
            float drawWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            float drawHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            float left = getPaddingLeft();
            float top = getPaddingTop();
            float right = left + drawWidth;
            float bottom = top + drawHeight;
            mDrawRect.set(left, top, right, bottom);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        needResetDrawRect = true;
    }
}
