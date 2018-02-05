package com.tk.simpleui.swipe;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tk.simpleui.R;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/11
 *      desc :
 * </pre>
 */
public class SwipeListActivity extends AppCompatActivity {

    private RecyclerView recyclerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_list);
        recyclerContent = (RecyclerView) findViewById(R.id.recycler_content);
        recyclerContent.setHasFixedSize(true);
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.setAdapter(new RecyclerView.Adapter<Holder>() {

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new Holder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_swipe_list, parent, false));
            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                //偷懒2333
                holder.swipeLayout.resetPosition(false);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
    }

    public static class Holder extends RecyclerView.ViewHolder implements SwipeLayout.OnSwipeListener, View.OnClickListener {
        SwipeLayout swipeLayout;

        public Holder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            swipeLayout.setOnSwipeListener(this);
            itemView.findViewById(R.id.btn_left).setOnClickListener(this);
            itemView.findViewById(R.id.btn_right).setOnClickListener(this);
            itemView.findViewById(R.id.btn_content).setOnClickListener(this);
        }

        @Override
        public void onSwipeOpen(@NonNull SwipeLayout view, @SwipeLayout.Direction int direction) {
            switch (direction) {
                case SwipeLayout.Direction.LEFT:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "左侧打开了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.TOP:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "上方打开了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.RIGHT:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "右侧打开了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.BOTTOM:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "下方打开了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onSwipeClose(@NonNull SwipeLayout view, @SwipeLayout.Direction int direction) {
            switch (direction) {
                case SwipeLayout.Direction.LEFT:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "左侧关闭了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.TOP:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "上方关闭了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.RIGHT:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "右侧关闭了", Toast.LENGTH_SHORT).show();
                    break;
                case SwipeLayout.Direction.BOTTOM:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "下方关闭了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onPositionChange(@NonNull SwipeLayout view, @IntRange(from = 0) int currentX, @IntRange(from = 0) int currentY) {

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_left:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "点击了左侧", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_right:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "点击了右侧", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_content:
                    Toast.makeText(itemView.getContext().getApplicationContext(), "点击了主体", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
