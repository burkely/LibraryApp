package com.lydia.digitallibrary.Helper;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Lydia on 10/07/2016.
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    private Context ccontext;

    //Because we are defining this behavior statically within the XML,
    // we must also implement constructor to enable layout inflation
    // to work correctly.
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        ccontext = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        if ((dyConsumed > 0 || dyConsumed < 0  ) && child.getVisibility() == View.VISIBLE) {
            child.hide();
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                   View target){
        super.onStopNestedScroll(coordinatorLayout, child, target);
        child.show();
    }
}