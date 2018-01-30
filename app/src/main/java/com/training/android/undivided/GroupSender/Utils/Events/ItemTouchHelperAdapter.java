package com.training.android.undivided.GroupSender.Utils.Events;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
