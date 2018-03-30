package com.tk.simpleui.scratch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by TK on 2016/10/25.
 * 刮奖
 */

public class ScratchView extends View {

    private static final PorterDuffXfermode MODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    //70%+触发
    public static final float ACTIVITY = 0.65f;
    //刮层颜色
    public static final int COVER_COLOR = 0xFFCECECE;

    private Path mPath = new Path();
    private Paint mPaint = new Paint();
    private float mLastX;
    private float mLastY;
    //刮痕粗细
    private int mStroke = 50;

    private Disposable mDisposable;
    private boolean isFinish;
    private OnScratchListener onScratchListener;

    private ValueAnimator mAnimator;
    private int mCoverColor = COVER_COLOR;

    public ScratchView(Context context) {
        super(context);
        init();
    }

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setBackgroundColor(Color.TRANSPARENT);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStroke);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //优化尖锐拐点
        mPaint.setPathEffect(new CornerPathEffect(4));
        mAnimator = ValueAnimator.ofFloat(1f, 0f)
                .setDuration(500);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                mCoverColor = Color.argb(Math.round(f * 255), Color.red(COVER_COLOR), Color.green(COVER_COLOR), Color.blue(COVER_COLOR));
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mCoverColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(null);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        if (!mPath.isEmpty()) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setXfermode(MODE);
            canvas.drawPath(mPath, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFinish) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mPath.moveTo(mLastX, mLastY);
                invalidate();

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mLastX - event.getX()) > 3
                        || Math.abs(mLastY - event.getY()) > 3) {
                    mPath.lineTo(mLastX, mLastY);
                    invalidate();
                }
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                check();
                break;
        }
        return true;
    }

    /**
     * 临摹生成快照
     *
     * @param view
     * @param w
     * @param h
     * @return
     */
    private static Bitmap createBitmap(View view, int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 检查区域
     */
    private void check() {
        if (mDisposable != null && (!mDisposable.isDisposed())) {
            mDisposable.dispose();
        }
        if (isFinish) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Bitmap bitmap = createBitmap(ScratchView.this, getWidth(), getHeight());
                int sum = 0;
                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j < getHeight(); j++) {
                        int color = bitmap.getPixel(i, j);
                        if (color == Color.TRANSPARENT) {
                            sum++;
                        }
                    }
                }
                e.onNext(sum);
                e.onComplete();
            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer >= getWidth() * getHeight() * ACTIVITY;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        isFinish = true;
                        mAnimator.start();
                        if (onScratchListener != null) {
                            onScratchListener.onComplete();
                        }
                    }
                });
    }


    /**
     * 再刮一次
     */
    public void scratchAgain() {
        if (mDisposable != null && (!mDisposable.isDisposed())) {
            mDisposable.dispose();
        }
        mPath.reset();
        mCoverColor = COVER_COLOR;
        mPaint.setColor(mCoverColor);
        isFinish = false;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDisposable != null && (!mDisposable.isDisposed())) {
            mDisposable.dispose();
        }
    }

    public void setOnScratchListener(OnScratchListener onScratchListener) {
        this.onScratchListener = onScratchListener;
    }

    public interface OnScratchListener {
        void onComplete();
    }

}
