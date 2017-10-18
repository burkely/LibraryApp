package com.lydia.digitallibrary.Helper;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.animation.AnticipateInterpolator;

/**
 * Created by Lydia on 08/07/2016.
 */

public class ItemViewTransition extends TransitionSet {
    public ItemViewTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform()).
                setDuration(600).
                setInterpolator(new AnticipateInterpolator());
    }
}