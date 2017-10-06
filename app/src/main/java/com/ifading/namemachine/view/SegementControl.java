package com.ifading.namemachine.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ifading.namemachine.R;


/**
 * Created by yangjingsheng on 17/9/30.
 */

public class SegementControl extends View {
    private final static int BACKGROUN_LINE_MARGIN = 10;
    private final static int BACKGROUN_LINE_WIDTH = 3;

    private final static int BODOR_ANGLE_STYTLE_SMALL = 0;
    private final static int BODOR_ANGLE_STYTLE_NRMAL = 1;
    private final static int BODOR_ANGLE_STYTLE_BIG = 2;

    private final static int BODOR_ANGLE_SMALL_VALUE = 10;
    private final static int BODOR_ANGLE_NRMAL_VALUE = 20;
    private final static int BODOR_ANGLE_BIG_VALUE = 30;


    private final static int SELECT_PART_LEFT = 0;
    private final static int SELECT_PART_MID = 1;
    private final static int SELECT_PART_RIGHT = 2;


    private final static int MAX_ITEM_TWO = 1;
    private final static int MAX_ITEM_THREE = 2;


    private static final String TAG = "SegementControl";
    private final RectF mMidRectF;
    private final RectF mLeftRectF;
    private int mTextSize = 35;

    private int mWidth;
    private int mHeight;
    private RectF mBgRectF;
    private Paint mPaint;
    private String mLeftText;
    private String mRightText;
    private String mMidText;
    private int mSelectPart;
    private int mMaxItem;
    private String mSelectBgColor;
    private String mUnselectBgColor;
    private String mBoderColor;
    private String mSelectTextColor;
    private String mUnselectTextColor;
    private RectF mRightRectF;
    private int mBoderAngle;
    private Rect mLeftTextBounds;
    private Rect mMidTextbounds;
    private Rect mRightTextbounds;
    private OnSelectPartChangeListener mOnSelectPartChangeListener;

    public SegementControl(Context context) {
        this(context, null);
    }

