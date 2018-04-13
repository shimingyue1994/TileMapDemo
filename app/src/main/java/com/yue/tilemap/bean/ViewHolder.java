package com.yue.tilemap.bean;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}

/*
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) { if(convertView == null) { convertView =
 * LayoutInflater.from(context).inflate(R.layout.singele_list_item_view, null);
 * } TextView contentTv = (TextView)ViewHolder.get(convertView,
 * R.id.tv_content); ImageView iconIv = (ImageView)ViewHolder.get(convertView,
 * R.id.iv_icon); contentTv.setText(items[position]); if(position ==
 * currentPosition) { iconIv.setImageResource(R.drawable.btn_radio_on_light);
 * contentTv
 * .setTextColor(context.getResources().getColor(R.color.txt_color_orange)); }
 * else { iconIv.setImageResource(R.drawable.btn_radio_off_light);
 * contentTv.setTextColor(Color.BLACK); } return convertView; }
 */