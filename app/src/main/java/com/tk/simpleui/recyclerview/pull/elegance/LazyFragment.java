package com.tk.simpleui.recyclerview.pull.elegance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by TK on 2016/10/3.
 */

public abstract class LazyFragment extends Fragment {
    private boolean onCreateView;
    private boolean hasLoad;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!onCreateView) {
            return;
        }
        if (isVisibleToUser && (!hasLoad)) {
            hasLoad = true;
            onFragmentVisibleChange(true);
        } else {
            onFragmentVisibleChange(false);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreateView = true;
        if (getUserVisibleHint()) {
            hasLoad = true;
            onFragmentVisibleChange(true);
        }
    }


    /**
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected abstract void onFragmentVisibleChange(boolean isVisible);

}

