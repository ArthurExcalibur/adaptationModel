package com.dazoo.meshwifi.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 类：FileProviderUtils
 * 从APP向外共享的文件URI时，必须使用该类进行适配，否则在7.0以上系统，会报错：FileUriExposedException(文件Uri暴露异常)
 *
 * 搭配xml/file_paths.xml使用
 */
public class FileProviderUtils {

    /**
     * 从文件获得URI
     * @param activity 上下文
     * @param file 文件
     * @return 文件对应的URI(适配7.0以上)
     */
    public static Uri uriFromFile(Activity activity, File file) {
        Uri fileUri;
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String p = activity.getPackageName() + ".FileProvider";
            fileUri = FileProvider.getUriForFile(
                    activity,
                    p,
                    file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

}
