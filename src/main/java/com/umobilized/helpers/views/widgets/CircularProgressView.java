package com.umobilized.helpers.views.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.umobilized.helpers.R;


/**
 * Custom view for showing circular progress indicator. It is based on ImageView and is showing
 * circular fragment of bitmap provided in 'progress_src' attribute.
 *
 * Created by tpaczesny on 2016-12-09.
 */

public class CircularProgressView extends android.support.v7.widget.AppCompatImageView {
    private Paint mPaint;
    private RectF mBounds;
    private long mMaxProgress;
    private float mProgress;

    public CircularProgressView(Context context) {
        super(context);
        init(null);
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int progressResId = R.drawable.default_progress;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressView, 0, 0);
            try {
                progressResId = ta.getResourceId(R.styleable.CircularProgressView_progressSrc, R.drawable.default_progress);
            } finally {
                ta.recycle();
            }
        }

        mMaxProgress = 100;
        mProgress = 0;

        mPaint = new Paint();
        Bitmap progress = BitmapFactory.decodeResource(getResources(), progressResId);
        mPaint.setShader(new BitmapShader(progress, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        mPaint.setAntiAlias(true);

        mBounds = new RectF();
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    public float getProgress() {
        return mProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBounds.set(0,0,getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = mProgress * 360f;

        canvas.drawArc(mBounds, -90, angle, true, mPaint);
    }


}