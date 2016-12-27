package com.tk.simpleui.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allhatch.ehatch.R;
import com.allhatch.ehatch.util.DensityUtil;


/**
 * Created by TK on 2016/2/27.
 * 自定义 效果ratingbar
 * setrating 支持float半颗星高亮
 * starChecked 支持点击选择
 */
public class TRatingbar extends LinearLayout {
    private static final int STAR_SUM = 5;
    private static final int STAR_COUNT = 0;
    //默认5dp间距
    private static final int DEFAULT_PADDING = 5;
    public static final int DEFAULT_EMPTY = R.drawable.star_off;
    public static final int DEFAULT_FILL = R.drawable.star_on;

    private int starImageSize;
    private int starSum;
    private float starCount;
    private int starPadding;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private boolean starChecked = false;
    private OnRatingChangeListener onRatingChangeListener;


    public TRatingbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TRatingbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRatingBar);
        //默认大小wrap_content
        starImageSize = (int) ta.getDimension(R.styleable.MyRatingBar_starImageSize, ViewGroup.LayoutParams.WRAP_CONTENT);
        //默认亮的星
        starCount = ta.getFloat(R.styleable.MyRatingBar_starCount, STAR_COUNT);
        //总共星
        starSum = ta.getInteger(R.styleable.MyRatingBar_starSum, STAR_SUM);
        //间距
        starPadding = (int) ta.getDimension(R.styleable.MyRatingBar_starPadding, DensityUtil.dp2px(context, DEFAULT_PADDING));
        //不亮的drawable
        starEmptyDrawable = ta.getDrawable(R.styleable.MyRatingBar_starEmpty);
        //亮的drawable
        starFillDrawable = ta.getDrawable(R.styleable.MyRatingBar_starFill);
        //是否允许点击评分
        starChecked = ta.getBoolean(R.styleable.MyRatingBar_starClickable, false);
        //默认值
        starEmptyDrawable = starEmptyDrawable == null ? getResources().getDrawable(DEFAULT_EMPTY) : starEmptyDrawable;
        starFillDrawable = starFillDrawable == null ? getResources().getDrawable(DEFAULT_FILL) : starFillDrawable;
        ta.recycle();

        for (int i = 0; i < starSum; i++) {
            ImageView imageView = getStarImageView(context, i);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (starChecked) {
                        int mStarCount = indexOfChild(v) + 1;
                        setRating(mStarCount);
                        if (onRatingChangeListener != null) {
                            onRatingChangeListener.onRating(mStarCount);
                        }
                    }
                }
            });
            addView(imageView);
        }
    }


    private ImageView getStarImageView(Context context, int i) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        LayoutParams para = new LayoutParams(starImageSize, starImageSize);
        para.setMargins(Math.round(starPadding >> 1), 0, Math.round(starPadding >> 1), 0);
        imageView.setLayoutParams(para);
        if (i < starCount) {
            if (starCount - i < 1) {
                //小数
                float s = starCount - i;
                setHalfStar(imageView, s);
            } else {
                imageView.setImageDrawable(starFillDrawable);
            }
        } else {
            imageView.setImageDrawable(starEmptyDrawable);

        }
        return imageView;
    }

    /**
     * 设置几颗星
     *
     * @param starCount
     */
    public void setRating(float starCount) {
        starCount = starCount > this.starSum ? this.starSum : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        this.starCount = starCount;
        for (int i = 0; i < starCount; ++i) {
            if (starCount - i < 1) {
                //小数
                float s = starCount - i;
                setHalfStar((ImageView) getChildAt(i), s);
                return;
            }
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        for (int i = this.starSum - 1; i >= starCount; i--) {

            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    /**
     * 设置半星
     *
     * @param imagview
     * @param space    0-1
     */
    private void setHalfStar(ImageView imagview, float space) {
        if (((int) (starFillDrawable.getIntrinsicWidth() * space)) <= 0) {
            imagview.setImageDrawable(starEmptyDrawable);
            return;
        }
        if (space >= 1) {
            imagview.setImageDrawable(starFillDrawable);
            return;
        }
        Drawable[] drawables = {starEmptyDrawable,
                drawableToBitmap(starFillDrawable,
                        (int) (starFillDrawable.getIntrinsicWidth() * space),
                        starFillDrawable.getIntrinsicHeight())};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, 0, 0, (int) (starFillDrawable.getIntrinsicWidth() * (1.0 - space)), 0);
        imagview.setImageDrawable(layerDrawable);
    }

    /**
     * 裁剪
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    private BitmapDrawable drawableToBitmap(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return new BitmapDrawable(null, bitmap);
    }

    public interface OnRatingChangeListener {
        void onRating(int ratingScore);
    }

    public void setOnRatingListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public float getStarCount() {
        return starCount;
    }
}
