package com.example.sy.myapplication.Utils.swipe;

import android.widget.ListView;

public class SwipeListUtil extends SwipeDismissListViewTouchListener {
    /**
     * Constructs a new swipe-to-dismiss touch listener for the given list view.
     *
     * @param listView  The list view whose items should be dismissable.
     * @param callbacks The callback to trigger when the user has indicated that she would like to
     */
    public SwipeListUtil(ListView listView, DismissCallbacks callbacks) {
        super(listView, callbacks);
    }

}
