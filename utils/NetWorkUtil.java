package com.dazoo.meshwifi.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.util.List;

//检测网络状态
public class NetWorkUtil {

    public static boolean isNetWorkConnected(Context context){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取手机当前连接的SSID
     * @param activity 上下文
     * @return  WIFI 的SSID
     */
    public static String getWIFISSID(Activity activity) {
        String ssid="unknown id";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O||Build.VERSION.SDK_INT==Build.VERSION_CODES.P) {
            if(DeviceUtil.getDeviceBrand().toUpperCase().equals("HUAWEI")){
                return getWIFISSID4HUAWEI(activity);
            }else{
                WifiManager mWifiManager = (WifiManager) activity.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                if(mWifiManager != null){
                    WifiInfo info = mWifiManager.getConnectionInfo();
                    return info.getSSID();
                }
            }
        } else if (Build.VERSION.SDK_INT==Build.VERSION_CODES.O_MR1){
            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connManager != null){
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
                if (networkInfo.isConnected()) {
                    if (networkInfo.getExtraInfo()!=null){
                        return networkInfo.getExtraInfo();
                    }
                }
            }
        }
        return ssid;
    }

    /**
     * Android 9:HUAWEI ALP-AL00 EMUI9.0.0
     * Android 6:HUAWEI CRR-UL00 EMUI4.0.3
     */
    private static String getWIFISSID4HUAWEI(Activity activity){
        String ssid = "unknown id";
        WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager != null){
            WifiInfo info = mWifiManager.getConnectionInfo();
            int networkId = info.getNetworkId();
            List<WifiConfiguration> configurationList = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration c : configurationList) {
                if(c.networkId == networkId){
                    ssid = c.SSID;
                    break;
                }
            }
        }
        return ssid;
    }

}
