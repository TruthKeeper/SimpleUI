package com.tk.simpleui.gesturelock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2017/1/16.
 */

public class GesureLockView extends ViewGroup {
    public static final int ROW = 3;
    public static final int COLUMN = 3;
    public static final int THEME_COLOR = 0xFF1E85D4;
    public static final int FAILURE_COLOR = 0xFFFF6262;
    /**
     * 最少的点
     */
    public static final int MIN_SIZE = 4;
    /**
     * 路径默认2DP
     */
    public static final int PATH_STROKE = 2;
    /**
     * 默认错误提示路线
     */
    public static final boolean FAILURE_GUIDE = true;
    /**
     * 默认触摸时不提示路线
     */
    public static final boolean DRAGGING_GUIDE = false;
    /**
     * TODO: 2017/1/16 暂时写死
     * 默认每个小区块大小 DP，圆的直径为  DEFAULT_SIZE   *  PERCENT_R_BIG  =90*0.6f
     */
    public static final int DEFAULT_SIZE = 90;

    /**
     * 痕迹清理时间
     */
    public static final int DELAY_DURING = 1_000;
    private int mRow = ROW;
    private int mColumn = COLUMN;
    private int mThemeColor = THEME_COLOR;
    private int mFailureColor = FAILURE_COLOR;
    private int mMinSize = MIN_SIZE;

    private boolean mFailureGuide = FAILURE_GUIDE;
    private boolean mDraggingGuide = DRAGGING_GUIDE;
    /**
     * 是否响应触摸事件
     */
    private boolean mCanDrag = true;
    /**
     * 子view 二维数组
     */
    private TouchView[][] mChildViews;

    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    /**
     * 存放位置信息
     */
    private List<GestureBean> mDragList = new ArrayList<>();
    private OnGestureListener onGestureListener;

    public GesureLockView(Context context) {
        this(context, null);
    }

