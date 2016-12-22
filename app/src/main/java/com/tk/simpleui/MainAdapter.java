package com.tk.simpleui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TK on 2016/12/20.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ItemHolder> {
    private Context mContext;
    private List<Item> mList;
    private LayoutInflater mInflater;

    public MainAdapter(Context mContext, List<Item> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(mInflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ((TextView) holder.itemView).setText(mList.get(position).getContent()
                + (mList.get(position).isDone() ? "\u3000√" : "\u3000_(:з」∠)_"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(mList.get(getAdapterPosition()).getIntent());
                }
            });
        }
    }

}