    public SegementControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SegementControl);
        // 取出左边text内容
        mLeftText = ta.getString(R.styleable.SegementControl_left_text);
        // 取出右边text内容
        mRightText = ta.getString(R.styleable.SegementControl_right_text);
        // 取出右边text内容
        mMidText = ta.getString(R.styleable.SegementControl_mid_text);
        // 取出选中状态
        mSelectPart = ta.getInt(R.styleable.SegementControl_default_select, 0);
        //最大item数
        mMaxItem = ta.getInt(R.styleable.SegementControl_max_item, 1);

        mSelectBgColor = ta.getString(R.styleable.SegementControl_select_backgroud_color);
        mUnselectBgColor = ta.getString(R.styleable.SegementControl_unselect_backgroud_color);
        mBoderColor = ta.getString(R.styleable.SegementControl_boder_color);
        mSelectTextColor = ta.getString(R.styleable.SegementControl_select_text_color);
        mUnselectTextColor = ta.getString(R.styleable.SegementControl_unselect_text_color);

        Log.d(TAG, "mSelectBgColor:" + mSelectBgColor + " mSelectTextColor:" + mSelectTextColor + " mSelectPart:" + mSelectPart);

        mBoderAngle = ta.getInt(R.styleable.SegementControl_boder_angle, 1);

        mBgRectF = new RectF();
        mLeftRectF = new RectF();
        mRightRectF = new RectF();
        mMidRectF = new RectF();

        mLeftTextBounds = new Rect();
        mRightTextbounds = new Rect();
        mMidTextbounds = new Rect();
        mRightTextbounds = new Rect();


        ta.recycle();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(BACKGROUN_LINE_WIDTH);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.d(TAG, "onMeasure,mWidth:" + mWidth + " mHeight:" + mHeight);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeight == 0 || mWidth == 0) {
            return;
        }
        drawView(canvas);
    }

    private void drawView(Canvas canvas) {
        int rectAngle = getRectAngle();
        drawBackground(canvas, rectAngle);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        if (mMaxItem == 1) {
            mPaint.getTextBounds(mLeftText, 0, mLeftText.length(), mLeftTextBounds);
            mPaint.getTextBounds(mRightText, 0, mLeftText.length(), mRightTextbounds);

            mPaint.setColor(Color.parseColor(mSelectPart == SELECT_PART_LEFT ? mSelectTextColor : mUnselectTextColor));
            canvas.drawText(mLeftText, (getMeasuredWidth() - BACKGROUN_LINE_MARGIN * 2) / 4 + BACKGROUN_LINE_MARGIN - mLeftTextBounds.width() / 2 - BACKGROUN_LINE_MARGIN / 2, getMeasuredHeight() / 2 + mLeftTextBounds.height() / 2 - BACKGROUN_LINE_MARGIN / 2, mPaint);
            mPaint.setColor(Color.parseColor(mSelectPart == SELECT_PART_RIGHT ? mSelectTextColor : mUnselectTextColor));
            canvas.drawText(mRightText, (getMeasuredWidth() - BACKGROUN_LINE_MARGIN * 2) / 4f * 3 + BACKGROUN_LINE_MARGIN - mRightTextbounds.width() / 2 - BACKGROUN_LINE_MARGIN / 2, getMeasuredHeight() / 2 + mRightTextbounds.height() / 2 - BACKGROUN_LINE_MARGIN / 2, mPaint);
        } else {
            mPaint.setColor(Color.parseColor(mSelectPart == SELECT_PART_LEFT ? mSelectTextColor : mUnselectTextColor));
            mPaint.getTextBounds(mLeftText, 0, mLeftText.length(), mLeftTextBounds);
            canvas.drawText(mLeftText,
                    BACKGROUN_LINE_WIDTH + (getMeasuredWidth() - BACKGROUN_LINE_MARGIN * 2) / 6f + BACKGROUN_LINE_MARGIN - mLeftTextBounds.width() / 2 - BACKGROUN_LINE_MARGIN / 2,
                    getMeasuredHeight() / 2 + mLeftTextBounds.height() / 2 - BACKGROUN_LINE_MARGIN / 2, mPaint);
            mPaint.setColor(Color.parseColor(mSelectPart == SELECT_PART_MID ? mSelectTextColor : mUnselectTextColor));
            mPaint.getTextBounds(mMidText, 0, mLeftText.length(), mMidTextbounds);
            canvas.drawText(mMidText, (getMeasuredWidth() - BACKGROUN_LINE_MARGIN * 2) / 6f * 3 + BACKGROUN_LINE_MARGIN - mMidTextbounds.width() / 2 - BACKGROUN_LINE_MARGIN / 2, getMeasuredHeight() / 2 + mMidTextbounds.height() / 2 - BACKGROUN_LINE_MARGIN / 2, mPaint);

            mPaint.setColor(Color.parseColor(mSelectPart == SELECT_PART_RIGHT ? mSelectTextColor : mUnselectTextColor));
            mPaint.getTextBounds(mRightText, 0, mLeftText.length(), mRightTextbounds);
            canvas.drawText(mRightText, (getMeasuredWidth() - BACKGROUN_LINE_MARGIN * 2) / 6f * 5 + BACKGROUN_LINE_MARGIN - mRightTextbounds.width() / 2 - BACKGROUN_LINE_MARGIN / 2, getMeasuredHeight() / 2 + mRightTextbounds.height() / 2 - BACKGROUN_LINE_MARGIN / 2, mPaint);
        }
    }


    /**
     * draw backgroup border
     *
     * @param canvas    canvas
     * @param rectAngle the border rect angle
     */
    private void drawBackground(Canvas canvas, int rectAngle) {
        Log.d(TAG, "drawBackground");
        mBgRectF.set(BACKGROUN_LINE_MARGIN, BACKGROUN_LINE_MARGIN, mWidth - BACKGROUN_LINE_MARGIN, mHeight - BACKGROUN_LINE_MARGIN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(mBoderColor));

        if (mMaxItem == MAX_ITEM_TWO) {
            int mid = mWidth / 2;
            canvas.drawLine(mid, BACKGROUN_LINE_MARGIN, mid, mHeight - BACKGROUN_LINE_MARGIN, mPaint);
            canvas.drawRoundRect(mBgRectF, rectAngle, rectAngle, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            if (mSelectPart == SELECT_PART_LEFT) {
                mLeftRectF.set(BACKGROUN_LINE_MARGIN, BACKGROUN_LINE_MARGIN, mid, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRoundRect(mLeftRectF, rectAngle, rectAngle, mPaint);
                mLeftRectF.set(mid / 2, BACKGROUN_LINE_MARGIN, mid, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRect(mLeftRectF, mPaint);
            } else if (mSelectPart == SELECT_PART_RIGHT) {
                mRightRectF.set(BACKGROUN_LINE_MARGIN + mid, BACKGROUN_LINE_MARGIN, mWidth - BACKGROUN_LINE_MARGIN, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRoundRect(mRightRectF, rectAngle, rectAngle, mPaint);
                mRightRectF.set(mid, BACKGROUN_LINE_MARGIN, mid + mid / 2, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRect(mRightRectF, mPaint);
            }
        } else {
            int firstLine = (int) ((mWidth - BACKGROUN_LINE_MARGIN * 2) / 3f + BACKGROUN_LINE_MARGIN + 0.5f);
            int secondLine = (int) ((mWidth - BACKGROUN_LINE_MARGIN * 2) / 3f * 2 + BACKGROUN_LINE_MARGIN + 0.5f);
            canvas.drawLine(firstLine, BACKGROUN_LINE_MARGIN, firstLine, mHeight - BACKGROUN_LINE_MARGIN, mPaint);
            canvas.drawLine(secondLine, BACKGROUN_LINE_MARGIN, secondLine, mHeight - BACKGROUN_LINE_MARGIN, mPaint);
            canvas.drawRoundRect(mBgRectF, rectAngle, rectAngle, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(mSelectBgColor));

            if (mSelectPart == SELECT_PART_LEFT) {
                mLeftRectF.set(BACKGROUN_LINE_MARGIN, BACKGROUN_LINE_MARGIN, firstLine, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRoundRect(mLeftRectF, rectAngle, rectAngle, mPaint);
                mLeftRectF.set(firstLine / 2, BACKGROUN_LINE_MARGIN, firstLine, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRect(mLeftRectF, mPaint);
            } else if (mSelectPart == SELECT_PART_MID) {
                mMidRectF.set(firstLine, BACKGROUN_LINE_MARGIN, secondLine, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRect(mMidRectF, mPaint);
            } else if (mSelectPart == SELECT_PART_RIGHT) {
                mRightRectF.set(BACKGROUN_LINE_MARGIN + secondLine, BACKGROUN_LINE_MARGIN, mWidth - BACKGROUN_LINE_MARGIN, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRoundRect(mRightRectF, rectAngle, rectAngle, mPaint);
                mRightRectF.set(secondLine, BACKGROUN_LINE_MARGIN, secondLine + (secondLine - firstLine) / 2, mHeight - BACKGROUN_LINE_MARGIN);
                canvas.drawRect(mRightRectF, mPaint);
            }
        }
    }

    /**
     * get the given backgroup boder RectAngle
     *
     * @return RectAngle
     */
    private int getRectAngle() {
        int boderAngle;
        switch (mBoderAngle) {
            case BODOR_ANGLE_STYTLE_SMALL:
                boderAngle = BODOR_ANGLE_SMALL_VALUE;
                break;
            case BODOR_ANGLE_STYTLE_NRMAL:
                boderAngle = BODOR_ANGLE_NRMAL_VALUE;
                break;
            case BODOR_ANGLE_STYTLE_BIG:
                boderAngle = BODOR_ANGLE_BIG_VALUE;
                break;
            default:
                boderAngle = BODOR_ANGLE_NRMAL_VALUE;
                break;
        }

        return boderAngle;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                processTouch(x);
                break;
        }
        return true;
    }

    private void processTouch(float x) {
        if (mMaxItem == MAX_ITEM_TWO) {
            int mid = mWidth / 2;
            mSelectPart = x < mid ? SELECT_PART_LEFT : SELECT_PART_RIGHT;
        } else {
            int firstLine = (int) ((mWidth - BACKGROUN_LINE_MARGIN * 2) / 3f + BACKGROUN_LINE_MARGIN + 0.5f);
            int secondLine = (int) ((mWidth - BACKGROUN_LINE_MARGIN * 2) / 3f * 2 + BACKGROUN_LINE_MARGIN + 0.5f);
            if (x < firstLine) {
                mSelectPart = SELECT_PART_LEFT;
            } else if (x >= firstLine && x < secondLine) {
                mSelectPart = SELECT_PART_MID;
            } else if (x >= secondLine) {
                mSelectPart = SELECT_PART_RIGHT;
            }
        }
        invalidate();
        if (mOnSelectPartChangeListener!=null){
            mOnSelectPartChangeListener.onSelectPartChange(mSelectPart);
        }
    }


    public interface OnSelectPartChangeListener {

        void onSelectPartChange(int selectPart);
    }

    public void setOnSelectPartChangeListener(OnSelectPartChangeListener listener){
        this.mOnSelectPartChangeListener = listener;
    }
}
