## 一些Android版本适配的问题

------
Updated 2019.04.22
### 关于网络请求
* ssl.Tls12SocketFactory.java
* ssl.MQTTManager.java
* 在Android4.X以下要用HTTPS请求时，客户端默认没有启用TLS1.1和TLS1.2导致请求一直失败
	> 解决方法：自定义SSLSocketFactory启用TLS 1.2，在生成OKHTTPClient时添加进去。如果有后台给定的SSL证书的话，可以根据证书内容生成SocketFactory，在建立连接时传入。

* 在Android9.0之后HTTP请求被默认禁止，使用会报异常
	> 解决方法：在清单文件中配置network_security_config.xml，其中将network_security_config属性置为true以打开HTTP开关。

------
Updated 2019.04.23
### 关于fileprovider
* fileprovider.FileProviderUtils.java
* fileprovider.file_paths.xml
* Android 7.0 禁止在应用外部公开 file:// URI，所以我们必须使用 content:// 替代 file://，这时主要需要 FileProvider 的支持，而因为 FileProvider 是 ContentProvider 的子类，所以需要在 AndroidManifest.xml 文件中进行注册，而又因为需要对真实的 filepath 进行映射，所以需要编写一个 xml 文档，用于描述可使用的文件夹目录，以及通过 name 去映射该文件夹目录。
    > 1.res/xml文件夹下定义file_paths，其中申明需要公开的文件目录
    > 
    > 2.清单文件中配置fileprovider
    > 
    > 3.应用中完成file://到content://的转换

------
Updated 2019.04.25
### 语言适配
* language.AllUtil.java
* Android7.0之前手机只能设置单一语言，获取系统语言采用：
    > 1.getResources().getConfiguration().locale
    > 
    > 2.Locale.getDefault()
* Android7.0之后手机可以设置多种语言，并指定优先级。此时系统返回的不再是单一的语言，而是一个语言列表（在手机设置-语言设置中可以查看），获取这个列表采用:
    >1.getResources().getConfiguration().getLocales()
    >
    > 2.LocaleList.getDefault()
* 使用locale.getLanguage()和locale.getCountry()来分别获取系统语言和国家
* 结论：Android7.0之后获取系统为APP调整后的默认语言：Locale.getDefault()；（Locale.getDefault() 和 LocaleList.getAdjustedDefault().get(0) 同等效果，还不需要考虑版本问题）

------
Updated 2019.04.30
### 各种编码格式下的字节问题
* charset.AllUtil.java
* 字节数表格

| 编码格式  | 中文字节数  | 英文字节数 |
| :------------: |:---------------:| :-----:|
| utf-8      | 3 | 1 |
| utf-16      | 4        |   4 |
| UTF-16BE | 2        |    2 |
| UTF-16LE | 2        |    2 |
| UTF-32 | 4        |    4 |
| UTF-32BE | 4        |    4 |
| UTF-32LE | 4        |    4 |
| unicode | 4        |    4 |
| GBK | 2        |    1 |
| GB2312 | 2        |    1 |
| GB18030 | 2        |    1 |
| ISO8859-1 | 1        |    1 |
| BIG5 | 2        |    1 |
| ASCII | 1        |    1 |

* 限制计算中英文混合输入的最大字节数：
	> 使用edittext自带的InputFilter过滤



### Android Studio搜索代码中的中文
* 使用正则：^((?!(\*|//)).)+[\u4e00-\u9fa5]全局搜索

------
Updated 2019.05.12
### 无线设置界面跳转
* 正常跳转：
    > Intent intent = new Intent();
    > 
    > intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
    > 
    > startActivity(intent);
    
* 部分魅族机型无法通过上面的跳转，处理方式:
    >Intent aaa = new Intent("android.settings.WIFI_SETTINGS");
    >
    >startActivity(aaa);
* 已知机型：
	> MX5 Android5.1 Flyme6.1.1.0A