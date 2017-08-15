package com.tk.simpleui.triangledialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;
import com.tk.simpleui.statusbar.StatusBarHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.BOTTOM;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.LEFT;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.RIGHT;
import static com.tk.simpleui.triangledialog.dialog.TriangleDialog.Mode.TOP;


/**
 * <pre>
 *      author : TK
 *      time : 2017/8/15
 *      desc :
 * </pre>
 */

public abstract class TriangleDialog extends Dialog {
    /**
     * 三角箭头朝向
     */
    @IntDef({LEFT, TOP, RIGHT, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)

    public @interface Mode {
        int LEFT = 0x01;
        int TOP = 0x02;
        int RIGHT = 0x03;
        int BOTTOM = 0x04;
    }

    private RecyclerView recyclerContent;
    private TriangleContainer triangleContainer;
    private View anchor;
    @Mode
    private int mode;
    private int distance2triangleAngle;
    private int containerOff;

    public TriangleDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //窗口之后的内容变暗。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triangle_dialog);
        triangleContainer = (TriangleContainer) findViewById(R.id.triangle_container);
        recyclerContent = (RecyclerView) findViewById(R.id.recycler_content);

        recyclerContent.setHasFixedSize(true);
        recyclerContent.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContent.setAdapter(createAdapter());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int[] location = new int[2];
            anchor.getLocationInWindow(location);

            int triangleWidth = getTriangleWidth();
            int triangleHeight = getTriangleHeight();
            int off = 0;
            int round = getContainerRound();
            int bar = StatusBarHelper.getStatusBarHeight(getContext());

            int minX;
            int maxX;
            int minY;
            int maxY;
            int newX = 0;
            int newY = 0;

            int viewW = getWindow().getDecorView().getMeasuredWidth();
            int viewH = getWindow().getDecorView().getMeasuredHeight();

            int windowW = getContext().getResources().getDisplayMetrics().widthPixels;
            int windowH = getContext().getResources().getDisplayMetrics().heightPixels;

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.gravity = Gravity.LEFT | Gravity.TOP;
            switch (mode) {
                //三角箭头方向
                case LEFT:
                    minY = location[1] + anchor.getMeasuredHeight() / 2 + triangleWidth / 2 + round - viewH - bar;
                    maxY = location[1] + anchor.getMeasuredHeight() / 2 - triangleWidth / 2 - round - bar;
                    minY = minY < 0 ? 0 : minY;
                    maxY = maxY > windowH ? windowH : maxY;

                    newX = location[0] + anchor.getMeasuredWidth() + distance2triangleAngle;
                    newX = newX < 0 ? 0 : newX;
                    newX = newX > windowW ? windowW : newX;
                    newY = location[1] + containerOff - round - bar;
                    newY = newY < minY ? minY : newY;
                    newY = newY > maxY ? maxY : newY;
                    //修正
                    containerOff = newY - location[1] + round + bar;
                    off = (anchor.getMeasuredHeight() - triangleWidth) / 2 - containerOff;
                    break;
                case TOP:
                    minX = location[0] + anchor.getMeasuredWidth() / 2 + triangleWidth / 2 + round - viewW;
                    maxX = location[0] + anchor.getMeasuredWidth() / 2 - triangleWidth / 2 - round;
                    minX = minX < 0 ? 0 : minX;
                    maxX = maxX > windowW ? windowW : maxX;

                    newX = location[0] + containerOff - round;
                    newX = newX < minX ? minX : newX;
                    newX = newX > maxX ? maxX : newX;
                    newY = location[1] + anchor.getMeasuredHeight() + distance2triangleAngle - bar;
                    newY = newY < 0 ? 0 : newY;
                    newY = newY > windowH ? windowH : newY;
                    //修正
                    containerOff = newX - location[0] + round;
                    off = (anchor.getMeasuredWidth() - triangleWidth) / 2 - containerOff;
                    break;
                case RIGHT:
                    minY = location[1] + anchor.getMeasuredHeight() / 2 + triangleWidth / 2 + round - viewH - bar;
                    maxY = location[1] + anchor.getMeasuredHeight() / 2 - triangleWidth / 2 - round - bar;
                    minY = minY < 0 ? 0 : minY;
                    maxY = maxY > windowH ? windowH : maxY;

                    newX = location[0] - viewW - distance2triangleAngle;
                    if (0 == triangleContainer.getPaddingRight()) {
                        //未重新layout
                        newX = newX - triangleHeight;
                    }
                    newX = newX < 0 ? 0 : newX;
                    newX = newX > windowW ? windowW : newX;
                    newY = location[1] + containerOff - round - bar;
                    newY = newY < minY ? minY : newY;
                    newY = newY > maxY ? maxY : newY;
                    //修正
                    containerOff = newY - location[1] + round + bar;
                    off = (anchor.getMeasuredHeight() - triangleWidth) / 2 - containerOff;
                    break;
                case BOTTOM:
                    minX = location[0] + anchor.getMeasuredWidth() / 2 + triangleWidth / 2 + round - viewW;
                    maxX = location[0] + anchor.getMeasuredWidth() / 2 - triangleWidth / 2 - round;
                    minX = minX < 0 ? 0 : minX;
                    maxX = maxX > windowW ? windowW : maxX;

                    newX = location[0] + containerOff - round;
                    newX = newX < minX ? minX : newX;
                    newX = newX > maxX ? maxX : newX;
                    newY = location[1] - viewH - distance2triangleAngle - bar;
                    if (0 == triangleContainer.getPaddingBottom()) {
                        //未重新layout
                        newY = newY - triangleHeight;
                    }
                    newY = newY < 0 ? 0 : newY;
                    newY = newY > windowH ? windowH : newY;
                    //修正
                    containerOff = newX - location[0] + round;
                    off = (anchor.getMeasuredWidth() - triangleWidth) / 2 - containerOff;
                    break;
            }

            params.x = newX;
            params.y = newY;
            getWindow().setAttributes(params);
            triangleContainer.init(mode,
                    triangleWidth,
                    triangleHeight,
                    off,
                    round,
                    getContainerColor());
        }
    }

    /**
     * 显示
     *
     * @param anchor                 锚点View，对应居中三角形
     * @param mode                   三角形朝向
     * @param distance2triangleAngle 三角形和锚点的间距
     * @param containerOff           Dialog的偏移
     */
    public void show(View anchor, @Mode int mode, int distance2triangleAngle,
                     int containerOff) {
        this.anchor = anchor;
        this.mode = mode;
        this.distance2triangleAngle = distance2triangleAngle;
        this.containerOff = containerOff;
        show();
    }

    public abstract RecyclerView.Adapter createAdapter();

    protected int getTriangleWidth() {
        return DensityUtil.dp2px(12);
    }

    protected int getTriangleHeight() {
        return DensityUtil.dp2px(6);
    }

    protected int getContainerRound() {
        return DensityUtil.dp2px(4);
    }

    protected int getContainerColor() {
        return Color.WHITE;
    }
}
