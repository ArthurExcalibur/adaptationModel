package com.dazoo.meshwifi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.ScrollView;

import com.dazoo.meshwifi.R;
import com.dazoo.meshwifi.application.MeshApplication;
import com.dazoo.meshwifi.mqtt.MQTTTopics;
import com.dazoo.meshwifi.proterties.MacUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

public class AllUtil {

    /**
     * 根据Date获取具体天数(Date.getDay获取的数字表示的是当天是那一周的第几天，也就是周几。。。)
     */
    public static int getDayByDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 根据Date获取具体月份(Date.getMonth获取的是0-11的月份数字。即正常月份减一)
     */
    public static int getMonthByDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 根据Date获取年份(Date.getYear获取的当年到1900年的数字。比如2018返回的是118)
     */
    public static int getYearByDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static boolean pingEnable(String port){
        try {
            Runtime runtime = Runtime.getRuntime();
            String command = "ping -c 1 " + port;
            Process process = runtime.exec(command);
            process.waitFor();
            int exit = process.exitValue();
            return exit == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断app当前语言环境是否为中文环境
     * @return true为中文环境
     */
    public static boolean isChineseDefaultLanguage(){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        String language = Locale.getDefault().getLanguage();
        return "zh".equals(language.trim().toLowerCase());
    }


    /**
     * 根据年份和月份获取当月天数
     * @param year 年份
     * @param month 月份0-11
     * @return 当月的天数
     */
    public static int getDayNumber(int year,int month){
        int day;
        if(year % 4 == 0 && year %100 != 0|| year % 400 == 0){
            day = 29;
        }else{
            day = 28;
        }
        switch (month){
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                return day;

        }
        return day;
    }
	
    /**
     * http://blog.csdn.net/lyy1104/article/details/40048329
     * scrollview截图
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
