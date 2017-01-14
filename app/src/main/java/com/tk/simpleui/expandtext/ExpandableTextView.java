package com.tk.simpleui.expandtext;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;

/**
 * Created by TK on 2017/1/10.
 */

public class ExpandableTextView extends LinearLayout {
    //0 折叠 1 扩展
    public static final int STATUS_COLLAPSE = 0;
    public static final int STATUS_EXPAND = 1;
    private int minLines = 3;
    private TextView contentView;
    private TextView indicatorView;
    private TextView fake;
    private ValueAnimator anim;
    private int during = 350;
    private int lineSpacingExtra;
    private float lineSpacingMultiplier;

    private int collapseHeight;
    private int expandTextHeight;

    private int status;

    private OnExpandListener listener;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
            contentView.setTextColor(array.getColor(R.styleable.ExpandableTextView_contentColor, Color.BLACK));
            contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimension(R.styleable.ExpandableTextView_contentSize, DensityUtil.sp2px(context, 14)));
            lineSpacingExtra = array.getDimensionPixelOffset(R.styleable.ExpandableTextView_lineSpacingExtra, lineSpacingExtra);
            lineSpacingMultiplier = array.getFloat(R.styleable.ExpandableTextView_lineSpacingMultiplier, lineSpacingMultiplier);
            contentView.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier);
            indicatorView.setTextColor(array.getColor(R.styleable.ExpandableTextView_indicatorTextColor, 0xFF1E85D4));
            indicatorView.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimension(R.styleable.ExpandableTextView_indicatorTextSize, DensityUtil.sp2px(context, 15)));
            int mode = array.getInt(R.styleable.ExpandableTextView_indicatorMode, 0);
            switch (mode) {
                case 1:
                    ((LayoutParams) indicatorView.getLayoutParams()).gravity = Gravity.CENTER;
                    break;
                case 2:
                    ((LayoutParams) indicatorView.getLayoutParams()).gravity = Gravity.END;
                    break;
            }
            int marginTop = array.getDimensionPixelOffset(R.styleable.ExpandableTextView_indicatorMarginTop, DensityUtil.dp2px(context, 6));
            ((LayoutParams) indicatorView.getLayoutParams()).topMargin = marginTop;
            during = array.getInt(R.styleable.ExpandableTextView_during, during);
            minLines = array.getInt(R.styleable.ExpandableTextView_minLines, minLines);
            setContent(array.getString(R.styleable.ExpandableTextView_content));
            array.recycle();
        }
        initAnim();
    }

    private void initAnim() {
        anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(during);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                if (status == 0) {
                    contentView.setMaxHeight((int) (collapseHeight + (expandTextHeight - collapseHeight) * f));
                } else {
                    contentView.setMaxHeight((int) (collapseHeight + (expandTextHeight - collapseHeight) * (1 - f)));
                }
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (status == STATUS_EXPAND) {
                    status = STATUS_COLLAPSE;
                    contentView.setMaxLines(minLines);
                } else {
                    status = STATUS_EXPAND;
                }
                if (listener != null) {
                    listener.onStatusChange(status);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void init() {
        lineSpacingExtra = DensityUtil.dp2px(getContext(), 3);
        lineSpacingMultiplier = 1f;
        LayoutInflater.from(getContext()).inflate(R.layout.expandable_textview_layout, this);
        setOrientation(VERTICAL);
        contentView = (TextView) findViewById(R.id.content);
        indicatorView = (TextView) findViewById(R.id.btn);
        indicatorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!anim.isRunning() && expandTextHeight > collapseHeight) {
                    anim.start();
                }

            }
        });

    }

    /**
     * 设置文本内容
     *
     * @param text
     */
    public void setContent(CharSequence text) {
        setContent(text, false);
    }

    /**
     * 设置文本内容
     *
     * @param text
     */
    public void setContent(CharSequence text, final boolean expand) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        contentView.setText(text);
        contentView.setMaxLines(minLines);
        fake = new TextView(getContext());
        fake.setMaxLines(Integer.MAX_VALUE);
        fake.setText(text);
        fake.getPaint().setTextSize(contentView.getPaint().getTextSize());
        fake.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier);
        post(new Runnable() {
            @Override
            public void run() {
                status = expand ? STATUS_EXPAND : STATUS_COLLAPSE;
                int widthMeasureSpec = MeasureSpec.makeMeasureSpec(contentView.getMeasuredWidth(), MeasureSpec.EXACTLY);
                int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                fake.measure(widthMeasureSpec, heightMeasureSpec);
                expandTextHeight = getTextViewHeight(fake);
                collapseHeight = getTextViewHeight(contentView);
                if (expand) {
                    contentView.setMaxLines(Integer.MAX_VALUE);
                }
            }
        });

    }

    /**
     * 当前状态是否处于展开状态
     *
     * @return
     */
    public boolean isExpand() {
        return status == 1;
    }

    /**
     * 获取TextView真实高度
     *
     * @param textView
     * @return
     */
    private static int getTextViewHeight(TextView textView) {
        int textHeight = textView.getLineHeight() * textView.getLineCount();
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    public void setOnExpandListener(OnExpandListener listener) {
        this.listener = listener;
    }

    public interface OnExpandListener {
        void onStatusChange(int status);
    }
}
