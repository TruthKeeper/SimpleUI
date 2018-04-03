package com.tk.simpleui.triangledialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tk.simpleui.R;
import com.tk.simpleui.triangledialog.dialog.TriangleDialog;

/**
 * Created by TK on 2017/8/15.
 */

public class TriangleDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle_dialog);
    }

    public void show(View view) {
        new TriangleDialog.Builder(this)
//                .offX()
//                .offY()
//                .containerColor()
//                .containerOffsetPercent()
//                .paddingLeft()
//                .paddingTop()
//                .paddingRight()
//                .paddingBottom()
                .direction(TriangleDialog.Direction.TOP)
                .adapter(new RecyclerView.Adapter() {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_triangle_dialog, parent, false)) {
                        };
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(R.id.item)).setText("第" + position + "个条目");
                    }

                    @Override
                    public int getItemCount() {
                        return 5;
                    }
                })
                .build()
                .show(view);
    }

}
