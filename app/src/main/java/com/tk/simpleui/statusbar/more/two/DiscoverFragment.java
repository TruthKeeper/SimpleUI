package com.tk.simpleui.statusbar.more.two;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.statusbar.StatusBarHelper;

/**
 * Created by TK on 2017/1/4.
 */

public class DiscoverFragment extends Fragment {
    private Toolbar bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_two_discover, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bar = (Toolbar) view.findViewById(R.id.bar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarHelper.setStatusBarColorInFragment(getActivity(), this, android.R.color.holo_orange_light);
    }

}
