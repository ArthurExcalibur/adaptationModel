package com.dazoo.meshwifi.mqtt;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dazoo.meshwifi.application.MeshApplication;
import com.dazoo.meshwifi.event.PubErrorEvent;
import com.dazoo.meshwifi.utils.ActivityUtil;
import com.dazoo.meshwifi.utils.DeviceUtil;
import com.dazoo.meshwifi.utils.LoggerUtil;
import com.dazoo.meshwifi.websocket.SocketConnection;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okio.Buffer;

class MQTTManager {
  

    /**
     * 创建Mqtt 连接
     *
     * @param broker Mqtt服务器地址
     * @param port Mqtt端口号
     * @param userName  用户名
     * @param password  密码
     * @param clientId  clientId
     */
    void createConnect(String broker,int port, String userName, String password, String clientId,String cert) {
        this.broker = broker;
        String brokerUrl = "ssl://"+broker+":"+port;
        try {
            client = new MqttAndroidClient(MeshApplication.getContext(), brokerUrl, clientId);
            client.setCallback(mqttCallback);

            conOpt = new MqttConnectOptions();
            conOpt.setAutomaticReconnect(false);
            //设置超时时间，单位：秒
            conOpt.setConnectionTimeout(60);
            //心跳包发送间隔，单位：秒
            conOpt.setKeepAliveInterval(10);
            conOpt.setCleanSession(true);

            if (password != null) {
                conOpt.setPassword(password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(userName);
            }
            SocketFactory factory = setCertificates(new Buffer().writeUtf8(cert).inputStream());
            if(factory != null)
                conOpt.setSocketFactory(factory);

            doConnect();
        } catch (Exception e) {
            LoggerUtil.e(TAG + "createConnect error : " + e.toString());
        }
    }

    private SocketFactory setCertificates(InputStream... certificates){
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(null);
            int index = 0;
            for(InputStream certificate : certificates){
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try{
                    if(certificate !=null)
                        certificate.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());
            return sslContext.getSocketFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
