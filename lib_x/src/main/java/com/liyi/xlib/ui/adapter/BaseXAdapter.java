package com.liyi.xlib.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class BaseXAdapter<VH extends BaseXHolder> extends BaseAdapter {

    /**
     * 创建 ViewHolder
     */
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定 item 的 ViewHolder
     */
    public abstract void onBindViewHolder(VH holder, int position);

    /**
     * 获取 item 的数量
     */
    public abstract int getItemCount();

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        VH holder = null;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, viewType);
            convertView = holder.getConvertView();
            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }
}
