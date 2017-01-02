package com.tk.simpleui.statusbar.more.one;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private ArgbEvaluator evaluator;
    private int h;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_one_index, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        evaluator = new ArgbEvaluator();
        h = DensityUtil.dp2px(getContext(), 200);

        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        tabLayout = (LinearLayout) view.findViewById(R.id.tab_layout);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int color = getResources().getColor(R.color.colorPrimary);
                if (scrollY >= h && ((ColorDrawable) tabLayout.getBackground()).getColor() != color) {
                    StatusBarHelper.setTranslucentInFragment(getActivity(), tabLayout, color, 255);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (scrollY < h) {
                    StatusBarHelper.setTranslucentInFragment(getActivity(),
                            tabLayout, color, (int) (scrollY * 255f / h));
                    tabLayout.setBackgroundColor(StatusBarHelper.calculateNewColor(color, (int) (scrollY * 255f / h)));
                }
            }
        });
    }

    public void refreshLayout() {
        if (tabLayout != null) {
            int a = (int) (scrollView.getScrollY() * 255f / h);
            StatusBarHelper.setTranslucentInFragment(getActivity(), tabLayout,
                    getResources().getColor(R.color.colorPrimary), a > 255 ? 255 : a);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarHelper.setTranslucentInFragment(getActivity(), tabLayout, getResources().getColor(R.color.colorPrimary), 0);
    }

}
