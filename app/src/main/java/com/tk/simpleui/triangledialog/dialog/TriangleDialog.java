package com.tk.simpleui.triangledialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tk.simpleui.R;
import com.tk.simpleui.bar.BarUtils;
import com.tk.simpleui.common.DensityUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Direction.*;


/**
 * <pre>
 *      author : TK
 *      time : 2018/3/30
 *      desc :
 * </pre>
 */

public class TriangleDialog extends Dialog {
    private View anchor;

    /**
     * 三角箭头朝向
     */
    @IntDef({LEFT, TOP, RIGHT, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
        int LEFT = 0x01;
        int TOP = 0x02;
        int RIGHT = 0x03;
        int BOTTOM = 0x04;
    }

    private RecyclerView recyclerContent;
    private TriangleContainer triangleContainer;

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int radius;
    @Direction
    private int direction;

    private int triangleWidth;
    private int triangleHeight;
    private int offX;
    private int offY;
    private int containerOffset;
    private int containerColor;

    private int shadowRadius;
    private int shadowColor;

    private RecyclerView.Adapter adapter;

    public TriangleDialog(Context context) {
        this(context, new Builder(context));
    }

    private TriangleDialog(Context context, Builder builder) {
        super(context);
        paddingLeft = builder.paddingLeft;
        paddingTop = builder.paddingTop;
        paddingRight = builder.paddingRight;
        paddingBottom = builder.paddingBottom;
        radius = builder.radius;
        direction = builder.direction;
        triangleWidth = builder.triangleWidth;
        triangleHeight = builder.triangleHeight;
        offX = builder.offX;
        offY = builder.offY;
        containerOffset = builder.containerOffset;
        containerColor = builder.containerColor;
        shadowRadius = builder.shadowRadius;
        shadowColor = builder.shadowColor;
        adapter = builder.adapter;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (shadowRadius > 0) {
            //有阴影的话不自动变暗
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triangleContainer = new TriangleContainer(getContext());
        recyclerContent = new RecyclerView(getContext());
        recyclerContent.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerContent.setHasFixedSize(true);
        recyclerContent.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContent.setAdapter(adapter);
        triangleContainer.addView(recyclerContent);
        setContentView(triangleContainer);

        triangleContainer.prepare(paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom,
                radius,
                direction,
                triangleWidth,
                triangleHeight,
                containerOffset,
                containerColor,
                shadowRadius,
                shadowColor);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus || anchor == null) {
            return;
        }

        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        int anchorWidth = anchor.getMeasuredWidth();
        int anchorHeight = anchor.getMeasuredHeight();
        int dialogWidth = getWindow().getDecorView().getMeasuredWidth();
        int dialogHeight = getWindow().getDecorView().getMeasuredHeight();

        int barHeight = getStatusBarHeight(getContext());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.START | Gravity.TOP;
        int rawX = 0;
        int rawY = 0;
        switch (direction) {
            case Direction.BOTTOM:
                containerOffset = Math.min(containerOffset, dialogWidth - shadowRadius * 2 - radius * 2 - triangleWidth);
                rawX = location[0] + anchorWidth / 2
                        - triangleWidth / 2 - radius - shadowRadius
                        + offX - containerOffset;
                rawY = location[1] - dialogHeight - barHeight
                        + offY;
                break;
            case LEFT:
                containerOffset = Math.min(containerOffset, dialogHeight - shadowRadius * 2 - radius * 2 - triangleWidth);
                rawX = location[0] + anchorWidth
                        + offX;
                rawY = location[1] + anchorHeight / 2
                        - triangleWidth / 2 - radius - shadowRadius - barHeight
                        + offY - containerOffset;
                break;
            case Direction.RIGHT:
                containerOffset = Math.min(containerOffset, dialogHeight - shadowRadius * 2 - radius * 2 - triangleWidth);
                rawX = location[0] - dialogWidth
                        + offX;
                rawY = location[1] + anchorHeight / 2
                        - triangleWidth / 2 - radius - shadowRadius - barHeight
                        + offY - containerOffset;
                break;
            case Direction.TOP:
                containerOffset = Math.min(containerOffset, dialogWidth - shadowRadius * 2 - radius * 2 - triangleWidth);
                rawX = location[0] + anchorWidth / 2
                        - triangleWidth / 2 - radius - shadowRadius
                        + offX - containerOffset;
                rawY = location[1] + anchorHeight - barHeight
                        + offY;
                break;
        }
        params.x = rawX;
        params.y = rawY;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
    }

    /**
     * 显示
     *
     * @param anchor
     */
    public void show(@NonNull View anchor) {
        this.anchor = anchor;
        super.show();
    }

    private static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static final class Builder {
        private Context context;

        private int paddingLeft = 0;
        private int paddingTop = 0;
        private int paddingRight = 0;
        private int paddingBottom = 0;
        private int radius = DensityUtil.dp2px(4);
        private int direction = Direction.BOTTOM;
        private int triangleWidth = DensityUtil.dp2px(12);
        private int triangleHeight = DensityUtil.dp2px(8);
        private int offX = 0;
        private int offY = 0;
        private int containerOffset = 0;
        private int containerColor = Color.WHITE;
        private int shadowRadius = DensityUtil.dp2px(2);
        private int shadowColor = Color.GRAY;
        private RecyclerView.Adapter adapter = null;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 主体区域内padding
         *
         * @param paddingLeft
         * @return
         */
        public Builder paddingLeft(@IntRange(from = 0) int paddingLeft) {
            this.paddingLeft = Math.max(paddingLeft, 0);
            return this;
        }

        /**
         * 主体区域内padding
         *
         * @param paddingTop
         * @return
         */
        public Builder paddingTop(@IntRange(from = 0) int paddingTop) {
            this.paddingTop = Math.max(paddingTop, 0);
            return this;
        }

        /**
         * 主体区域内padding
         *
         * @param paddingRight
         * @return
         */
        public Builder paddingRight(@IntRange(from = 0) int paddingRight) {
            this.paddingRight = Math.max(paddingRight, 0);
            return this;
        }

        /**
         * 主体区域内padding
         *
         * @param paddingBottom
         * @return
         */
        public Builder paddingBottom(@IntRange(from = 0) int paddingBottom) {
            this.paddingBottom = Math.max(paddingBottom, 0);
            return this;
        }

        /**
         * 主体区域圆角
         *
         * @param radius
         * @return
         */
        public Builder radius(@IntRange(from = 0) int radius) {
            this.radius = Math.max(radius, 0);
            return this;
        }

        /**
         * 箭头方向
         *
         * @param direction
         * @return
         */
        public Builder direction(@Direction int direction) {
            this.direction = direction;
            return this;
        }

        /**
         * 三角宽度
         *
         * @param triangleWidth
         * @return
         */
        public Builder triangleWidth(@IntRange(from = 0) int triangleWidth) {
            this.triangleWidth = Math.max(triangleWidth, 0);
            return this;
        }

        /**
         * 三角高度
         *
         * @param triangleHeight
         * @return
         */
        public Builder triangleHeight(@IntRange(from = 0) int triangleHeight) {
            this.triangleHeight = Math.max(triangleHeight, 0);
            return this;
        }

        /**
         * 整体x轴偏移
         *
         * @param offX
         * @return
         */
        public Builder offX(int offX) {
            this.offX = offX;
            return this;
        }

        /**
         * 整体y轴偏移
         *
         * @param offY
         * @return
         */
        public Builder offY(int offY) {
            this.offY = offY;
            return this;
        }

        /**
         * 主体区域偏移
         *
         * @param containerOffset
         * @return
         */
        public Builder containerOffset(@IntRange(from = 0) int containerOffset) {
            this.containerOffset = Math.max(containerOffset, 0);
            return this;
        }

        /**
         * 主体区域颜色
         *
         * @param containerColor
         * @return
         */
        public Builder containerColor(@ColorInt int containerColor) {
            this.containerColor = containerColor;
            return this;
        }

        /**
         * 阴影的扩散半径
         *
         * @param shadowRadius
         * @return
         */
        public Builder shadowRadius(@IntRange(from = 0) int shadowRadius) {
            this.shadowRadius = Math.max(shadowRadius, 0);
            return this;
        }

        /**
         * 阴影颜色
         *
         * @param shadowColor
         * @return
         */
        public Builder shadowColor(@ColorInt int shadowColor) {
            this.shadowColor = shadowColor;
            return this;
        }

        /**
         * 适配器
         *
         * @param adapter
         * @return
         */
        public Builder adapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public TriangleDialog build() {
            return new TriangleDialog(context, this);
        }
    }
}
