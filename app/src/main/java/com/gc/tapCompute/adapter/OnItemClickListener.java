package com.gc.tapCompute.adapter;

/**
 * 列表点击事件
 */
public interface OnItemClickListener {
    /**
     * item的 点击事件
     *
     * @param position:
     * @param itemId:
     */
    void onItemClick(int position, long itemId);

}