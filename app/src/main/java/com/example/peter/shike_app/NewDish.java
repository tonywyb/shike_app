package com.example.peter.shike_app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.lang.reflect.Array;
import java.util.Arrays;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class NewDish extends Activity implements View.OnClickListener{

    private Button publishbtn = null;
    private Button neretbtn = null;
    private Context mContext = null;
    private Button exlist_lol;
    private TextView loc = null;
    private RadioGroup radgroup = null;
    private static final int msgKey1 = 1;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private EditText header, content;
    private Boolean hasbd = false;

    private Button list_tag;
    private TextView tags;
    private boolean[] checkItems;
    private ImageView upload;
    private Uri dishUri;
    private String dishPath;
    private File dishPic = null;
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private static final int REQUESTCODE = 0;//定义请求码常量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newdish);
        ((LinearLayout)findViewById(R.id.selectplace)).setVisibility(View.VISIBLE);
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            ((LinearLayout)findViewById(R.id.selectplace)).setVisibility(View.GONE);
            hasbd = true;
        }
        bindViews();

        mContext = NewDish.this;
        exlist_lol.setOnClickListener(this);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(NewDish.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.media_grid_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){
            //dishUri = Matisse.obtainResult(data).get(0);
            dishPath = Matisse.obtainPathResult(data).get(0);
            String tempPath = "file://" + Matisse.obtainPathResult(data).get(0);
            //Toast.makeText(mContext, uri.toString(), Toast.LENGTH_LONG).show();

            Picasso.with(mContext)
                    .load(tempPath)
                    .placeholder(R.mipmap.addphoto2)
                    .fit()
                    .into(upload);
        }
    }
    public void onStart(){
        super.onStart();
        //获取运行时权限
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
    }
    private void bindViews() {
        exlist_lol = (Button) findViewById(R.id.exlist_lol);
        publishbtn = (Button) findViewById(R.id.publishbtn);
        neretbtn = (Button) findViewById(R.id.neretbtn);
        loc = (TextView) findViewById(R.id.loc);
        header = (EditText) findViewById(R.id.header);
        content = (EditText) findViewById(R.id.content);
        publishbtn.setOnClickListener(this);
        neretbtn.setOnClickListener(this);
        exlist_lol.setOnClickListener(this);

        upload = (ImageView)findViewById(R.id.uploadPhoto);
        radgroup = (RadioGroup)findViewById(R.id.radiogroup);
        list_tag = (Button)findViewById(R.id.list_tag);
        tags = (TextView)findViewById(R.id.tag);

        list_tag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publishbtn:
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
                                if (header.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "名称不能为空", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                                else if (header.getText().toString().length() > 20) {
                                    Toast.makeText(mContext, "名称不能超过20个字", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                                else if (content.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "菜品描述不能为空", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                                else if (content.getText().toString().length() > 100) {
                                    Toast.makeText(mContext, "菜品描述不能超过100个字", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                                else if (loc.getText().toString().equals("")) {
                                    Toast.makeText(mContext, "请选择食堂", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                                else {
                                    final Dish dish = new Dish();
                                    dish.setPublisherID(PreferenceUtil.userID);
                                    dish.setPublisherName(PreferenceUtil.username);
                                    dish.setName(header.getText().toString());
                                    //dish.setCategory(((RadioButton)findViewById(radgroup.getCheckedRadioButtonId())).getText().toString());
                                    dish.setDescription(content.getText().toString());
                                    dish.setCanteenID(PreferenceUtil.getPlace(loc.getText().toString()));
                                    //compress the picture with Luban
                                    Luban.with(mContext)
                                            .load(dishPath)
                                            .setCompressListener(new OnCompressListener() {
                                                @Override
                                                public void onStart() {
                                                }
                                                @Override
                                                public void onSuccess(File file) {
                                                    dishPic = file;
                                                    dishByAsyncHttpClientPost(dish);
                                                    finish();
                                                }
                                                @Override
                                                public void onError(Throwable e) {
                                                    Toast.makeText(mContext, "图片压缩出错", Toast.LENGTH_LONG).show();
                                                }
                                            }).launch();
                                    dishByAsyncHttpClientPost(dish);
                                    finish();
                                }
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();                    //显示对话框
                break;
            case R.id.neretbtn:
                finish();
                break;
            case R.id.list_tag:
                checkItems = new boolean[]{false, false, false, false};
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle("选择菜品标签")
                        .setMultiChoiceItems(PreferenceUtil.tag, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkItems[which] = isChecked;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String result = "";
                                int count = 0;
                                for (int i = 0; i < checkItems.length; i++){
                                    if (checkItems[i]) {
                                        result += PreferenceUtil.tag[i] + " ";
                                        count++;
                                    }
                                    if (count == 5) {
                                        result += "......";
                                        break;
                                    }
                                }
                                tags.setText(result);
                            }
                        })
                        .create();
                alert.show();
                break;
            case R.id.exlist_lol:
                alert = null;
                builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setCancelable(true);
                alert = builder.setTitle("食堂选择")
                        .setItems(PreferenceUtil.canteen, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loc.setText(PreferenceUtil.canteen[which]);
                            }
                        }).create();
                alert.show();
                Window dialogWindow = alert.getWindow();
                WindowManager m = this.getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                Point size = new Point();
                d.getSize(size);
                p.height = (int) (size.y * 0.6);
                p.width = (int) (size.x * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
                dialogWindow.setAttributes(p);
                break;
            default:
                alert.dismiss();
        }
    }
    public void dishByAsyncHttpClientPost(final Dish dish) {
        //创建异步请求对象
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        String url = "http://ch.huyunfan.cn/PHP/dish/addDish.php";
        RequestParams params = new RequestParams();
        try{
            /*File photo = null;
            if (dishPath != null)
                photo = new File(dishPath);*/
            if (dishPic != null)
                params.put("photo", dishPic);
            params.put("name", dish.getName());
            params.put("canteenID", dish.getCanteenID() + 1);
            params.put("description", dish.getDescription());
            //params.put("category", dish.getCategory());
            params.put("publisherID", dish.getPublisherID());
        }catch (Exception e){
            e.printStackTrace();
        }
        client.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int result = (response.getInt("addDishStatus"));
                    if (result == 0)
                        Toast.makeText(mContext, "发布成功", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(mContext, "发布失败", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }
}
