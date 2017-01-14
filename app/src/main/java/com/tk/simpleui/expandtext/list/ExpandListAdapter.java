package com.tk.simpleui.expandtext.list;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tk.simpleui.R;
import com.tk.simpleui.expandtext.ExpandableTextView;

/**
 * Created by TK on 2017/1/14.
 */

public class ExpandListAdapter extends RecyclerView.Adapter<ExpandListAdapter.ItemHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expandabletext_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        ExpandableTextView view = (ExpandableTextView) holder.itemView;
        view.setContent("第" + position + "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
                        + "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
                        + "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
                        + "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
                        + "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈",
                array.get(position, false));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ExpandableTextView view;

        public ItemHolder(View itemView) {
            super(itemView);
            view = (ExpandableTextView) itemView;
            view.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onStatusChange(int status) {
                    if (status == ExpandableTextView.STATUS_EXPAND) {
                        array.put(getAdapterPosition(), true);
                    } else {
                        array.delete(getAdapterPosition());
                    }
                }
            });
        }
    }
}
