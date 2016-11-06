package com.mrd.todo.Screens;

/**
 * Created by mayurdube on 11/08/16.
 */
public interface OnItemInteractionListener {
    public void onLongClick(int position, Object show);
    public void onClick(int position, Object show);
}
