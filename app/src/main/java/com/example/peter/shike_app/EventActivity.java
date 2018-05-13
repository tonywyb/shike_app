package com.example.peter.shike_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.*;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EventActivity extends AppCompatActivity {

    private int which, eventID;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private Context mContext;
    private Button eventcontentret, deletebtn, reportbtn, comment_send, contactBtn;
    private TextView event_content, event_title, eventcontenttitle, event_user;
    private ImageButton newComment;
    private RelativeLayout rl_input;
    private TextView hide;
    private EditText comment_content;
    private TextView helper;
    private String mobile;
    private ImageView testImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_content);
        mContext = EventActivity.this;
        eventcontenttitle = (TextView) findViewById(R.id.eventcontenttitle);
        eventcontentret = (Button) findViewById(R.id.eventcontentret);
        eventcontentret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reportbtn = (Button) findViewById(R.id.reportbtn);
        rl_input = (RelativeLayout)findViewById(R.id.rl_input);
        newComment = (ImageButton)findViewById(R.id.newComment);
        hide = (TextView)findViewById(R.id.hide_down);
        comment_content = (EditText)findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);

        //test
        testImage = (ImageView)findViewById(R.id.testImage);
        String picURL = "http://ch.huyunfan.cn/IMG_9211.JPG";
        Picasso.with(mContext)
                .load(picURL)
                .into(testImage);

        Bundle bd = getIntent().getExtras();
        eventID = bd.getInt("eventID");
        which = bd.getInt("which");
        final Event event = PreferenceUtil.getEvent(eventID);
        event_title.setText(event.getTitle());
        event_content.setText(event.getDescription());
        event_user.setText(event.getUsername());

        newComment.setVisibility(View.GONE);
        if (which != 1 && event.getType() != 2) {
            newComment.setVisibility(View.VISIBLE);
            newComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出输入法
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    // 显示评论框
                    newComment.setVisibility(View.GONE);
                    rl_input.setVisibility(View.VISIBLE);

                }
            });
            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 隐藏评论框
                    newComment.setVisibility(View.VISIBLE);
                    rl_input.setVisibility(View.GONE);
                    // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                    InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);

                }
            });

            comment_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PreferenceUtil.islogged) {
                        final String comment = comment_content.getText().toString();
                        alert = null;
                        builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
                        alert = builder.setMessage("是否确定发布？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mContext, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (comment.equals("")) {
                                            Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
                                            alert.dismiss();
                                        } else {
                                            postComment(comment, PreferenceUtil.userID, eventID);
                                            alert.dismiss();
                                            comment_content.setText("");
                                        }
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();                    //显示对话框
                    } else {
                        Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void reportEventAsyncHttpClientPost(final int eventID, final Event event) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://120.25.232.47:8002/reportOutDate/";
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
                Toast.makeText(mContext, "举报成功", Toast.LENGTH_SHORT).show();
                event.setOutdate(1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_LONG).show();
            }
        });
        return;
    }

    public void postComment(final String comment, final int userID, final int eventID) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://120.25.232.47:8002/postComment/";
        //请求的参数对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", comment);
            jsonObject.put("publisherID", userID);
            jsonObject.put("fatherID", eventID);
            jsonObject.put("fatherType", "event");
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
                Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                Comment tmpcomment = new Comment();
                try {
                    tmpcomment.setCommentID(response.getInt("commentID"));
                    tmpcomment.setFatherID(eventID);
                    tmpcomment.setContent(comment);
                    tmpcomment.setFatherType("event");
                    tmpcomment.setPublisherID(userID);
                    tmpcomment.setUsername(PreferenceUtil.username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PreferenceUtil.commentdatas.add(tmpcomment);
                PreferenceUtil.myAdapterforComment.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //Toast.makeText(mContext, "connection error!Error number is:" + statusCode,  Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }
}
