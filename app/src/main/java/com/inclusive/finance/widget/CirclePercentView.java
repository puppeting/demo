package com.inclusive.finance.widget;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.inclusive.finance.R;


/**
 */
public class CirclePercentView extends View {
    public static final int WIDTH_RADIUS_RATIO = 8;
    private Paint mPaint,mTextPaint;
    private float progressPercent;
    private int mRadius;
    private RectF mRectF;
    private int mBgColor, mProgressColor;
    private int mStartColor, mEndColor;
    private LinearGradient mGradient;
    private boolean isGradient;
    private int yOffset;
    public CirclePercentView(Context context) {
        super(context);
        init(context);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView);
        mBgColor = typedArray.getColor(R.styleable.CirclePercentView_circle_BgColor, ContextCompat.getColor(context, R.color.color_main_orangeAlpha));
        mProgressColor = typedArray.getColor(R.styleable.CirclePercentView_circle_ProgressColor, ContextCompat.getColor(context,R.color.color_main_orange));
        mRadius = typedArray.getInt(R.styleable.CirclePercentView_circle_Radius, WIDTH_RADIUS_RATIO);
        isGradient = typedArray.getBoolean(R.styleable.CirclePercentView_circle_IsGradient, false);
        mStartColor = typedArray.getColor(R.styleable.CirclePercentView_circle_StartColor, ContextCompat.getColor(context,R.color.color_main_orange));
        mEndColor = typedArray.getColor(R.styleable.CirclePercentView_circle_EndColor, ContextCompat.getColor(context,R.color.color_main_orange));
        typedArray.recycle();
        init(context);
    }

    public CirclePercentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mTextPaint = new Paint();
        mTextPaint.setColor(mProgressColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DensityUtil.dip2px(context,16));
        measureText();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGradient = new LinearGradient(getWidth(), 0, getWidth(), getHeight(), mStartColor, mEndColor, Shader.TileMode.MIRROR);
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1、绘制背景灰色圆环
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int strokeWidth = centerX / mRadius;
        mPaint.setStrokeWidth(strokeWidth);
        //必须设置为null，否则背景也会加上渐变色
        mPaint.setShader(null);
        mPaint.setColor(mBgColor);
        canvas.drawCircle(centerX, centerX, centerX - strokeWidth / 2, mPaint);
        // 2、绘制比例弧
        if (mRectF == null) {
            mRectF = new RectF(strokeWidth / 2, strokeWidth / 2, 2 * centerX - strokeWidth / 2, 2 * centerX - strokeWidth / 2);
        }
        //3、是否绘制渐变色
        if (isGradient) {
            //设置线性渐变
            mPaint.setShader(mGradient);
        } else {
            mPaint.setColor(mProgressColor);
        }
        //画比例圆弧
        canvas.drawArc(mRectF, -90, 3.6f * progressPercent, false, mPaint);
        //绘制中间的字
        String text = progressPercent + "%";
        canvas.drawText(text,centerX,centerY + yOffset/2,mTextPaint);
    }

    @Keep
    public void setPercentage(float percentage) {
        this.progressPercent = percentage;
        measureText();
        invalidate();
    }
    private void measureText(){
        Rect bound = new Rect();
        String text = progressPercent + "%";
        mTextPaint.getTextBounds(text,0,text.length(),bound);
        yOffset = bound.bottom - bound.top;
    }
    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public void setStartColor(int mStartColor) {
        this.mStartColor = mStartColor;
    }

    public void setEndColor(int mEndColor) {
        this.mEndColor = mEndColor;
    }

    public void setGradient(boolean mGradient) {
        isGradient = mGradient;
    }
}
