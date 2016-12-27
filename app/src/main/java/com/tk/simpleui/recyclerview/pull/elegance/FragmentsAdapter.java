package com.tk.simpleui.recyclerview.pull.elegance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by TK on 2016/12/25.
 */

public class FragmentsAdapter extends FragmentPagerAdapter {
    private List<Fragment> fmList;
    private List<String> titleList;

    public FragmentsAdapter(FragmentManager fm, List<Fragment> fmList, List<String> titleList) {
        super(fm);
        this.fmList = fmList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
