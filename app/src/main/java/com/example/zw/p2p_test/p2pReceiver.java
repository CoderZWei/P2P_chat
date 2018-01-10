package com.example.zw.p2p_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2016/10/13.
 */
public class p2pReceiver extends BroadcastReceiver {
    WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private Activity activity;
    private WifiP2pManager.PeerListListener peerListListener;
    private List peers=new ArrayList();
    private Context mcontext;
    private boolean connected=false;

    public p2pReceiver( WifiP2pManager mManager,WifiP2pManager.Channel mChannel,Activity activity) {
        this.mChannel = mChannel;
        this.mManager = mManager;
        this.activity=activity;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        mcontext=context;
   String action=intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state=intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //wifi p2p is enabled


            }else
            {
                //wifi p2p is not enabled
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            if(mManager!=null){
                mManager.requestPeers(mChannel,peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            NetworkInfo info=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(info.isConnected()){

                mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {

                        String address=null;
                        if(info.groupFormed&&info.isGroupOwner){
                            address=info.groupOwnerAddress.getHostAddress().toString();


                        }else if(info.groupFormed){
                            address=info.groupOwnerAddress.getHostAddress();

                        }else {

                        }
                    }
                });
            }else {

            }

            // Respond to new connection or disconnections
        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
                .equals(action)) {
            // Respond to this device's wifi state changing
        }
        peerListListener=new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerlist) {
                peers.clear();
                peers.addAll(peerlist.getDeviceList());
                if(connected==false) {
                    connectPeer((WifiP2pDevice) peers.get(0));
                }

            }
        };

    }

    private void connectPeer(WifiP2pDevice device) {
        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                 connected=true;
                Toast.makeText(mcontext,"连接成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                // 连接失败
                Toast.makeText(mcontext,"连接失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

