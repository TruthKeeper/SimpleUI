package com.tk.simpleui.statusbar.more.two;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2017/1/4.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fmList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fmList) {
        super(fm);
        this.fmList = fmList;
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }
}
