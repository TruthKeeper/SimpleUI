package com.tk.simpleui.flow;

import android.database.Observable;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TK on 2016/11/5.
 */

public abstract class FlowAdapter<T> {
    protected List<T> mList;
    private AdapterDataObservable mObservable;

    public FlowAdapter(List<T> mList) {
        this.mList = mList;
        this.mObservable = new AdapterDataObservable();
    }

    public void registerDataSetObserver(FlowLayout.AdapterDataObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(FlowLayout.AdapterDataObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mObservable.notifyChanged();
    }

    public FlowAdapter(T[] mList) {
        this.mList = new ArrayList<>(Arrays.asList(mList));
    }

    public int getItemCount() {
        return mList.size();
    }

    public List<T> getList() {
        return mList;
    }

    public abstract View getView(ViewGroup parent, int position);

    public static class AdapterDataObservable extends Observable<FlowLayout.AdapterDataObserver> {

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

}
