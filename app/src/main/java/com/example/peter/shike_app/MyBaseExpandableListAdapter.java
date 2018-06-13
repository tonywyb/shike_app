package com.example.peter.shike_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> gData;
    private ArrayList<ArrayList<Tag>> iData;
    private Context mContext;

    public MyBaseExpandableListAdapter(ArrayList<String> gData, ArrayList<ArrayList<Tag>> iData, Context mContext) {
        this.gData = gData;
        this.iData = iData;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iData.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return gData.get(groupPosition);
    }

    @Override
    public Tag getChild(int groupPosition, int childPosition) {
        return iData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void select(int groupPosition, int childPosition) {
        if(!iData.get(groupPosition).get(childPosition).isSelected()) {
            iData.get(groupPosition).get(childPosition).setSelected(true);
            for(int i = 0; i < iData.get(groupPosition).size(); i ++) {
                if(i != childPosition)
                    iData.get(groupPosition).get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolderGroup groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.tag_item_group, parent, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tag_group_name = (TextView) convertView.findViewById(R.id.tag_group_name);
            convertView.setTag(groupHolder);
        }else{
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tag_group_name.setText(gData.get(groupPosition).toString());
        return convertView;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem itemHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.tag_item_child, parent, false);
            itemHolder = new ViewHolderItem();
            itemHolder.tag_name = (TextView) convertView.findViewById(R.id.tag_name);
            itemHolder.tag_check = ((RadioButton) convertView.findViewById(R.id.tag_check));
            //itemHolder.tag_check.setClickable(false);
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.tag_name.setText(iData.get(groupPosition).get(childPosition).getName());
        itemHolder.tag_check.setChecked(iData.get(groupPosition).get(childPosition).isSelected());
        return convertView;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolderGroup{
        private TextView tag_group_name;
    }

    private static class ViewHolderItem{
        private TextView tag_name;
        private RadioButton tag_check;
    }

}