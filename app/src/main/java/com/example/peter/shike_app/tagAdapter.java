package com.example.peter.shike_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/6/13.
 */

public class tagAdapter extends BaseAdapter {
    private List<String> mString;
    private Context mContext;

    public tagAdapter(List<String> mString, Context mContext) {
        this.mContext = mContext;
        this.mString = mString;
    }

    @Override
    public int getCount() {
        return mString.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txt_item_title = (TextView) convertView.findViewById(R.id.txt_item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_item_title.setText(mString.get(position).toString());

        return convertView;
    }

    private class ViewHolder {
        TextView txt_item_title;
    }
}
