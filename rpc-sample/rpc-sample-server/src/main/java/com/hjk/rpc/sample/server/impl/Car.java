package com.hjk.rpc.sample.server.impl;

import com.hjk.rpc.sample.api.Transport;

/**
 * Created by hanjk on 16/9/9.
 */
public class Car implements Transport {
    @Override
    public String getName() {
//        throw new IllegalArgumentException("数据错误!");
        return "car";
    }

    @Override
    public void start(String driver) {
        System.out.println("driver = [" + driver + "] begin start this car!");
    }
}
