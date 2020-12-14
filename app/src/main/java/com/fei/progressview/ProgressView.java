package com.fei.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @ClassName: ProgressView
 * @Description: 自定义进度条
 * @Author: Fei
 * @CreateDate: 2020-12-13 21:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-13 21:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ProgressView extends View {

    /**
     * 属性值
     */
    private int mOriginalColor = Color.BLUE;
    private int mRunningColor = Color.YELLOW;
    private float mProgressTextSize = 20f;
    private int mProgressTextColor = Color.BLUE;
    private float mProgressWidth = 10f;//圆环宽度

    /**
     * 画笔
     */
    private Paint mOriginalPaint;
    private Paint mRunningPaint;
    private Paint mTextPaint;

    private int width = 0;
    private int height = 0;
    private int center = 0;
    private int halfProgressWidth = 0;

    private int mMax = 100;
    private float mProgress = 20;//进度
    private RectF mRectF;//圆弧区域
    private Rect bounds;//文本区域

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mOriginalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOriginalPaint.setColor(mOriginalColor);
        mOriginalPaint.setStrokeWidth(mProgressWidth);
        mOriginalPaint.setStyle(Paint.Style.STROKE);

        mRunningPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRunningPaint.setColor(mRunningColor);
        mRunningPaint.setStrokeWidth(mProgressWidth);
        mRunningPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);

        mRectF = new RectF();
        bounds = new Rect();
    }

    /**
     * 获取属性值
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mOriginalColor = typedArray.getColor(R.styleable.ProgressView_originalColor, mOriginalColor);
        mRunningColor = typedArray.getColor(R.styleable.ProgressView_runningColor, mRunningColor);
        mProgressWidth = typedArray.getDimension(R.styleable.ProgressView_progressWidth, dp2px(mProgressWidth));
        mProgressTextColor = typedArray.getColor(R.styleable.ProgressView_progressTextColor, mProgressTextColor);
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.ProgressView_progressTextSize, sp2px(mProgressTextSize));
        typedArray.recycle();

        halfProgressWidth = (int) (mProgressWidth / 2);
    }

    /**
     * dp转px
     */
    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int min = Math.min(width, height);

        this.width = min;
        this.height = min;
        center = min / 2;

        setMeasuredDimension(min, min);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画圆
        canvas.drawCircle(center, center, center - halfProgressWidth, mOriginalPaint);

        //画弧
        float angle = mProgress / mMax;
        int endAngle = (int) (angle * 360);
        mRectF.set(halfProgressWidth, halfProgressWidth, width - halfProgressWidth, height - halfProgressWidth
        );
        canvas.drawArc(mRectF, 0, endAngle, false, mRunningPaint);

        //画文本

        String text = (int) (angle * 100) + "%";
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = width / 2 - bounds.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int y = height / 2 + dy;
        canvas.drawText(text, x, y, mTextPaint);
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }
}
