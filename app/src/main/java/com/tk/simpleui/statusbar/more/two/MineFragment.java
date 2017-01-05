package com.tk.simpleui.statusbar.more.two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tk.simpleui.R;

/**
 * Created by TK on 2017/1/4.
 */

public class MineFragment extends Fragment {
    private TextView tabTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_two_mine, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tabTitle = (TextView) view.findViewById(R.id.tab_title);

    }


}
