package com.example.peter.shike_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Dish> mDish;
    private Context mContext;

    public MyAdapter(List<Dish> mDish, Context mContext) {
        this.mContext = mContext;
        this.mDish = mDish;
    }

    @Override
    public int getCount() {
        return mDish.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dish_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dish_name = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.dish_picture = (ImageView) convertView.findViewById(R.id.dish_picture);
            viewHolder.dish_description = (TextView) convertView.findViewById(R.id.dish_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dish_name.setText(mDish.get(position).getName());
        if (mDish.get(position).getPictureURL() != "") {
            Picasso.with(mContext)
                    .load("http://"+mDish.get(position).getPictureURL())
                    .fit()
                    .into(viewHolder.dish_picture);
        }
        viewHolder.dish_description.setText("23333!");
        return convertView;
    }

    private class ViewHolder {
        ImageView dish_picture;
        TextView dish_name;
        TextView dish_description;
    }
}
