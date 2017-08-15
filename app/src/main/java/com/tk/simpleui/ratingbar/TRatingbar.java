package com.tk.simpleui.ratingbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;


/**
 * Created by TK on 2016/12/27.
 * 可以调节间距
 * setrating 支持float半颗星高亮
 * starChecked 支持点击选择
 */
public class TRatingbar extends View {
    private static final PorterDuffXfermode MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
    private static final int STAR_SUM = 5;
    private static final int STAR_RATING = 0;
    /**
     * 默认5dp间距
     */
    private static final int DEFAULT_PADDING = 5;

    private int mStarSize;
    private int mStarSum;
    private float mStarRating;
    private int mStarPadding;
    private Drawable mStarEmptyDrawable;
    private Drawable mStarFillDrawable;
    private boolean mStarChecked = false;

    private Bitmap mStarEmptyBitmap;
    private Bitmap mStarFillBitmap;
    private Paint mPaint = new Paint();

    //    private float mLastX;
//    private float mLastY;
    private OnRatingChangeListener onRatingChangeListener;

    public TRatingbar(Context context) {
        super(context);
        init();
    }

    public TRatingbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TintTypedArray array = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.TRatingbar);
            //默认大小wrap_content
            mStarSize = array.getDimensionPixelOffset(R.styleable.TRatingbar_starImageSize, ViewGroup.LayoutParams.WRAP_CONTENT);
            mStarRating = array.getFloat(R.styleable.TRatingbar_starRating, STAR_RATING);
            mStarSum = array.getInteger(R.styleable.TRatingbar_starSum, STAR_SUM);
            mStarPadding = array.getDimensionPixelOffset(R.styleable.TRatingbar_starPadding, DensityUtil.dp2px(DEFAULT_PADDING));
            mStarEmptyDrawable = array.getDrawable(R.styleable.TRatingbar_starEmpty);
            mStarFillDrawable = array.getDrawable(R.styleable.TRatingbar_starFill);
            //是否允许点击评分
            mStarChecked = array.getBoolean(R.styleable.TRatingbar_starClickable, false);

            if (mStarSum < 0) {
                mStarSum = 1;
            }
            if (mStarRating < 0) {
                mStarRating = 0;
            } else if (mStarRating > mStarSum) {
                mStarRating = mStarSum;
            }
            int s = getStarSize();
            if (mStarEmptyDrawable != null) {
                mStarEmptyBitmap = drawableToBitmap(mStarEmptyDrawable, s, s, s, s);
            }
            if (mStarFillDrawable != null) {
                mStarFillBitmap = drawableToBitmap(mStarFillDrawable, s, s, s, s);
            }
            array.recycle();
        }
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setXfermode(MODE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStarEmptyDrawable == null || mStarFillDrawable == null) {
            return;
        }
        Rect rect = new Rect();
        for (int i = 0; i < mStarSum; i++) {
            if (mStarRating > i) {

                if (i != mStarSum - 1 && mStarRating < i + 1) {
                    //半星
                    rect.top = getPaddingTop();
                    rect.bottom = mStarEmptyBitmap.getHeight() + rect.top;
                    rect.left = i * (mStarEmptyBitmap.getWidth() + mStarPadding);
                    rect.right = (i + 1) * mStarEmptyBitmap.getWidth() + i * mStarPadding;
                    canvas.drawBitmap(mStarEmptyBitmap, null, rect, mPaint);

                    int s = getStarSize();
                    Bitmap off = drawableToBitmap(mStarFillDrawable, (int) ((mStarRating - i) * s), s, s, s);
                    rect.top = getPaddingTop();
                    rect.bottom = mStarEmptyBitmap.getHeight() + rect.top;
                    rect.left = i * (mStarEmptyBitmap.getWidth() + mStarPadding);
                    rect.right = (int) (mStarRating * mStarEmptyBitmap.getWidth() + i * mStarPadding);
                    canvas.drawBitmap(off, null, rect, mPaint);
                } else {
                    //全星
                    rect.top = getPaddingTop();
                    rect.bottom = mStarEmptyBitmap.getHeight() + rect.top;
                    rect.left = i * (mStarEmptyBitmap.getWidth() + mStarPadding);
                    rect.right = (i + 1) * mStarEmptyBitmap.getWidth() + i * mStarPadding;
                    canvas.drawBitmap(mStarFillBitmap, null, rect, null);
                }
            } else {
                //0星
                rect.top = getPaddingTop();
                rect.bottom = mStarEmptyBitmap.getHeight() + rect.top;
                rect.left = i * (mStarEmptyBitmap.getWidth() + mStarPadding);
                rect.right = (i + 1) * mStarEmptyBitmap.getWidth() + i * mStarPadding;
                canvas.drawBitmap(mStarEmptyBitmap, null, rect, null);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int resultW = 0;
        for (int i = 0; i < mStarSum; i++) {
            resultW += getStarSize();
        }
        resultW += (mStarSum - 1) * mStarPadding;
        int resultH = getStarSize();
        setMeasuredDimension(modeW == MeasureSpec.EXACTLY ? sizeW : resultW + getPaddingLeft() + getPaddingRight(),
                modeH == MeasureSpec.EXACTLY ? sizeH : resultH + getPaddingTop() + getPaddingBottom());
    }


    /**
     * 设置几颗星
     *
     * @param rating
     */
    public void setRating(float rating) {
        if (rating != mStarRating && rating >= 0 && rating <= mStarSum) {
            mStarRating = rating;
            invalidate();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mLastX = event.getX();
//                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!mStarChecked) {
                    return false;
                }
                float x = event.getX();
                float y = event.getY();
                if (x < getPaddingLeft() || x > getWidth() || y < getPaddingTop() || y > getHeight()) {
                    return false;
                }
                float rating = (x - getPaddingLeft()) / (getStarSize() + mStarPadding);
                int r;
                if (rating * 10 % 10 != 0) {
                    r = (int) (rating + 1);
                } else {
                    r = (int) rating;
                }
                //不允许0星
                if (r == 0) {
                    r++;
                } else if (r > mStarSum) {
                    r = mStarSum;
                }
                setRating(r);
                if (onRatingChangeListener != null) {
                    onRatingChangeListener.onRating(r);
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 裁剪
     *
     * @param drawable
     * @param canvasW
     * @param canvasH
     * @param drawableW
     * @param drawableH
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable, int canvasW, int canvasH,
                                           int drawableW, int drawableH) {
        Bitmap bitmap = Bitmap.createBitmap(canvasW, canvasH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawableW, drawableH);
        drawable.draw(canvas);
        return bitmap;
    }

    private int getStarSize() {
        if (mStarEmptyDrawable == null) {
            return 0;
        }
        return mStarSize == ViewGroup.LayoutParams.WRAP_CONTENT ? mStarEmptyDrawable.getIntrinsicWidth() : mStarSize;
    }

    public interface OnRatingChangeListener {
        void onRating(int ratingScore);
    }

    public void setOnRatingListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public float getRating() {
        return mStarRating;
    }
}
