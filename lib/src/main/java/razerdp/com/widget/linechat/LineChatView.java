package razerdp.com.widget.linechat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
    private volatile boolean isInAnimating;


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

    public LineChatView applyConfig(LineChatConfig config) {
        if (config == null) return this;
        applyConfigInternal(config);
        return this;
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
        //绘制折线
        drawLineChat(canvas);

    }

    private void drawCoordinate(Canvas canvas) {
        List<String> yCoordinateDesc = mConfig.mChatHelper.getYCoordinateDesc();
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

        if (!TextUtils.isEmpty(mConfig.startXcoordinateDesc)) {
            //横坐标文字
            float drawXcoorStartX = drawStartX + textBounds.width();
            float drawXcoorStartY = mDrawRect.bottom;
            canvas.drawText(mConfig.startXcoordinateDesc, drawXcoorStartX, drawXcoorStartY, mConfig.coordinateTextPaint);
        }

        if (!TextUtils.isEmpty(mConfig.endXcoordinateDesc)) {
            Rect xCoorTextRect = mConfig.mChatHelper.getCoordinateTextSize(mConfig.endXcoordinateDesc);
            float drawXcoorEndX = mDrawRect.right - xCoorTextRect.width();
            float drawXcoorEndY = mDrawRect.bottom;
            canvas.drawText(mConfig.endXcoordinateDesc, drawXcoorEndX, drawXcoorEndY, mConfig.coordinateTextPaint);
        }


    }

    //debug
    Paint prePointP = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint curPointP = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint afterPointP = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void drawLineChat(Canvas canvas) {
        prePointP.setColor(Color.RED);
        curPointP.setColor(Color.GREEN);
        afterPointP.setColor(Color.BLUE);

        List<InternalChatInfo> chatLineLists = mConfig.mChatHelper.getChatLists();
        if (ToolUtil.isListEmpty(chatLineLists)) return;
        Rect textBounds = mConfig.mChatHelper.getCoordinateTextSize(null);

        float xFreq = mDrawRect.width() / mConfig.mChatHelper.xCoordinateLength;

        final int size = mConfig.mChatHelper.xCoordinateLength;
        final float contentHeight = mDrawRect.height() - textBounds.height();

        List<InternalChatInfo> list = mConfig.mChatHelper.getChatLists();
        if (ToolUtil.isListEmpty(list)) return;
        for (int i = 0; i < size - 1; i++) {
            for (InternalChatInfo info : list) {
                Path p = info.linePath;
                LineChatInfoWrapper curInfoWrapper = info.getInfo(i);
                LineChatInfoWrapper nextInfoWrapper = info.getInfo(i + 1);

                if (curInfoWrapper != null && nextInfoWrapper != null) {

                    //当前线段
                    float startPointX = 0;
                    float startPointY = 0;
                    //下一条线段
                    float endPointX = 0;
                    float endPointY = 0;

                    //前控制点
                    float preControlX = 0;
                    float preControlY = 0;
                    //后控制点
                    float afterControlX = 0;
                    float afterControlY = 0;

                    startPointY = (float) (contentHeight * (1 - curInfoWrapper.yPercent));
                    if (i == 0) {
                        p.reset();
                        startPointX = mDrawRect.left + textBounds.width();
                        preControlX = startPointX;
                        p.moveTo(startPointX, startPointY);
                    } else {
                        startPointX = mDrawRect.left + textBounds.width() + xFreq * i;
                    }

                    endPointX = startPointX + xFreq;
                    endPointY = (float) (contentHeight * (1 - nextInfoWrapper.yPercent));

                    preControlX = preControlX == 0 ? (startPointX + endPointX) / 2 : preControlX;
                    preControlY = startPointY;

                    afterControlX = endPointX;
                    afterControlY = endPointY;

                    Log.i(TAG, "drawLineChat: y  >>  " + endPointY + "   contentHeight  >>  " + contentHeight + "  percent  >>  " + curInfoWrapper.yPercent + "%");

//                    p.lineTo(endPointX, endPointY);
                    p.cubicTo(preControlX, preControlY, afterControlX, afterControlY, endPointX, endPointY);
                    canvas.drawPath(p, info.linePaint);

                    canvas.drawCircle(preControlX, preControlY, 4, prePointP);
                    canvas.drawCircle(endPointX, endPointY, 8, curPointP);
                    canvas.drawCircle(afterControlX, afterControlY, 4, afterPointP);

                    canvas.drawLine(preControlX, preControlY, endPointX, endPointY, mConfig.coordinateLinePaint);
                    canvas.drawLine(afterControlX, afterControlY, endPointX, endPointY, mConfig.coordinateLinePaint);
                }
            }
        }

    }


    public void start() {
        start(null);
    }

    public void start(LineChatPrepareConfig prepareConfig) {
        if (isInAnimating) return;
        isInAnimating = true;
        mConfig.mChatHelper.prepare(prepareConfig);
        postInvalidate();
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
