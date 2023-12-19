package com.wisecashier.ecr.sdk.jmdns;

import android.content.Context;

import com.wisecashier.ecr.sdk.util.NetUtils;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;


public class JMdnsManager implements ServiceListener {
    private final static String CLIENT_REMOTE_TYPE = "_ecr-hub-client._tcp.local.";

    public final static String SERVER_REMOTE_TYPE = "_ecr-hub-server._tcp.local.";
    private JmDNS mJmdns;
    SearchServerListener mSearchServerListener;

    public JMdnsManager(Context context) {
        new Thread(() -> {
            InetAddress ip = NetUtils.getLocalIPAddress();
            try {
                mJmdns = JmDNS.create(ip, "ECRHubServerName");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void searchServer(SearchServerListener listener) {
        mSearchServerListener = listener;
        if (null != mJmdns) {
            mJmdns.addServiceListener(SERVER_REMOTE_TYPE, this);
        }
    }

    @Override
    public void serviceAdded(ServiceEvent event) {

    }

    @Override
    public void serviceRemoved(ServiceEvent event) {

    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        ServiceInfo info = event.getInfo();
        if (null != info) {
            String ipv4 = "";
            if (null != info.getInet4Addresses()) {
                ipv4 = info.getInet4Addresses()[0].getHostAddress();
            }
            mSearchServerListener.onServerFind(ipv4, "" + info.getPort(), info.getName());
        }
    }
}
