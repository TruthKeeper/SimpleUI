package com.tk.simpleui.statusbar.more.one;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;

/**
 * Created by TK on 2017/1/1.
 */

public class DiscoverFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_one_discover, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }


}
