package com.example.zw.p2p_test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zw on 2016/10/20.
 */
public class AndroidService extends Service{
    private MyBinder mBInder=new MyBinder();
    static Socket socket;

    class MyBinder extends Binder{

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBInder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            final ServerSocket service = new ServerSocket(30000);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                             socket=service.accept();

                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  String line=null;
                                  InputStream input;

                                  try {
                                      input=socket.getInputStream();
                                      BufferedReader bff=new BufferedReader(new InputStreamReader(input));
                                      String buffer="";
                                      //获取客户端的信息
                                      while((line=bff.readLine())!=null){
                                        buffer+=line;
                                      }
                                      Intent intent=new Intent("com.example.zw.p2p_test_MESSAGE");

                                          String [] temp = null;
                                          temp=buffer.split("ip");
                                          intent.putExtra("address",temp[0]);
                                          intent.putExtra("message",temp[1]);

                                      sendBroadcast(intent);
                                      //关闭输入输出流
                                      bff.close();
                                      input.close();
                                      socket.close();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                              }
                          }).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
