package com.example.peter.shike_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private ListView list_event;
    private int which;
    private int myCanteen;
    private Context mContext;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    public ListFragment() {}
    @SuppressLint("ValidFragment")
    public ListFragment(int which, int myCanteen) {
        this.myCanteen = myCanteen;
        this.which = which;
    }

    @SuppressLint("ValidFragment")
    public ListFragment(int which) {
        this.which = which;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventlist, container, false);
        //mContext = getActivity();
        list_event = (ListView) view.findViewById(R.id.list_event);

        if (which == 0) {
            PreferenceUtil.myAdapter = new MyAdapter(PreferenceUtil.dishDatas, getActivity());
            list_event.setAdapter(PreferenceUtil.myAdapter);
            PreferenceUtil.dishDatas.clear();
            getDishAsyncPHPClientPost();
            list_event.setOnItemClickListener(this);
            //list_event.setOnItemLongClickListener(this);
        }
        else if (which == 1) {
            /*
            PreferenceUtil.myAdapter2 = new MyAdapter(PreferenceUtil.mydatas, getActivity());
            list_event.setAdapter(PreferenceUtil.myAdapter2);
            list_event.setOnItemClickListener(this);
            list_event.setOnItemLongClickListener(this);
            */
        }
        if (which == 3) {
            /*
            Bundle bd = getArguments();
            PreferenceUtil.locdatas.clear();
            PreferenceUtil.myAdapterloc = new MyAdapter(PreferenceUtil.locdatas, getActivity());
            list_event.setAdapter(PreferenceUtil.myAdapterloc);
            getEventByLocationID(bd.getInt("locationID"), bd.getInt("type"));
            list_event.setOnItemClickListener(this);
            list_event.setOnItemLongClickListener(this);
            */
        }
        if (which == 4) {
            PreferenceUtil.myAdapterforComment = new MyAdapterforComment(PreferenceUtil.commentdatas, getActivity());
            list_event.setAdapter(PreferenceUtil.myAdapterforComment);
            PreferenceUtil.commentdatas.clear();
            Bundle bd = getArguments();
            getCommentByFather(bd.getInt("fatherID"));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bd = new Bundle();
        int eventID = 0;
        int dishID = 0;
        switch (which) {
            case 0:
                dishID = PreferenceUtil.dishDatas.get(position).getID();
                //eventID = PreferenceUtil.datas.get(position).getEventId();
                break;
            case 1:
                dishID = PreferenceUtil.myDishDatas.get(position).getID();
                //eventID = PreferenceUtil.mydatas.get(position).getEventId();
                break;
            case 3:
                dishID = PreferenceUtil.locDishdatas.get(position).getID();
                //eventID = PreferenceUtil.locdatas.get(position).getEventId();
                break;
        }
        bd.putInt("dishID", dishID);
        bd.putInt("which", which);
        Intent it = new Intent(getActivity(), dishActivity.class);
        it.putExtras(bd);
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (which == 1) {
            builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setCancelable(true);
            alert = builder.setMessage("是否确定删除？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEventAsyncHttpClientPost(PreferenceUtil.mydatas.get(position).getEventId());
                        }
                    }).create();
            alert.show();
        }
        return true;
    }

    private void deleteEventAsyncHttpClientPost(final int eventID) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://120.25.232.47:8002/deleteEventByID/";
        //String url = "http://www.baidu.com";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eventID", eventID);
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
                PreferenceUtil.deletebyID(eventID);
                PreferenceUtil.myAdapter.notifyDataSetChanged();
                PreferenceUtil.myAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_LONG).show();
            }
        });
        return;

    }

    private void getDishAsyncPHPClientPost() {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://ch.huyunfan.cn/PHP/dish/searchDish.php";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("canteenID", this.myCanteen);
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
                    int status = response.getInt("searchDishStatus");
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

    private void getEventByLocationID(final int locationID, final int type) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://120.25.232.47:8002/getEventByLocationID/";
        //String url = "http://www.baidu.com";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("locationID", locationID);
            jsonObject.put("type", type);
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
                    int status = response.getInt("getStatus");
                    if (status == 1) {
                        Toast.makeText(mContext, "status code is:"+ statusCode+ "\n更新失败!\n", Toast.LENGTH_LONG).show();
                    }
                    else if(status == 0) {
                        //Toast.makeText(mContext, response.toString(), Toast.LENGTH_LONG).show();
                        int count = response.getInt("eventNum");
                        if (count > 0) {
                            JSONArray events = response.getJSONArray("events");
                            //Toast.makeText(mContext, events.toString(), Toast.LENGTH_LONG).show();
                            for (int i = 0; i < count; i++) {
                                JSONObject temp = events.getJSONObject(i);
                                Event event = new Event();
                                event.setEventID(temp.getInt("eventID"));
                                //eventList[i].setBeginTime(temp.getString("beginTime"));
                                event.setDescription(temp.getString("description"));
                                //eventList[i].setEndTime(temp.getString("endTime"));
                                event.setOutdate(temp.getInt("outdate"));
                                event.setType(temp.getInt("type"));
                                if (event.getType() == 2) {
                                    event.setIshelped(temp.getInt("isHelped"));
                                    event.setHelper(temp.getInt("helperID"));
                                }
                                event.setPublisherID(temp.getInt("publisherID"));
                                event.setTitle(temp.getString("title"));
                                event.setUsername(temp.getString("username"));
                                PreferenceUtil.locdatas.add(event);
                            }
                            PreferenceUtil.myAdapterloc.notifyDataSetChanged();
                        }
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_LONG).show();
            }
        });
        return;

    }

    private void getCommentByFather(final int fatherID) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://ch.huyunfan.cn/PHP/dish/checkComments.php";
        //String url = "http://www.baidu.com";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dishID", fatherID);
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
                    int status = response.getInt("checkStatus");
                    if (status == 1) {
                        Toast.makeText(mContext, "status code is:"+ statusCode+ "\n更新失败!\n", Toast.LENGTH_LONG).show();
                    }
                    else if(status == 0) {
                        int count = response.getInt("commentNum");
                        if (count != 0) {
                            JSONArray comments = response.getJSONArray("commentList");
                            for (int i = 0; i < count; i++) {
                                JSONObject temp = comments.getJSONObject(i);
                                Comment comment = new Comment();
                                //comment.setCommentID(temp.getInt("commentID"));
                                comment.setContent(temp.getString("comment"));
                                comment.setFatherID(fatherID);
                                comment.setPublisherID(temp.getInt("userID"));
                                comment.setUsername(temp.getString("userName"));
                                PreferenceUtil.commentdatas.add(comment);
                            }
                            PreferenceUtil.myAdapterforComment.notifyDataSetChanged();
                        }
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_LONG).show();
            }
        });
        return;

    }
}