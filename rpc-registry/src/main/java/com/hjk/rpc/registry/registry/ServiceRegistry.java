package com.hjk.rpc.registry.registry;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.registry.StateListener;

/**
 * 服务注册接口
 * Created by hanjk on 16/9/7.
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     */
    void register(ServiceObject serviceObject);

    void addStateListener(StateListener stateListener);

    void notifyListener(int stat);
}
