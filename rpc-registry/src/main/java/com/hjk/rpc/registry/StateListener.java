package com.hjk.rpc.registry;

/**
 * Created by hanjk on 16/9/18.
 */
public interface StateListener{

    int DISCONNECTED = 0;

    int CONNECTED = 1;

    int RECONNECTED = 2;

    void stateChanged(int stat);
}
