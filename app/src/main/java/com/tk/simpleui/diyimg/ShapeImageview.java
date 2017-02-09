package com.tk.simpleui.diyimg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by TK on 2016/11/9.
 */

public abstract class ShapeImageview extends ImageView {
    private Paint mPaint;
    private Path mPath;
    private BitmapShader mShader;
    private Bitmap mBitmap;

    public ShapeImageview(Context context) {
        super(context);
        super.setScaleType(CENTER_CROP);
        initPaint();
    }

    public ShapeImageview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeImageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //// TODO: 2016/11/9 几乎只用这个，别的自行适配哦⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄
        super.setScaleType(CENTER_CROP);
        initPaint();
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(CENTER_CROP);
    }


    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        refreshUI();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        refreshUI();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        refreshUI();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        refreshUI();
    }

    /**
     * 更新UI并重新绘制
     */
    private void refreshUI() {
        initPaint();
        refreshBitmap();
        mPath = new Path();
        //子类实现
        generatePath(mPath);
        invalidate();
    }

    /**
     * 刷新Bitmap
     */
    private void refreshBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null || (getWidth() == 0 && getHeight() == 0)) {
            mBitmap = null;
            mShader = null;
            mPaint.setShader(null);
            return;
        }
        mBitmap = drawableToBitmap(drawable);
        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);
        refreshMatrix();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.WHITE);
        }
    }


    /**
     * 刷新矩阵
     *
     * @param
     * @return
     */
    private void refreshMatrix() {
        Matrix matrix = new Matrix();
        int bitmapW = mBitmap.getWidth();
        int bitmapH = mBitmap.getHeight();

        int mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float scale;
        float dx = 0, dy = 0;

        if (bitmapW * mHeight > mWidth * bitmapH) {
            scale = (float) mHeight / (float) bitmapH;
            dx = (mWidth - bitmapW * scale) * 0.5f;
        } else {
            scale = (float) mWidth / (float) bitmapW;
            dy = (mHeight - bitmapH * scale) * 0.5f;
        }
        matrix.setScale(scale, scale);
        matrix.postTranslate(Math.round(dx), Math.round(dy));
        mShader.setLocalMatrix(matrix);
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap;
        if (drawable instanceof ColorDrawable) {
            bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mPath.isEmpty()) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        refreshUI();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        refreshUI();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshUI();
    }

    public abstract void generatePath(Path path);
}
