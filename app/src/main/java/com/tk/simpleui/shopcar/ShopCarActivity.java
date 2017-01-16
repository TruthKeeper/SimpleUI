package com.tk.simpleui.shopcar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tk.simpleui.R;

/**
 * Created by TK on 2016/12/30.
 */

public class ShopCarActivity extends AppCompatActivity {
    private KeepChangeLayout changeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);

        changeLayout = (KeepChangeLayout) findViewById(R.id.change_layout);

    }

    public void confirm(View view) {
        Toast.makeText(this, changeLayout.getNowSum() + "", Toast.LENGTH_SHORT).show();
    }
}
