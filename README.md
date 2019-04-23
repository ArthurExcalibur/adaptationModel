##一些Android版本适配的问题

Updated 2019.04.22
###关于网络请求
* ssl.Tls12SocketFactory.java
* ssl.MQTTManager.java
* 在Android4.X以下要用HTTPS请求时，客户端默认没有启用TLS1.1和TLS1.2导致请求一直失败
	> 解决方法：自定义SSLSocketFactory启用TLS 1.2，在生成OKHTTPClient时添加进去。如果有后台给定的SSL证书的话，可以根据证书内容生成SocketFactory，在建立连接时传入。

* 在Android9.0之后HTTP请求被默认禁止，使用会报异常
	> 解决方法：在清单文件中配置network_security_config.xml，其中将network_security_config属性置为true以打开HTTP开关。
