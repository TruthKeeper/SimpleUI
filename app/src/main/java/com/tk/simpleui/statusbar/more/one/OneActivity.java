package com.tk.simpleui.statusbar.more.one;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.statusbar.StatusBarHelper;

/**
 * Created by TK on 2017/1/2.
 */

public class OneActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private FrameLayout container;
    private RadioGroup tabGroup;

    private IndexFragment indexFragment;
    private DiscoverFragment discoverFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_one_main);
        StatusBarHelper.setTranslucent(this, Color.WHITE);
        container = (FrameLayout) findViewById(R.id.container);
        tabGroup = (RadioGroup) findViewById(R.id.tab_group);

        indexFragment = new IndexFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, indexFragment)
                .commitAllowingStateLoss();

        tabGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        switch (checkedId) {
            case R.id.tab_index:
                hide(ft);
                show(0, ft);
                ft.commitAllowingStateLoss();
                indexFragment.refreshLayout();
                break;
            case R.id.tab_discover:
                hide(ft);
                show(1, ft);
                ft.commitAllowingStateLoss();
                StatusBarHelper.setStatusBarColorInFragment(this, discoverFragment, getResources().getColor(R.color.colorPrimary));

                break;
            case R.id.tab_mine:
                hide(ft);
                show(2, ft);
                ft.commitAllowingStateLoss();
                StatusBarHelper.setStatusBarColorInFragment(this, mineFragment, 0xff6262);
                break;
        }
    }


    private void hide(FragmentTransaction ft) {
        if (indexFragment != null) {
            ft.hide(indexFragment);
        }
        if (discoverFragment != null) {
            ft.hide(discoverFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }

    private void show(int i, FragmentTransaction ft) {
        switch (i) {
            case 0:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    ft.add(R.id.container, indexFragment);
                } else {
                    ft.show(indexFragment);
                }
                break;
            case 1:
                if (discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                    ft.add(R.id.container, discoverFragment);
                } else {
                    ft.show(discoverFragment);
                }
                break;
            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    ft.add(R.id.container, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                break;
        }
    }
}
