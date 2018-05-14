package com.example.peter.shike_app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class PreferenceUtil {

    /**
     * 是否显示欢迎界面,true表示显示，false表示不显示
     */
    public static MyAdapter myAdapter = null;
    public static MyAdapter myAdapter2 = null;
    public static MyAdapter myAdapterloc = null;
    public static MyAdapterforComment myAdapterforComment = null;
    public static ArrayList<Event> datas = new ArrayList<Event>();
    public static ArrayList<Event> mydatas = new ArrayList<Event>();
    public static ArrayList<Event> locdatas = new ArrayList<Event>();
    public static ArrayList<Comment> commentdatas = new ArrayList<Comment>();
    public static int maptype = 0;
    public static boolean islogged = false;
    public static int userID;
    public static String username;
    public static final String SHOW_GUIDE = "showguide";
    public static final String[] canteen = {"学一食堂", "学五食堂", "艺园食堂",
            "艺园二楼", "农园一楼", "农园二楼", "农园三楼", "勺园一楼",
            "勺园二楼", "燕南食堂", "佟园食堂", "畅春园食堂", "医学部",
            "松林包子"};

    public static Event getEvent(int eventID) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getEventId() == eventID) {
                return datas.get(i);
            }
        }
        return null;
    }
    public static void deletebyID(int eventID) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getEventId() == eventID) {
                datas.remove(i);
                break;
            }
        }
        for (int i = 0; i < mydatas.size(); i++) {
            if (mydatas.get(i).getEventId() == eventID) {
                mydatas.remove(i);
                break;
            }
        }
    }

    public static int getPlace(String p) {
        for (int i = 0; i < canteen.length; i++) {
            if (canteen[i] == p)
                return i;
        }
        return -1;
    }
    /**
     * 保存到Preference
     */
    public static void setBoolean(Context context, String key, boolean value) {
        // 得到SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 从Preference取出数据
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        // 返回key值，key值默认值是false
        return preferences.getBoolean(key, false);

    }
}
