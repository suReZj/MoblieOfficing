package com.r2.scau.moblieofficing.smack;

import android.util.Log;



import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * 服务器连接监听
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class SmackConnectionListener implements ConnectionListener {

    @Override
    public void connected(XMPPConnection connection) {

        Log.d("mmmm","connection connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

        Log.d("mmmm","connection authenticated");
    }

    @Override
    public void connectionClosed() {

        Log.d("mmmm","connection connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {

        Log.d("mmmm","connectionClosedOnError");
    }

    @Override
    public void reconnectingIn(int seconds) {

        Log.d("mmmm","connection reconnectingIn " + seconds + " second");
    }

    @Override
    public void reconnectionFailed(Exception e) {

        Log.d("mmmm","reconnectionFailed");
    }

    @Override
    public void reconnectionSuccessful() {

        Log.d("mmmm","reconnectionSuccessful");
    }
}
