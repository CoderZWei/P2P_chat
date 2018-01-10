package com.example.zw.p2p_test;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private final IntentFilter intentFilter=new IntentFilter();
    WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private p2pReceiver receiverP2p;


 private Socket socket=null;

    private Button Btn;
    private TextView text;
    private MessageReceiver receiver;
    private String ip_address="192.168.49.1";

    private List<Msg>msgList=new ArrayList<Msg>();
    private Msg_adapter adapter;
    private ListView msgListview;
    private MyDatabaseHelper helper;
    private Cursor cursor;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
        mManager.discoverPeers(mChannel,new WifiP2pManager.ActionListener(){

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });


        text=(TextView)findViewById(R.id.text);
        msgListview=(ListView)findViewById(R.id.listview_msg);
        Btn=(Button)findViewById(R.id.Btn);

        helper=new MyDatabaseHelper(SecondActivity.this,"Message.db",null,1);
        db=helper.getWritableDatabase();
         cursor=db.query("message_data",null,null,null,null,null,null);
        if(cursor.moveToFirst()){

            do {

                String content=cursor.getString(cursor.getColumnIndex("content"));
                int type= cursor.getInt(cursor.getColumnIndex("type"));

                Msg msg = new Msg(content, type);
                msgList.add(msg);
            }while (cursor.moveToNext());
        }else {

        }

       cursor.close();

        adapter=new Msg_adapter(SecondActivity.this,R.layout.msg_item,msgList);
        msgListview.setAdapter(adapter);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyThread(text.getText().toString()).start();
                Msg msg=new Msg(text.getText().toString(),Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyDataSetChanged();


                    ContentValues values=new ContentValues();
                    values.put("content",text.getText().toString());
                    values.put("type",Msg.TYPE_SENT);
                    db.insert("message_data",null,values);



                text.setText("");
            }
        });

        Intent intent=new Intent(this,AndroidService.class);
        startService(intent);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.zw.p2p_test_MESSAGE");
        receiver=new MessageReceiver();
        registerReceiver(receiver,intentFilter);





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent(this,AndroidService.class);
        stopService(intent);
        unregisterReceiver(receiverP2p);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiverP2p=new p2pReceiver(mManager,mChannel,SecondActivity.this);
        registerReceiver(receiverP2p,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverP2p);
    }








    class MyThread extends Thread{
       private  String txt;
        public MyThread(String str){
            txt=str;
        }

        @Override
        public void run() {
            super.run();
            //定义消息

            try{
                //连接服务器 并设置连接超时为5秒
                socket=new Socket();
                socket.connect(new InetSocketAddress(ip_address,30000),5000);
                OutputStream ou=socket.getOutputStream();

                    ou.write(getLocalIpAddress().getBytes("gbk"));
                    ou.write("ip".getBytes("gbk"));

               //获得输入输出流

                ou.write(txt.getBytes("gbk"));

                ou.flush();

                ou.close();
                socket.close();

            }catch (SocketTimeoutException aa){

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
        String message=intent.getStringExtra("message");

                ip_address=intent.getStringExtra("address");
                ContentValues values=new ContentValues();
                values.put("content",message);
                values.put("type",Msg.TYPE_RECEIVED);
                db.insert("message_data",null,values);


            Msg msg=new Msg(message,Msg.TYPE_RECEIVED);
            msgList.add(msg);
            adapter.notifyDataSetChanged();

        }
    }
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&& inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();

                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

}
