##一些Android版本适配的问题

Updated 2019.04.22
###关于网络请求
*在Android4.X以下要用HTTPS请求时，客户端默认没有启用TLS1.1和TLS1.2
>解决方法：自定义SSLSocketFactory启用TLS 1.2，在生成OKHTTPClient时添加进去。
*在Android9.0之后HTTP请求被默认禁止，使用会报异常
>解决方法：在清单文件中配置network_security_config.xml，其中将network_security_config属性置为true以打开HTTP开关。
