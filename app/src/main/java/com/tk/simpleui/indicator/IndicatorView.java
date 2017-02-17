package com.tk.simpleui.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tk.simpleui.common.DensityUtil;


/**
 * Created by TK on 2017/2/6.
 * 数字指示器
 */

public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";
    /**
     * 圆角4dp
     */
    public static final int ROUND = 4;
    /**
     * 字数过长增加4dp间距
     */
    public static final int PADDING = 4;

    public enum Shape {
        /**
         * 小点
         */
        POINT,
        /**
         * 圆，字数过长是变为椭圆
         */
        CIRCLE,
        /**
         * 矩形
         */
        RECT,
        /**
         * 圆角矩形
         */
        ROUND_RECT
    }

    private Paint mPaint = new Paint();
    //Build
    private int size = 0x20;
    private int leftMargin = 0;
    private int topMargin = 0;
    private int bottomMargin = 0;
    private int rightMargin = 0;
    private int backgroundColor = Color.RED;
    private float textSize = 12f;
    private int textColor = Color.WHITE;
    private int text = 0;
    private int gravity = Gravity.END | Gravity.TOP;
    private boolean plus = true;
    private Shape shape = Shape.POINT;
    private float textHeight;

    private RectF content;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private IndicatorView(Builder builder) {
        this(builder.context);
        size = builder.size;
        leftMargin = builder.leftMargin;
        topMargin = builder.topMargin;
        rightMargin = builder.rightMargin;
        bottomMargin = builder.bottomMargin;
        backgroundColor = builder.backgroundColor;
        textColor = builder.textColor;
        textSize = builder.textSize;
        text = builder.text;
        gravity = builder.gravity;
        plus = builder.plus;
        shape = builder.shape;
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), textSize));

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        textHeight = fontMetrics.bottom + fontMetrics.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (text <= 0) {
            return;
        }
        mPaint.setColor(backgroundColor);
        if (shape == Shape.POINT) {
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, mPaint);
        } else if (shape == Shape.CIRCLE) {

            canvas.drawOval(content, mPaint);

            String s = calculText(text);
            if (!TextUtils.isEmpty(s)) {
                mPaint.setColor(textColor);
                canvas.drawText(s, getWidth() / 2f, (getHeight() - textHeight) / 2f, mPaint);
            }
        } else if (shape == Shape.RECT) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

            String s = calculText(text);
            if (!TextUtils.isEmpty(s)) {
                mPaint.setColor(textColor);
                canvas.drawText(s, getWidth() / 2f, (getHeight() - textHeight) / 2f, mPaint);
            }
        } else if (shape == Shape.ROUND_RECT) {
            int r = DensityUtil.dp2px(getContext(), ROUND);
            canvas.drawRoundRect(content, r, r, mPaint);

            String s = calculText(text);
            if (!TextUtils.isEmpty(s)) {
                mPaint.setColor(textColor);
                canvas.drawText(s, getWidth() / 2f, (getHeight() - textHeight) / 2f, mPaint);
            }
        }

    }


    private String calculText(int text) {
        if (text <= 0) {
            return "";
        }
        if (plus && text > 99) {
            return "99+";
        }
        return Integer.toString(text);
    }

    /**
     * 绑定
     * 偷梁换柱，添加一层FrameLayout
     *
     * @param target
     */
    public IndicatorView bind(View target) {
        if (target.getParent() == null) {
            Log.e(TAG, "target need parent!");
            return null;
        }
        String s = calculText(text);
        int w = Math.round(mPaint.measureText(s));

        if (target.getParent() instanceof IndicatorFrameLayout) {
            //已经偷梁换柱
            IndicatorView indicatorView = (IndicatorView) ((IndicatorFrameLayout) target.getParent()).getChildAt(1);
            FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(
                    shape == Shape.POINT ? size : w > size ? w + DensityUtil.dp2px(getContext(), PADDING) : size, size);

            p.leftMargin = leftMargin;
            p.topMargin = topMargin;
            p.rightMargin = rightMargin;
            p.bottomMargin = bottomMargin;
            p.gravity = gravity;
            indicatorView.backgroundColor = backgroundColor;
            indicatorView.textSize = textSize;
            indicatorView.mPaint.setTextSize(DensityUtil.sp2px(getContext(), textSize));
            indicatorView.textColor = textColor;
            indicatorView.text = text;
            indicatorView.plus = plus;
            indicatorView.shape = shape;
            indicatorView.setLayoutParams(p);
            indicatorView.invalidate();
        } else {
            ViewGroup viewGroup = (ViewGroup) target.getParent();
            ViewGroup.LayoutParams targetP = target.getLayoutParams();
            FrameLayout.LayoutParams indicatorP = new FrameLayout.LayoutParams(
                    shape == Shape.POINT ? size : w > size ? w + DensityUtil.dp2px(getContext(), PADDING) : size, size);

            indicatorP.leftMargin = leftMargin;
            indicatorP.topMargin = topMargin;
            indicatorP.rightMargin = rightMargin;
            indicatorP.bottomMargin = bottomMargin;
            indicatorP.gravity = gravity;

            IndicatorFrameLayout parent = new IndicatorFrameLayout(target.getContext());
            int index = viewGroup.indexOfChild(target);
            viewGroup.removeView(target);
            parent.addView(target, targetP);
            parent.addView(this, indicatorP);
            viewGroup.addView(parent, index,targetP);
        }
        return this;
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
        private Context context;
        private int size = 0x20;
        private int leftMargin = 0;
        private int topMargin = 0;
        private int bottomMargin = 0;
        private int rightMargin = 0;
        private int backgroundColor = Color.RED;
        private float textSize = 12f;
        private int textColor = Color.WHITE;
        private int text = 0;
        private int gravity = Gravity.END | Gravity.TOP;
        private boolean plus = true;
        private Shape shape = Shape.POINT;

        public Builder(Context context) {
            this.context = context;
        }

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
         * 在BindView的相对位置
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
         * @param text
         * @return
         */
        public Builder text(int text) {
            this.text = text;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

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
         * @param plus
         * @return
         */
        public Builder needPlus(boolean plus) {
            this.plus = plus;
            return this;
        }

        /**
         * 设置形状
         *
         * @param shape
         * @return
         */
        public Builder shape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public IndicatorView build() {
            return new IndicatorView(this);
        }
    }
}
