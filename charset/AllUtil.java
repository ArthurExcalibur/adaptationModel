package com.dazoo.meshwifi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.IntRange;
import android.text.Spanned;
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
     * 对输入框进行输入字节数限制，适配中英文混合输入。这里中文用的utf-8编码，一个中文占3个字节。最多输入32个字节
     * @param dest 输入框已经有的内容
     * @param source 要输入的内容
     * @return 裁剪后的内容
     */
    public static CharSequence subSSIDText(Spanned dest, CharSequence source){
        int length = 0;
        for (int i = 0; i < dest.length(); i++) {
            if (dest.charAt(i) > 255) {
                length += 3;
            } else {
                length++;
            }
        }
        int length1 = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) > 255) {
                length += 3;
            } else {
                length++;
            }
        }
        if(length + length1 > 32){
            if(length >= 32){
                // 输入框内已经有32个字符则返回空字符
                return "";
            }else{
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < source.length(); i++) {
                    if(source.charAt(i) > 255)
                        length += 3;
                    else
                        length++;
                    if(length > 32)
                        break;
                    else
                        builder.append(source.charAt(i));
                }
                if(builder.length() >= 1){
                    return builder;
                }else{
                    return "";
                }
            }
        }
        return null;
    }


}
