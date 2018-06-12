package com.example.peter.shike_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MyFragment1 extends Fragment {

    private Button locbtn = null;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private int count = 0;
    private static final int msgKey1 = 1;
    private FrameLayout buttomfl;
    private Button tobuttom;
    private View nothing;

    private Button recommend;
    private ListView recommend_listview;
    private Button recommendret;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content1, container, false);
        mContext = getActivity();

        recommend = (Button)view.findViewById(R.id.recommend);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.myAdapter = new MyAdapter(PreferenceUtil.dishDatas, getActivity());
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                View view_custom = inflater.inflate(R.layout.list_recommend, null, false);
                recommend_listview = (ListView)view_custom.findViewById(R.id.recommend_listview);
                recommend_listview.setAdapter(PreferenceUtil.myAdapter);
                recommend_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bd = new Bundle();
                        int dishID = 0;
                        dishID = PreferenceUtil.dishDatas.get(position).getID();
                        bd.putInt("dishID", dishID);
                        bd.putInt("which", 0);
                        Intent it = new Intent(getActivity(), dishActivity.class);
                        it.putExtras(bd);
                        startActivity(it);
                    }
                });
                PreferenceUtil.dishDatas.clear();
                getDishAsyncPHPClientPost();
                recommendret = (Button)view_custom.findViewById(R.id.recommendret);
                recommendret.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                builder.setView(view_custom);
                alert = builder.create();
                alert.show();
            }
        });

        return view;
    }

    public void onStart(){
        super.onStart();
    }

    public void onDestroy(){
        super.onDestroy();
    }
    public void onResume(){
        super.onResume();
    }
    public void onPause(){
        super.onPause();
    }

    private void getDishAsyncPHPClientPost() {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://ch.huyunfan.cn/PHP/recommend/recommend.php";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PreferenceUtil.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //将参数加入到参数对象中
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //进行post请求
        client.post(mContext, url, entity, "application/json", new JsonHttpResponseHandler() {
            //如果成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int status = response.getInt("status");
                    if (status == 1) {
                        Toast.makeText(mContext, "status code is:"+ statusCode+ "\n更新失败!\n", Toast.LENGTH_LONG).show();
                    }
                    else if(status == 0) {
                        //Toast.makeText(mContext, response.toString(), Toast.LENGTH_LONG).show();
                        int count = response.getInt("dishNum");
                        if (count > 0) {
                            JSONArray dishList = response.getJSONArray("dishList");
                            //Toast.makeText(mContext, dishList.toString(), Toast.LENGTH_LONG).show();
                            for (int i = 0; i < count; i ++) {
                                JSONObject temp = dishList.getJSONObject(i);
                                Dish dish = new Dish();
                                dish.setID(temp.getInt("dishID"));
                                dish.setCanteenID(temp.getInt("canteenID"));
                                dish.setName(temp.getString("dishName"));
                                dish.setPictureURL(temp.getString("photo"));
                                dish.setRating(temp.getDouble("rating"));
                                dish.setPublisherName(temp.getString("userName"));
                                PreferenceUtil.dishDatas.add(dish);
                            }
                            PreferenceUtil.myAdapter.notifyDataSetChanged();
                        }
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }
}