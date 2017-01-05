package com.tk.simpleui.ripple.reveal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by TK on 2017/1/5.
 */

public class CircleRevealLayout extends FrameLayout {
    private View target;
    private int centerX;
    private int centerY;
    private float startRadius;
    private float endRadius;
    private int during;

    private ValueAnimator animator;
    private Path mPath = new Path();
    private float mRevealRadius;

    public CircleRevealLayout(Context context) {
        this(context, null);
    }

    public CircleRevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    private ValueAnimator initAnim() {
        if (animator != null && animator.isRunning()) {
            return animator;
        }
        animator = ValueAnimator.ofFloat(startRadius, endRadius);
        animator.setDuration(during);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRevealRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setLayerType(LAYER_TYPE_HARDWARE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setLayerType(LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setLayerType(LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        return animator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (animator != null && animator.isRunning() && child == target) {
            mPath.reset();
            mPath.addCircle(centerX, centerY, mRevealRadius, Path.Direction.CW);
            canvas.clipPath(mPath);
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    public static class Builder {
        private View target;
        private int centerX;
        private int centerY;
        private float startRadius;
        private float endRadius;
        private int during;

        public Builder() {
        }

        public Builder setTarget(View target) {
            this.target = target;
            return this;
        }

        public Builder setCenterX(int centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder setCenterY(int centerY) {
            this.centerY = centerY;
            return this;
        }

        public Builder setStartRadius(float startRadius) {
            this.startRadius = startRadius;
            return this;
        }

        public Builder setEndRadius(float endRadius) {
            this.endRadius = endRadius;
            return this;
        }

        public Builder setDuring(int during) {
            this.during = during;
            return this;
        }

        public ValueAnimator build(CircleRevealLayout layout) {
            layout.target = target;
            layout.centerX = centerX;
            layout.centerY = centerY;
            layout.startRadius = startRadius;
            layout.endRadius = endRadius;
            layout.during = during;

            return layout.initAnim();
        }
    }
}
