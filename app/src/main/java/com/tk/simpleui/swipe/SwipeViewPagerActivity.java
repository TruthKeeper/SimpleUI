package com.tk.simpleui.swipe;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;

/**
 * <pre>
 *      author : TK
 *      time : 2017/10/11
 *      desc :
 * </pre>
 */
public class SwipeViewPagerActivity extends AppCompatActivity {

    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_viewpager);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.activity_swipe_list, container, false);
                RecyclerView recyclerContent = (RecyclerView) view.findViewById(R.id.recycler_content);
                recyclerContent.setHasFixedSize(true);
                recyclerContent.setLayoutManager(new LinearLayoutManager(container.getContext()));
                recyclerContent.setAdapter(new RecyclerView.Adapter<SwipeListActivity.Holder>() {


                    @Override
                    public SwipeListActivity.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new SwipeListActivity.Holder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_swipe_list, parent, false));
                    }

                    @Override
                    public void onBindViewHolder(SwipeListActivity.Holder holder, int position) {
                        //偷懒2333
                        holder.swipeLayout.resetPosition(false);
                    }

                    @Override
                    public int getItemCount() {
                        return 20;
                    }
                });
                container.addView(view);
                return view;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        viewpager.setOffscreenPageLimit(3);
    }


}
