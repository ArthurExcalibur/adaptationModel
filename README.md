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
    > res/xml文件夹下定义file_paths，其中申明需要公开的文件目录
    > 清单文件中配置fileprovider
    > 应用中完成file://到content://的转换