package com.training.android.undivided.GroupSender.Utils.Events;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public abstract class OnRecyclerItemClickListener  implements RecyclerView.OnItemTouchListener  {

    private RecyclerView mRecyclerView;
    private GestureDetectorCompat mGestureDetector;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(
                recyclerView.getContext(), new ItemGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public class ItemGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (null != child) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (null != child) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
                onItemLongClick(vh);
            }
        }
    }

    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    public abstract void onItemLongClick(RecyclerView.ViewHolder vh);
}
