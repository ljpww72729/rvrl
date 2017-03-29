package com.ww.lp.rvrl_lib;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import static com.ww.lp.rvrl_lib.LPRecyclerViewAdapter.TYPE_ITEM;

/**
 * Created by LinkedME06 on 16/11/12.
 */

public class SingleItemClickListener implements RecyclerView.OnItemTouchListener {


    private OnItemClickListener clickListener;
    private GestureDetector gestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public SingleItemClickListener(final RecyclerView recyclerView,
                                   OnItemClickListener listener) {
        this.clickListener = listener;
        gestureDetector = new GestureDetector(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null && clickListener != null) {
                            if (recyclerView.getChildViewHolder(childView).getItemViewType() == TYPE_ITEM) {
                                int actualPostion = recyclerView.getChildAdapterPosition(childView);
                                if (((LPRecyclerViewAdapter)recyclerView.getAdapter()).getmHeaderBinding() != null) {
                                    actualPostion -= 1;
                                }
                                clickListener.onItemClick(childView, actualPostion);
                            }
                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null && clickListener != null) {
                            clickListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}