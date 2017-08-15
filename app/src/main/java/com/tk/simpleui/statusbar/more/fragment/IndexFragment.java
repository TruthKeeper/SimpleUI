package com.tk.simpleui.statusbar.more.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tk.simpleui.R;
import com.tk.simpleui.common.DensityUtil;
import com.tk.simpleui.statusbar.StatusBarHelper;

/**
 * Created by TK on 2017/1/1.
 */

public class IndexFragment extends Fragment {
    private LinearLayout tabLayout;
    private NestedScrollView scrollView;

    private int height;
    private int colorPrimary;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_one_index, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        height = DensityUtil.dp2px(200);

        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        tabLayout = (LinearLayout) view.findViewById(R.id.tab_layout);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= height && ((ColorDrawable) tabLayout.getBackground()).getColor() != colorPrimary) {
                    StatusBarHelper.setTranslucentOffset(getActivity(), tabLayout, colorPrimary, 255);
                    tabLayout.setBackgroundColor(colorPrimary);
                } else if (scrollY < height) {
                    int alpha = (int) (scrollY * 255f / height);
                    StatusBarHelper.setTranslucentOffset(getActivity(),
                            tabLayout, colorPrimary, alpha);
                    tabLayout.setBackgroundColor(StatusBarHelper.calculateNewColor(colorPrimary, alpha));
                }
            }
        });
    }

    public void refreshLayout() {
        int alpha = (int) (scrollView.getScrollY() * 255f / height);
        StatusBarHelper.setTranslucentOffset(getActivity(),
                tabLayout,
                ContextCompat.getColor(getContext(), R.color.colorPrimary),
                alpha > 255 ? 255 : alpha);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarHelper.setTranslucentOffset(getActivity(), tabLayout, colorPrimary, 0);
    }

}
