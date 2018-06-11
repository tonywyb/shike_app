package com.example.peter.shike_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment2 extends Fragment {

    private FragmentManager fManager = null;
    private Button addbtn = null;
    private Context mContext = null;
    private TextView canteen = null;
    private TextView title = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content2, container, false);
        mContext = getActivity();
        fManager = getFragmentManager();
        addbtn = (Button) view.findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtil.islogged)
                    startActivity(new Intent(getActivity(), NewDish.class));
                else
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        });
        title = (TextView) view.findViewById(R.id.txt_title);
        canteen = (TextView) view.findViewById(R.id.can);
        registerForContextMenu(canteen);

        ListFragment nlFragment = new ListFragment(0, PreferenceUtil.canteenID[0]);
        FragmentTransaction ft = fManager.beginTransaction();
        ft.replace(R.id.fl_content, nlFragment);
        ft.commit();
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflator = getActivity().getMenuInflater();
        inflator.inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ListFragment nlFragment = null;
        switch (item.getItemId()) {
            case R.id.xueyi:
                title.setText("学一食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[0]);
                break;
            case R.id.xuewu:
                title.setText("学五食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[1]);
                break;
            case R.id.yiyuan1:
                title.setText("艺园一楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[2]);
                break;
            case R.id.yiyuan2:
                title.setText("艺园二楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[3]);
                break;
            case R.id.nongyuan1:
                title.setText("农园一楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[4]);
                break;
            case R.id.nongyuan2:
                title.setText("农园二楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[5]);
                break;
            case R.id.nongyuan3:
                title.setText("农园三楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[6]);
                break;
            case R.id.shaoyuan1:
                title.setText("勺园一楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[7]);
                break;
            case R.id.shaoyuan2:
                title.setText("勺园二楼");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[8]);
                break;
            case R.id.yannan:
                title.setText("燕南食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[9]);
                break;
            case R.id.tongyuan:
                title.setText("佟园食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[10]);
                break;
            case R.id.changchun:
                title.setText("畅春园食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[11]);
                break;
            case R.id.yixuebu:
                title.setText("医学部食堂");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[12]);
                break;
            case R.id.songlin:
                title.setText("松林包子");
                nlFragment = new ListFragment(0, PreferenceUtil.canteenID[13]);
                break;
        }
        FragmentTransaction ft = fManager.beginTransaction();
        ft.replace(R.id.fl_content, nlFragment);
        ft.commit();
        return true;
    }
}
