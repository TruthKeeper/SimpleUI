package com.tk.simpleui.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tk.simpleui.R;

import java.util.ArrayList;
import java.util.List;


public class FlowActivity extends AppCompatActivity implements View.OnClickListener {


    private FlowLayout flowLayout1;
    private Button add1;
    private Button remove1;
    private FlowLayout flowLayout2;
    private Button add2;
    private Button remove2;


    private List<String> list = new ArrayList<String>();
    private List<String> list1 = new ArrayList<String>();
    private List<String> list2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        initView();
        list.add("英俊潇洒");
        list.add("风流倜傥");
        list.add("一朵梨花压海棠");
        list.add("玉树临风胜潘安");
        list.add("语死早");
        list.add("没东西编了");
        list.add("(╯‵□′)╯︵┻━┻");
        list.add("哈哈哈哈");
        list.add("end");
        list1.addAll(list);
        list2.addAll(list);
        flowLayout1.setAdapter(new FlowAdapter<String>(list1) {
            @Override
            public View getView(ViewGroup parent, int position) {
                TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.flow_item_1, parent, false);
                view.setText(mList.get(position));
                return view;
            }
        });
        flowLayout2.setAdapter(new FlowAdapter<String>(list2) {
            @Override
            public View getView(ViewGroup parent, final int position) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flow_item_2, parent, false);
                ((TextView) view.findViewById(R.id.tag)).setText(mList.get(position));

                return view;
            }
        });
    }


    private void initView() {
        flowLayout1 = (FlowLayout) findViewById(R.id.flow_layout_1);
        add1 = (Button) findViewById(R.id.add_1);
        remove1 = (Button) findViewById(R.id.remove_1);
        flowLayout2 = (FlowLayout) findViewById(R.id.flow_layout_2);
        add2 = (Button) findViewById(R.id.add_2);
        remove2 = (Button) findViewById(R.id.remove_2);
        add1.setOnClickListener(this);
        remove1.setOnClickListener(this);
        add2.setOnClickListener(this);
        remove2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_1:
                list1.add("想不出来了");
                flowLayout1.getAdapter().notifyDataSetChanged();
                break;
            case R.id.remove_1:
                list1.remove(list1.size() - 1);
                flowLayout1.getAdapter().notifyDataSetChanged();
                break;
            case R.id.add_2:
                list2.add("想不出来了");
                flowLayout2.getAdapter().notifyDataSetChanged();
                break;
            case R.id.remove_2:
                list2.remove(list2.size() - 1);
                flowLayout2.getAdapter().notifyDataSetChanged();
                break;
        }
    }
}
