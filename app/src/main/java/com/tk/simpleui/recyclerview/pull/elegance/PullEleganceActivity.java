package com.tk.simpleui.recyclerview.pull.elegance;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tk.simpleui.R;

import java.util.ArrayList;
import java.util.List;


public class PullEleganceActivity extends AppCompatActivity {

    private CollapsingToolbarLayout toolbarlayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;

    private List<Fragment> fmList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_elegance);
        initView();
    }

    private void initView() {
        toolbarlayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fmList.add(new TextFragment());
        fmList.add(new TextFragment());
        fmList.add(new TextFragment());
        titleList.add("First");
        titleList.add("Second");
        titleList.add("Third");
        for (int i = 0; i < fmList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }
        viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager(), fmList, titleList));
        tabLayout.setupWithViewPager(viewpager);
    }


}