    public GesureLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GesureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int stroke = DensityUtil.dp2px(context, PATH_STROKE);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GesureLockView);
            mRow = array.getInt(R.styleable.GesureLockView_row, ROW);
            mColumn = array.getInt(R.styleable.GesureLockView_column, COLUMN);
            mThemeColor = array.getColor(R.styleable.GesureLockView_themeColor, THEME_COLOR);
            mFailureColor = array.getColor(R.styleable.GesureLockView_failureColor, FAILURE_COLOR);
            mMinSize = array.getInt(R.styleable.GesureLockView_minSize, MIN_SIZE);
            stroke = array.getDimensionPixelOffset(R.styleable.GesureLockView_pathStroke, stroke);
            mFailureGuide = array.getBoolean(R.styleable.GesureLockView_failureGuide, FAILURE_GUIDE);
            mDraggingGuide = array.getBoolean(R.styleable.GesureLockView_draggingGuide, DRAGGING_GUIDE);
            checkParam();
            array.recycle();
        }
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(stroke);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * check
     */
    private void checkParam() {
        if (mRow < 1 || mColumn < 1 || mMinSize > (mColumn * mRow)) {
            throw new IllegalArgumentException("param not standard!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = DensityUtil.dp2px(getContext(), DEFAULT_SIZE);
        setMeasuredDimension(measureWidth(widthMeasureSpec, defaultSize),
                measureHeight(heightMeasureSpec, defaultSize));
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @param defaultSize
     * @return
     */
    private int measureWidth(int widthMeasureSpec, int defaultSize) {
        int wResult = 0;
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            wResult = defaultSize * mColumn;
        } else if (wMode == MeasureSpec.EXACTLY) {
            wResult = wSize;
        }
        return wResult;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @param defaultSize
     * @return
     */
    private int measureHeight(int heightMeasureSpec, int defaultSize) {
        int hResult = 0;
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (hMode == MeasureSpec.AT_MOST) {
            hResult = defaultSize * mRow;
        } else if (hMode == MeasureSpec.EXACTLY) {
            hResult = hSize;
        }
        return hResult;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mChildViews != null) {
            return;
        }
        int mWidth = r - l;
        int mHeight = b - t;
        mChildViews = new TouchView[mRow][mColumn];
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                TouchView childView = new TouchView(getContext());
                childView.initColors(mThemeColor, mFailureColor);
                childView.layout(mWidth / mColumn * j,
                        mHeight / mRow * i,
                        mWidth / mColumn * (j + 1),
                        mHeight / mRow * (i + 1));
                mChildViews[i][j] = childView;
                addView(mChildViews[i][j]);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mPath.isEmpty()) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mCanDrag) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = ev.getX();
                float newY = ev.getY();
                TouchView childView = null;
                for (int i = 0; i < mRow; i++) {
                    for (int j = 0; j < mColumn; j++) {
                        childView = mChildViews[i][j];
                        int centerX = mChildViews[i][j].getLeft() + mChildViews[i][j].getRight() >> 1;
                        int centerY = mChildViews[i][j].getTop() + mChildViews[i][j].getBottom() >> 1;
                        if (Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2) <= Math.pow(childView.getBigRadius(), 2)) {
                            //点击范围内
                            GestureBean newBean = new GestureBean(i, j);
                            if (mDragList.size() != 0) {
                                GestureBean lastBean = mDragList.get(mDragList.size() - 1);
                                //判断和队列末尾的点之间是否有间隔点，有就把它点亮
                                addSpacePoint(lastBean, newBean);
                            }
                            if (!mDragList.contains(newBean)) {
                                //未包含就add
                                childView.setState(TouchView.STATE_DRAGGING);
                                mDragList.add(newBean);
                            }
                            break;
                        }
                    }
                }
                initTriangle();
                for (int i = 0; i < mRow; i++) {
                    for (int j = 0; j < mColumn; j++) {
                        mChildViews[i][j].invalidate();
                    }
                }
                mPaint.setColor(mThemeColor);
                if (mDragList.size() != 0) {
                    drawPath();
                    mPath.lineTo(newX, newY);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mDragList.size() == 0) {
                    break;
                }
                if (mDragList.size() < mMinSize) {
                    mPaint.setColor(mFailureColor);
                    drawPath();
                    setFailureGesture();
                    if (onGestureListener != null) {
                        onGestureListener.onFailure(this);
                    }
                } else {
                    mPaint.setColor(mThemeColor);
                    drawPath();
                    if (onGestureListener != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mDragList.size(); i++) {
                            GestureBean bean = mDragList.get(i);
                            sb.append(bean.getRow() * mColumn + bean.getColumn());
                        }
                        onGestureListener.onDragOver(this, sb.toString());
                    }
                }
                break;
        }
        return true;
    }


    /**
     * 设置三角形偏移角度
     */
    private void initTriangle() {
        if (mDragList.size() < 2) {
            return;
        }
        GestureBean bean;
        GestureBean nextBean;
        for (int i = 0; i < mDragList.size() - 1; i++) {
            bean = mDragList.get(i);
            nextBean = mDragList.get(i + 1);
            float offX = nextBean.getColumn() - bean.getColumn();
            float offY = nextBean.getRow() - bean.getRow();
            float degree = 0f;
            if (offX > 0) {
                degree = (float) (Math.atan(offY / offX) / Math.PI * 180 + 90);
            } else if (offX < 0) {
                degree = (float) (Math.atan(offY / offX) / Math.PI * 180 + 270);
            } else {
                if (offY > 0) {
                    degree = 180;
                } else {
                    degree = 0;
                }
            }
            mChildViews[bean.getRow()][bean.getColumn()].setGuideDegree(degree);
            mChildViews[bean.getRow()][bean.getColumn()].setDragGuide(mDraggingGuide);
        }
    }

    /**
     * 绘制路径
     */
    private void drawPath() {
        if (mDragList.size() != 0) {
            mPath.reset();
            GestureBean bean;
            TouchView view;
            bean = mDragList.get(0);
            view = mChildViews[bean.getRow()][bean.getColumn()];
            mPath.moveTo(view.getLeft() + view.getRight() >> 1, view.getTop() + view.getBottom() >> 1);

            for (int i = 1; i < mDragList.size(); i++) {
                bean = mDragList.get(i);
                view = mChildViews[bean.getRow()][bean.getColumn()];
                mPath.lineTo(view.getLeft() + view.getRight() >> 1, view.getTop() + view.getBottom() >> 1);
            }
        }
    }

    /**
     * 排查中间是否间隔其他点
     *
     * @param lastBean
     * @param newBean
     */
    private void addSpacePoint(GestureBean lastBean, GestureBean newBean) {
        if (newBean.getRow() == lastBean.getRow()) {
            //同行
            int offX = newBean.getColumn() - lastBean.getColumn();
            //有间隔,遍历取得点
            if (offX >= 2) {
                for (int o = 1; o < offX; o++) {
                    generateOffsetBean(0, o, lastBean);
                }
            } else if (offX <= -2) {
                for (int o = -1; o > offX; o--) {
                    generateOffsetBean(0, o, lastBean);
                }
            }
        } else if (newBean.getColumn() == lastBean.getColumn()) {
            //同列
            int offY = newBean.getRow() - lastBean.getRow();
            //有间隔,遍历取得点
            if (offY >= 2) {
                for (int o = 1; o < offY; o++) {
                    generateOffsetBean(o, 0, lastBean);
                }
            } else if (offY <= -2) {
                for (int o = -1; o > offY; o--) {
                    generateOffsetBean(o, 0, lastBean);
                }
            }
        } else {
            int offX = newBean.getColumn() - lastBean.getColumn();
            int offY = newBean.getRow() - lastBean.getRow();
            if (Math.abs(offX) >= 2 &&
                    Math.abs(offY) >= 2 &&
                    Math.abs(offX) == Math.abs(offY)) {
                //45°线
                if (offX > 0) {
                    if (offY > 0) {
                        for (int o = 1; o < offX; o++) {
                            generateOffsetBean(o, o, lastBean);
                        }
                    } else {
                        for (int o = 1; o < offX; o++) {
                            generateOffsetBean(-o, o, lastBean);
                        }
                    }
                } else {
                    if (offY > 0) {
                        for (int o = -1; o > offX; o--) {
                            generateOffsetBean(-o, o, lastBean);
                        }
                    } else {
                        for (int o = -1; o > offX; o--) {
                            generateOffsetBean(o, o, lastBean);
                        }
                    }
                }
            }
        }
    }

    /**
     * 生成偏移点集合并添加
     *
     * @param offX
     * @param offY
     * @param sourceBean
     */
    private void generateOffsetBean(int offX, int offY, GestureBean sourceBean) {
        GestureBean offsetBean = new GestureBean(sourceBean.getRow() + offX, sourceBean.getColumn() + offY);
        if (!mDragList.contains(offsetBean)) {
            mDragList.add(offsetBean);
            mChildViews[offsetBean.getRow()][offsetBean.getColumn()].setState(TouchView.STATE_DRAGGING);
        }
    }

    /**
     * 设置路线错误，密码错误时调用
     */
    public void setFailureGesture() {
        mPaint.setColor(mFailureColor);
        for (int i = 0; i < mDragList.size(); i++) {
            GestureBean bean = mDragList.get(i);
            mChildViews[bean.getRow()][bean.getColumn()].setState(TouchView.STATE_FAILURE);
            if (i != mDragList.size() - 1) {
                mChildViews[bean.getRow()][bean.getColumn()].setFailureGuide(mFailureGuide);
            } else {
                mChildViews[bean.getRow()][bean.getColumn()].setFailureGuide(false);
            }
            mChildViews[bean.getRow()][bean.getColumn()].invalidate();
        }
        initTriangle();
        invalidate();
        delayClearMark();
    }


    /**
     * 设置区域是否可以响应
     *
     * @param canDrag
     */
    public void setDragEnable(boolean canDrag) {
        this.mCanDrag = canDrag;
    }

    /**
     * 擦除痕迹
     */
    public void clearMark() {
        mPaint.setColor(mThemeColor);
        mDragList.clear();
        mPath.reset();
        for (int i = 0; i < mRow; i++) {
            for (int j = 0; j < mColumn; j++) {
                mChildViews[i][j].setDragGuide(false);
                mChildViews[i][j].setFailureGuide(false);
                mChildViews[i][j].setState(TouchView.STATE_INIT);
                mChildViews[i][j].invalidate();
            }
        }
    }

    /**
     * 延迟清理痕迹
     */
    public void delayClearMark() {
        final boolean c = mCanDrag;
        //上锁
        mCanDrag = false;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mCanDrag = c;
                clearMark();
            }
        }, DELAY_DURING);
    }

    /**
     * 构造
     *
     * @param builder
     */
    public void build(Builder builder) {
        mRow = builder.mRow;
        mColumn = builder.mColumn;
        mThemeColor = builder.mThemeColor;
        mFailureColor = builder.mFailureColor;
        mMinSize = builder.mMinSize;
        mFailureGuide = builder.mFailureGuide;
        mDraggingGuide = builder.mDraggingGuide;

        checkParam();
        mChildViews = null;
        mPath.reset();
        removeAllViews();
        invalidate();
    }

    /**
     * 设置滑动是否带有引导
     *
     * @param draggingGuide
     */
    public void setDraggingGuide(boolean draggingGuide) {
        this.mDraggingGuide = draggingGuide;
    }

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    public interface OnGestureListener {
        void onDragOver(GesureLockView view, String source);

        void onFailure(GesureLockView view);
    }

    public static class Builder {
        int mRow = ROW;
        int mColumn = COLUMN;
        int mThemeColor = THEME_COLOR;
        int mFailureColor = FAILURE_COLOR;
        int mMinSize = MIN_SIZE;
        boolean mFailureGuide = FAILURE_GUIDE;
        boolean mDraggingGuide = DRAGGING_GUIDE;

        public Builder() {
        }

        public Builder setRow(int row) {
            this.mRow = row;
            return this;
        }

        public Builder setColumn(int column) {
            this.mColumn = column;
            return this;
        }

        public Builder setThemeColor(int themeColor) {
            this.mThemeColor = themeColor;
            return this;
        }

        public Builder setFailureColor(int failureColor) {
            this.mFailureColor = failureColor;
            return this;
        }

        public Builder setMinSize(int minSize) {
            this.mMinSize = minSize;
            return this;
        }

        public Builder setFailureGuide(boolean failureGuide) {
            this.mFailureGuide = failureGuide;
            return this;
        }

        public Builder setDraggingGuide(boolean draggingGuide) {
            this.mDraggingGuide = draggingGuide;
            return this;
        }

    }
}
