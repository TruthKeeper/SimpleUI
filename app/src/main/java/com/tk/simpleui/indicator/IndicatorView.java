package com.tk.simpleui.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tk.simpleui.Utils;
import com.tk.simpleui.common.DensityUtil;


/**
 * Created by TK on 2017/2/6.
 * 数字指示器
 */

public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";

    private Paint mPaint = new Paint();
    //Build
    private int backgroundColor;
    private int radius;
    private float textSize;
    private int textColor;
    private int num;
    private boolean upper;
    private boolean point;
    private float textHeight;

    private RectF content;

    public static Builder with() {
        return new Builder();
    }

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void initBuilder(Builder builder) {
        backgroundColor = builder.backgroundColor;
        textColor = builder.textColor;
        textSize = builder.textSize;
        num = builder.num;
        upper = builder.upper;
        point = builder.point;
        radius = builder.radius;

        mPaint.setTextSize(DensityUtil.sp2px(  textSize));

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        textHeight = fontMetrics.bottom + fontMetrics.top;
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num <= 0) {
            return;
        }
        mPaint.setColor(backgroundColor);
        if (point) {
            canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, getHeight() >> 1, mPaint);
        } else {
            canvas.drawRoundRect(content, radius, radius, mPaint);

            String numStr = convertText(num, upper);
            if (!TextUtils.isEmpty(numStr)) {
                mPaint.setColor(textColor);
                canvas.drawText(numStr, getWidth() / 2F, (getHeight() - textHeight) / 2F, mPaint);
            }
        }
    }

    private static String convertText(int num, boolean upper) {
        if (num <= 0) {
            return "";
        }
        if (upper && num > 99) {
            return "99+";
        }
        return Integer.toString(num);
    }

    /**
     * 绑定
     *
     * @param builder
     * @param target
     * @return
     */
    private static IndicatorView bind(@NonNull Builder builder, @NonNull View target) {
        if (target.getParent() == null) {
            Log.e(TAG, "target need parent!");
            return null;
        }
        Paint paint = new Paint();
        paint.setTextSize(DensityUtil.sp2px(builder.textSize));
        int width = Math.round(paint.measureText(convertText(builder.num, builder.upper)));
        IndicatorView indicatorView;
        if (target.getParent() instanceof IndicatorFrameLayout) {
            //已经偷梁换柱
            indicatorView = (IndicatorView) ((IndicatorFrameLayout) target.getParent()).getChildAt(((IndicatorFrameLayout) target.getParent()).getChildCount() - 1);
            FrameLayout.LayoutParams indicatorP = new FrameLayout.LayoutParams(
                    builder.point ? builder.size : Math.max(width + 2 * builder.padding, builder.size),
                    builder.size);

            indicatorP.leftMargin = builder.leftMargin;
            indicatorP.topMargin = builder.topMargin;
            indicatorP.rightMargin = builder.rightMargin;
            indicatorP.bottomMargin = builder.bottomMargin;
            indicatorP.gravity = builder.gravity;
            indicatorView.initBuilder(builder);
            indicatorView.setLayoutParams(indicatorP);
            indicatorView.invalidate();
        } else {
            indicatorView = new IndicatorView(target.getContext());
            ViewGroup viewGroup = (ViewGroup) target.getParent();
            ViewGroup.LayoutParams targetP = target.getLayoutParams();
            FrameLayout.LayoutParams indicatorP = new FrameLayout.LayoutParams(
                    builder.point ? builder.size : Math.max(width + 2 * builder.padding, builder.size),
                    builder.size);

            indicatorP.leftMargin = builder.leftMargin;
            indicatorP.topMargin = builder.topMargin;
            indicatorP.rightMargin = builder.rightMargin;
            indicatorP.bottomMargin = builder.bottomMargin;
            indicatorP.gravity = builder.gravity;
            indicatorView.initBuilder(builder);

            IndicatorFrameLayout parent = new IndicatorFrameLayout(target.getContext());
            int index = viewGroup.indexOfChild(target);
            viewGroup.removeView(target);
            parent.addView(target, targetP);
            parent.addView(indicatorView, indicatorP);
            viewGroup.addView(parent, index, targetP);
        }
        return indicatorView;
    }

    /**
     * 解绑
     *
     * @return
     */
    public boolean unbind() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        content = new RectF(0, 0, w, h);
    }

    public static final class Builder {
        private int size = DensityUtil.dp2px(  6);
        private int leftMargin = 0;
        private int topMargin = 0;
        private int bottomMargin = 0;
        private int rightMargin = 0;
        private int backgroundColor = Color.RED;
        /**
         * 圆角矩形时的圆角
         */
        private int radius = 0;
        /**
         * 圆角矩形时的左、右间距
         */
        private int padding = DensityUtil.dp2px(  2);
        private float textSize = 12F;
        private int textColor = Color.WHITE;
        private int num = 0;
        private int gravity = Gravity.END | Gravity.TOP;
        private boolean upper = true;
        /**
         * true：小圆点 false：圆角矩形(显示数字)
         */
        private boolean point = false;


        /**
         * 设置大小，设置成高度，宽度自动调节
         *
         * @param size
         * @return
         */
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder leftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
            return this;
        }

        public Builder topMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public Builder rightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
            return this;
        }

        /**
         * 相对位置
         *
         * @param gravity
         * @return
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 数字
         *
         * @param num
         * @return
         */
        public Builder num(int num) {
            this.num = num;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * 字体大小
         *
         * @param textSize 单位 : sp
         * @return
         */
        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder bottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        /**
         * 超过99是否显示99+
         *
         * @param upper
         * @return
         */
        public Builder upper(boolean upper) {
            this.upper = upper;
            return this;
        }

        /**
         * 设置形状
         *
         * @param point
         * @return
         */
        public Builder point(boolean point) {
            this.point = point;
            return this;
        }

        /**
         * 圆角矩形时的圆角
         *
         * @param radius
         * @return
         */
        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 圆角矩形时的左、右间距
         *
         * @param padding
         * @return
         */
        public Builder padding(int padding) {
            this.padding = padding;
            return this;
        }

        /**
         * 绑定
         *
         * @param view
         * @return
         */
        public void bind(@NonNull View view) {
            if (!point) {
                radius = Math.min(size >> 1, radius);
            }
            IndicatorView.bind(this, view);
        }
    }

    /**
     * 偷梁换柱的容器
     */
    public static class IndicatorFrameLayout extends FrameLayout {
        public IndicatorFrameLayout(Context context) {
            super(context);
        }
    }
}
