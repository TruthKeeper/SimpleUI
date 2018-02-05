package com.tk.simpleui.bar.more.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.bar.BarUtils;
import com.tk.simpleui.common.DensityUtil;

/**
 * Created by TK on 2017/1/1.
 */

public class IndexFragment extends Fragment {
    private Toolbar toolbar;
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
        height = DensityUtil.dp2px(getContext(), 200);

        scrollView =  view.findViewById(R.id.scrollView);
        toolbar =  view.findViewById(R.id.tab_layout);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= height && ((ColorDrawable) toolbar.getBackground()).getColor() != colorPrimary) {
                    BarUtils.setTranslucentOffset(getActivity(), toolbar, colorPrimary, 255);
                    toolbar.setBackgroundColor(colorPrimary);
                } else if (scrollY < height) {
                    int alpha = (int) (scrollY * 255f / height);
                    BarUtils.setTranslucentOffset(getActivity(),
                            toolbar, colorPrimary, alpha);
                    toolbar.setBackgroundColor(BarUtils.calculateNewColor(colorPrimary, alpha));
                }
            }
        });
    }

    public void refreshLayout() {
        int alpha = (int) (scrollView.getScrollY() * 255f / height);
        BarUtils.setTranslucentOffset(getActivity(), toolbar,
                getResources().getColor(R.color.colorPrimary), alpha > 255 ? 255 : alpha);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarUtils.setTranslucentOffset(getActivity(), toolbar, colorPrimary, 0);
    }

}
