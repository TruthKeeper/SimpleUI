package com.tk.simpleui.statusbar.more.two;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.statusbar.StatusBarHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TK on 2017/1/4.
 */

public class TwoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ViewPager viewpager;
    private RadioGroup tabGroup;
    private RadioButton tabIndex;
    private RadioButton tabDiscover;
    private RadioButton tabMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_two_main);
        StatusBarHelper.setTranslucent(this, Color.WHITE);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabGroup = (RadioGroup) findViewById(R.id.tab_group);
        tabIndex = (RadioButton) findViewById(R.id.tab_index);
        tabDiscover = (RadioButton) findViewById(R.id.tab_discover);
        tabMine = (RadioButton) findViewById(R.id.tab_mine);


        List<Fragment> fmList = new ArrayList<>();
        fmList.add(new IndexFragment());
        fmList.add(new DiscoverFragment());
        fmList.add(new MineFragment());

        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fmList));
        viewpager.addOnPageChangeListener(this);
        tabGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_index:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tab_discover:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tab_mine:
                viewpager.setCurrentItem(2);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                if (!tabIndex.isChecked()) {
                    tabIndex.setChecked(true);
                }
                break;
            case 1:
                if (!tabDiscover.isChecked()) {
                    tabDiscover.setChecked(true);
                }
                break;
            case 2:
                if (!tabMine.isChecked()) {
                    tabMine.setChecked(true);
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
