package com.tk.simpleui.bar.more.drawerlayout;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.tk.simpleui.R;
import com.tk.simpleui.bar.BarUtils;

/**
 * Created by TK on 2017/1/4.
 */

public class DrawerOneActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView menuView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_drawer_one);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuView = (NavigationView) findViewById(R.id.menu_view);
        menuView.setItemIconTintList(null);

        BarUtils.setStatusBarColorInDrawer(this, drawerLayout, 0xff6262);
    }

    public void open(View v) {
        drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
}
