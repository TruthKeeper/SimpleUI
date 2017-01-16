package com.tk.simpleui.shopcar;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tk.simpleui.R;

/**
 * Created by TK on 2016/12/30.
 */

public class KeepChangeLayout extends LinearLayout {
    public static final int CHANGE_TIME = 300;
    private static final int INCREASE_WHAT = 0x01;
    private static final int DECREASE_WHAT = 0x02;
    private ImageView mLeftView;
    private ImageView mRightView;
    private TextView mTextView;

    private int mSum;
    private int mPadding;
    /**
     * 当前选择数量
     */
    private int mNowSum = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INCREASE_WHAT) {
                increase();
                sendEmptyMessageDelayed(INCREASE_WHAT, CHANGE_TIME);
            } else if (msg.what == DECREASE_WHAT) {
                decrease();
                sendEmptyMessageDelayed(DECREASE_WHAT, CHANGE_TIME);
            }
        }
    };
    private ScaleAnimation mIncreaseAnim;

    public KeepChangeLayout(Context context) {
        this(context, null);
    }

    public KeepChangeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeepChangeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TintTypedArray array = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.KeepChangeLayout);
        mLeftView.setImageDrawable(array.getDrawable(R.styleable.KeepChangeLayout_leftDrawable));
        mRightView.setImageDrawable(array.getDrawable(R.styleable.KeepChangeLayout_rightDrawable));
        mTextView.setTextColor(array.getColor(R.styleable.KeepChangeLayout_textColor, Color.BLACK));
        mTextView.getPaint().setTextSize(array.getDimensionPixelSize(R.styleable.KeepChangeLayout_textSize, 16));
        int size = array.getDimensionPixelOffset(R.styleable.KeepChangeLayout_drawableSize, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (size != ViewGroup.LayoutParams.WRAP_CONTENT) {
            mLeftView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
            mRightView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        }
        //默认最大99

        mSum = array.getInt(R.styleable.KeepChangeLayout_textSum, 99);
        mPadding = array.getDimensionPixelOffset(R.styleable.KeepChangeLayout_textPadding, 0);
        setSum(mSum);
        array.recycle();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        mIncreaseAnim = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        mIncreaseAnim.setDuration(250);
        mIncreaseAnim.setFillAfter(false);
        mIncreaseAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        mLeftView = new ImageView(getContext());
        mRightView = new ImageView(getContext());
        mTextView = new TextView(getContext());
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setText("0");

        addView(mLeftView);
        addView(mTextView);
        addView(mRightView);
        initTouch();
    }

    private void initTouch() {
        mLeftView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        decrease();
                        mHandler.sendEmptyMessageDelayed(DECREASE_WHAT, CHANGE_TIME);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.removeMessages(DECREASE_WHAT);
                        break;
                }
                return false;
            }
        });
        mRightView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        increase();
                        mHandler.sendEmptyMessageDelayed(INCREASE_WHAT, CHANGE_TIME);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.removeMessages(INCREASE_WHAT);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置最多能选择的数量
     *
     * @param sum
     */
    public void setSum(int sum) {
        this.mSum = sum;
        float maxWidth = mTextView.getPaint().measureText(Integer.toString(mSum));
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(Math.round(maxWidth + mPadding), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 获取选择的数值
     *
     * @return
     */
    public int getNowSum() {
        return mNowSum;
    }

    private void increase() {
        if (mNowSum >= mSum) {
            return;
        }
        mNowSum++;
        mTextView.setText(Integer.toString(mNowSum));
        applyIncreaseAnim();
    }

    private void decrease() {
        if (mNowSum <= 0) {
            return;
        }
        mNowSum--;
        mTextView.setText(Integer.toString(mNowSum));

    }

    private void applyIncreaseAnim() {
        mTextView.clearAnimation();
        mTextView.startAnimation(mIncreaseAnim);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTextView != null) {
            mTextView.clearAnimation();
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}
